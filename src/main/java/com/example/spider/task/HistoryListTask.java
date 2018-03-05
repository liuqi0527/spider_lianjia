package com.example.spider.task;

import java.time.LocalDateTime;

import com.example.runner.TaskRunner;
import com.example.DebugLogger;
import com.example.spider.domain.CommunityData;
import com.example.spider.domain.HistoryData;
import com.example.spider.repository.CommunityRepository;
import com.example.spider.repository.HistoryDataRepository;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-02-24]
 */
@Component
public class HistoryListTask implements TaskRunner {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private HistoryDataRepository historyDataRepository;

    @Override
    public void run() {
        for (CommunityData data : communityRepository.findAll()) {
            if (data.getFetchHistoryTime() == null || data.getFetchHistoryTime().plusDays(30).isBefore(LocalDateTime.now())) {
                try {
                    resolveCommunity(data);
                    data.setFetchHistoryTime(LocalDateTime.now());
                    communityRepository.save(data);
                } catch (Exception e) {
                    DebugLogger.error(e, "fetch community history data error", data.getId(), data.getName());
                }
            }
        }

    }

    private void resolveCommunity(CommunityData communityData) throws Exception {
        int pageSize = getPageSize(communityData);
        if (pageSize <= 0) {
            return;
        }

        DebugLogger.info(String.format("%s 成交记录共 %s页", communityData.getName(), pageSize));

        for (int page = 1; page <= pageSize; page++) {
            String pageUrl = String.format("https://bj.lianjia.com/chengjiao/pg%dc%d/", page, communityData.getId());
            Document document = Jsoup.parse(getHtml(pageUrl));
            document.select("div.info").forEach(element -> {
                try {
                    Element titleElement = element.select("div.title>a").first();
                    String link = titleElement.attr("href");
                    String houseId = StringUtils.substringBefore(StringUtils.substringAfterLast(link, "/"), ".");

                    HistoryData data = new HistoryData();
                    data.setId(houseId);
                    data.setCommunityId(communityData.getId());
                    data.setDistrictId(communityData.getDistrictId());
                    data.setTitle(titleElement.text());
                    data.setLink(link);
                    data.setDealDate(element.select(".dealDate").first().text());
                    data.setTotalPrice(element.select(".totalPrice").first().text());
                    data.setUnitPrice(element.select(".unitPrice").first().text());
                    historyDataRepository.save(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            DebugLogger.info(String.format("解析进度 %d/%d", page, pageSize));
        }
    }

    private int getPageSize(CommunityData data) throws Exception {
        String url = "https://bj.lianjia.com/chengjiao/c" + data.getId() + "/";
        Document document = Jsoup.parse(getHtml(url));
        Element pageElement = document.select("div.page-box[page-data]").first();

        //{"totalPage":8,"curPage":1}
        if (pageElement == null) {
            DebugLogger.info(String.format("%s(%s) 未发现历史成交数据", data.getName(), data.getId()));
            return 0;
        }
        String pageData = pageElement.attr("page-data");
        String pageSize = StringUtils.substringBefore(StringUtils.substringAfter(pageData, ":"), ",");
        return Integer.parseInt(pageSize);
    }
    //

    private String getHtml(String url) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(new HttpGet(url));
        return EntityUtils.toString(response.getEntity());
    }
}
