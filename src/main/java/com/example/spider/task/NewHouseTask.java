package com.example.spider.task;

import com.example.DebugLogger;
import com.example.runner.TaskRunner;
import com.example.spider.domain.CommunityData;
import com.example.spider.repository.HistoryDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-02-24]
 */
@Component
public class NewHouseTask implements TaskRunner {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private HistoryDataRepository historyDataRepository;

    @Override
    public void run() {

        DebugLogger.info("");
    }

    private int getPageSize(CommunityData data) throws Exception {
        String url = "https://bj.fang.lianjia.com/loupan/";
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
