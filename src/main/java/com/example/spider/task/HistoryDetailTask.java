package com.example.spider.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.TaskRunner;
import com.example.DebugLogger;
import com.example.spider.domain.HistoryData;
import com.example.spider.domain.TransactionData;
import com.example.spider.repository.HistoryDataRepository;
import com.example.util.Util;

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
public class HistoryDetailTask implements TaskRunner {

    @Autowired
    private HistoryDataRepository historyDataRepository;

    @Override
    public void run() {
        updateFuzzyPriceData();
    }

    public void updateAllData() {
        updateDataList(historyDataRepository.findNeedUpdateDatas(LocalDateTime.now().plusDays(-30)));
    }

    public void updateFuzzyPriceData() {
        updateDataList(historyDataRepository.findByTotalPriceContains("*"));
    }

    private void updateDataList(List<HistoryData> list) {
        int size = list.size();

        DebugLogger.info(String.format("详细信息待更新数量 --> %s", size));

        for (int index = 0; index < list.size(); index++) {
            HistoryData data = list.get(index);
            try {
                resolveHistoryDetail(data);
                DebugLogger.info(String.format("进度 %d/%d", index + 1, size));
            } catch (Exception e) {
                DebugLogger.error(e, "查询详细信息失败", data.getId(), data.getTitle());
            }
        }
    }

    private void resolveHistoryDetail(HistoryData data) throws Exception {
        String url = "https://bj.lianjia.com/chengjiao/" + data.getId() + ".html";
        Document document = Jsoup.parse(getHtml(url), "utf-8");

        priceData(document, data);

        Elements baseElements = document.select("div.base").select("li");
        data.setLayoutType(baseElements.get(0).ownText());
        data.setFloor(baseElements.get(1).ownText());
        data.setSquare(baseElements.get(2).ownText());
        data.setBuildingType(baseElements.get(3).ownText());
        data.setDirect(baseElements.get(4).ownText());
        data.setBuildYear(baseElements.get(5).ownText());
        data.setDecoration(baseElements.get(6).ownText());
        data.setBuildingStruct(baseElements.get(7).ownText());
        data.setHeatingType(baseElements.get(8).ownText());
        data.setLimitYear(baseElements.get(9).ownText());
        data.setHasLift(baseElements.get(10).ownText());

        Elements transactElements = document.select("div.transaction").select("li");
        data.setTransactType(transactElements.get(1).ownText());
        data.setHangTime(transactElements.get(2).ownText());
        data.setHouseType(transactElements.get(3).ownText());
        data.setTaxInfo(transactElements.get(4).ownText());
        data.setOwnType(transactElements.get(5).ownText());

        //可能会有多个历史成交记录
        List<TransactionData> transactionList = new ArrayList<>();
        Elements recordElements = document.select("ul.record_list>li");
        for (int i = 0; i < recordElements.size(); i++) {
            Element record = recordElements.get(i);
            String totalPrice = record.getElementsByTag("span").text();
            String[] detailInfo = StringUtils.split(record.getElementsByTag("p").text(), ",");

            TransactionData transactionData = new TransactionData();
            transactionData.setTotalPrice(totalPrice);
            transactionData.setDealTime(detailInfo[detailInfo.length - 1]);
            if (StringUtils.contains(detailInfo[0], "单价")) {
                transactionData.setUnitPrice(StringUtils.substringAfter(detailInfo[0], "单价"));
            }
            if (i == 0) {
                data.setDealDate(transactionData.getDealTime());
            }
        }


        data.setDealRecords(Util.toString(transactionList));
        data.setUpdateTime(LocalDateTime.now());
        historyDataRepository.save(data);
    }

    private void priceData(Document document, HistoryData data) {
        Element priceElement = document.select("div.price").first();
        if (priceElement == null) {
            DebugLogger.error("未发现交易数据", data.getId(), data.getTitle());
            return;
        }
        Element totalPriceElement = priceElement.select(".dealTotalPrice>i").first();
        if (totalPriceElement == null) {
            DebugLogger.error("未发现交易数据", data.getId(), data.getTitle());
            return;
        }

        data.setTotalPrice(totalPriceElement.text());
        data.setUnitPrice(priceElement.select("b").first().text());
        Elements msgElements = document.select("div.msg").select("label");
        data.setHangPrice(msgElements.get(0).text());
        data.setHangDuration(msgElements.get(1).text());
        data.setUpdatePriceCount(msgElements.get(2).text());
        data.setDaikanCount(msgElements.get(3).text());
        data.setForcusCount(msgElements.get(4).text());
        data.setBrowseCount(msgElements.get(5).text());
    }

    private String getHtml(String url) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(new HttpGet(url));
        return EntityUtils.toString(response.getEntity());
    }
}
