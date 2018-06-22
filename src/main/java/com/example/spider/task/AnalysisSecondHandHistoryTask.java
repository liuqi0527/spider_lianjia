package com.example.spider.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.example.TaskRunner;
import com.example.spider.domain.AnalysisSecondHandHistoryData;
import com.example.spider.repository.AnalysisSecondHandHistoryRepository;
import com.example.DebugLogger;
import com.example.spider.domain.SecondHandHistoryData;
import com.example.spider.domain.TransactionData;
import com.example.spider.repository.SecondHandHistoryDataRepository;
import com.example.util.LocalCache;
import com.example.util.Util;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Component
public class AnalysisSecondHandHistoryTask implements TaskRunner {

    private static final LocalDate startDate = LocalDate.of(2015, 1, 1);

    private static final int PAGE_SIZE = 50000;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private SecondHandHistoryDataRepository historyDataRepository;

    @Autowired
    private AnalysisSecondHandHistoryRepository analysisHistoryRepository;

    @Autowired
    private LocalCache cache;

    Map<AnalysisSecondHandHistoryData.Key, AnalysisSecondHandHistoryData> resultMap = new HashMap<>();

    private int noDateDealCount = 0;

    private int noPriceDealCount = 0;

    @Override
    public void run() {
        analysis();
    }

    private void analysis() {
        Page<SecondHandHistoryData> firstPage = historyDataRepository.findAll(new PageRequest(1, PAGE_SIZE));
        int totalPage = firstPage.getTotalPages();

        DebugLogger.info(String.format("共%s页记录，开始分析数据...", totalPage));
        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {
            Page<SecondHandHistoryData> page = pageIndex == 1 ? firstPage : historyDataRepository.findAll(new PageRequest(pageIndex, PAGE_SIZE));
            page.forEach(historyData -> {
                List<TransactionData> transactionList = resolveRecord(historyData.getDealRecords());
                if (!transactionList.isEmpty()) {
                    for (TransactionData data : transactionList) {
                        build(historyData, data.getTotalPrice(), data.getUnitPrice(), data.getDealTime());
                    }
                } else {
                    build(historyData, historyData.getTotalPrice(), historyData.getUnitPrice(), historyData.getDealDate());
                }
            });
            DebugLogger.info(String.format("---- %s/%s", pageIndex, totalPage));
        }


        resultMap.values().forEach(history -> history.setAvgPrice(formatDouble(history.getTotalPrice() / history.getTotalPriceCount())));
        DebugLogger.info(String.format("分析完成，未记录日期的数据共%d条， 未记录价格的数据共%d条", noDateDealCount, noPriceDealCount));

        DebugLogger.info("开始持久化操作...");
        analysisHistoryRepository.deleteAll();
        analysisHistoryRepository.save(resultMap.values());
        DebugLogger.info("数据持久化完成");
    }

    private double formatDouble(double value) {
        return Math.round(value);
    }

    private void build(SecondHandHistoryData historyData, String totalPriceStr, String unitPriceStr, String dealDate) {
        LocalDate date = resolveDate(dealDate);
        if (date == null) {
            noDateDealCount++;
            return;
        }
        if (date.isBefore(startDate) || !cache.isInterest(historyData.getDistrictId())) {
            //进行日期和区域筛选
            return;
        }

        String dateString = Util.toYearMonth(date);
        long totalPrice = resolveNumber(totalPriceStr, "万", 10000);
        long unitPrice = resolveNumber(unitPriceStr, "元/平", 1);

        AnalysisSecondHandHistoryData.Key key = new AnalysisSecondHandHistoryData.Key(historyData.getDistrictId(), dateString);
        AnalysisSecondHandHistoryData history = resultMap.computeIfAbsent(key, AnalysisSecondHandHistoryData::new);
        history.setDealAmount(history.getDealAmount() + 1);

        if (unitPrice > 0) {
            history.setTotalPriceCount(history.getTotalPriceCount() + 1);
            history.setTotalPrice(history.getTotalPrice() + unitPrice);
        } else {
            noPriceDealCount++;
        }
    }

    private LocalDate resolveDate(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        if (StringUtils.contains(string, "30天内成")) {
            return LocalDate.now();
        }

        string = StringUtils.replace(string, ".", "-");
        if (string.length() < 10) {
            string = string + "-01";
        }
        try {
            return LocalDate.parse(string, formatter);
        } catch (Exception e) {
            DebugLogger.error(e, "解析时间格式错误", string);
        }

        return null;
    }

    private long resolveNumber(String value, String suffix, int scale) {
        String result = StringUtils.substringBefore(value, suffix);
        if (NumberUtils.isCreatable(result)) {
            return (long) (NumberUtils.createDouble(result) * scale);
        }
        return 0;
    }

    private List<TransactionData> resolveRecord(String dealRecord) {
        if (StringUtils.isBlank(dealRecord)) {
            return Collections.emptyList();
        }

        try {
            return Util.parse(dealRecord, new TypeReference<List<TransactionData>>() {
            });
        } catch (Exception e) {
            DebugLogger.error(e);
            return Collections.emptyList();
        }
    }
}
