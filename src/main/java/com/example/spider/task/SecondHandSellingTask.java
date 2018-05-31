package com.example.spider.task;

import java.time.LocalDateTime;
import java.util.List;

import com.example.TaskRunner;
import com.example.DebugLogger;
import com.example.spider.domain.CommunityData;
import com.example.spider.domain.SecondHandSellingData;
import com.example.spider.repository.CommunityRepository;
import com.example.spider.repository.SecondHandSellingRepository;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-02-24]
 */
@Component
public class SecondHandSellingTask implements TaskRunner {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private SecondHandSellingRepository secondHandRepository;

    @Override
    public void run() {
        List<CommunityData> list = communityRepository.findAll();
        secondHandRepository.deleteAllInBatch();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CommunityData data = list.get(i);
            if (needFetch(data)) {
                try {
                    resolveCommunity(data, i + 1, size);
                    data.setFetchSecondHandTime(LocalDateTime.now());
                    communityRepository.save(data);
                } catch (Exception e) {
                    DebugLogger.error(e, "fetch community history data error", data.getId(), data.getName());
                }
            }
        }
    }

    private boolean needFetch(CommunityData data) {
//        return data.getFetchSecondHandTime() == null || data.getFetchSecondHandTime().plusHours(5).isBefore(LocalDateTime.now());
        return true;
    }

    private void resolveCommunity(CommunityData communityData, int index, int size) throws Exception {
        int pageSize = getPageSize(communityData);
        if (pageSize <= 0) {
            return;
        }

        DebugLogger.info(String.format("%s %s页   %d/%d", communityData.getName(), pageSize, index, size));

        for (int page = 1; page <= pageSize; page++) {
            String pageUrl = String.format("https://bj.lianjia.com/ershoufang/pg%dc%d/", page, communityData.getId());
            Document document = Jsoup.parse(getHtml(pageUrl));

            Elements elements = document.select("ul.sellListContent").select("div.info");
            elements.forEach(element -> {
                String title = null;
                String link = null;
                String id = null;
                try {
                    Element titleElement = element.select("div.title>a").first();
                    title = titleElement.text();
                    link = titleElement.attr("href");
                    id = StringUtils.substringBefore(StringUtils.substringAfterLast(link, "/"), ".");

                    SecondHandSellingData data = new SecondHandSellingData();
                    data.setId(id);
                    data.setLink(link);
                    data.setTitle(title);
                    data.setCommunityId(communityData.getId());
                    data.setDistrictId(communityData.getDistrictId());

                    data.setTotalPrice(element.select(".totalPrice").text());
                    data.setUnitPrice(StringUtils.substringAfter(element.select(".unitPrice").text(), "单价"));

                    String[] hoseInfo = StringUtils.split(element.select(".houseInfo").text(), "/");
                    data.setLayoutType(hoseInfo[1]);
                    data.setSquare(hoseInfo[2]);
                    data.setDirect(hoseInfo[3]);
                    data.setDecoration(hoseInfo.length >= 5 ? hoseInfo[4] : "");
                    data.setHasLift(hoseInfo.length >= 6 ? hoseInfo[5] : "无电梯");

                    String positionInfo = element.select("div.positionInfo").text();
                    String split = StringUtils.contains(positionInfo, "/") ? "/" : " ";
                    String[] positionInfos = StringUtils.split(positionInfo, split);
                    data.setFloor(positionInfos[0]);
                    data.setBuildYear(positionInfos[1]);

                    String followInfo = element.select(".followInfo").first().text();
                    String[] followInfos = StringUtils.split(followInfo, "/");
                    data.setForcusCount(followInfos[0]);
                    data.setDaikanCount(followInfos[1]);
                    data.setHangTime(element.select(".timeInfo").text());

                    data.setLocationDetail(element.select(".subway").text());

                    String taxFree = element.select("taxfree").text();
                    String five = element.select("five").text();
                    data.setTaxInfo(join(taxFree, five));
                    secondHandRepository.save(data);
                } catch (Exception e) {
                    DebugLogger.error(e, title, id, link);
                }
            });
            DebugLogger.info(String.format("---- %d/%d", page, pageSize));
        }
    }

    private String join(String value1, String value2) {
        if (StringUtils.isBlank(value1)) {
            return value2;
        } else if (StringUtils.isBlank(value2)) {
            return value1;
        }
        return StringUtils.join(value1, value2);
    }

    private int getPageSize(CommunityData data) throws Exception {
        String url = "https://bj.lianjia.com/ershoufang/c" + data.getId() + "/";
        Document document = Jsoup.parse(getHtml(url));
        Element pageElement = document.select("div.page-box[page-data]").first();

        //{"totalPage":8,"curPage":1}
        if (pageElement == null) {
            return 1;
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
