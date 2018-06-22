package com.example.spider.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.DebugLogger;
import com.example.TaskRunner;
import com.example.spider.domain.AnalysisSecondHandHistoryData;
import com.example.spider.domain.AnalysisSecondHandSellingData;
import com.example.spider.repository.AnalysisSecondHandHistoryRepository;
import com.example.spider.repository.AnalysisSecondHandSellingRepository;
import com.example.spider.domain.SecondHandSellingData;
import com.example.spider.repository.SecondHandSellingRepository;
import com.example.util.LocalCache;
import com.example.util.Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 挂牌量统计，即当前想要出手的房屋数据统计
 * 由于统计数据涉及与上个月的交互计算，所以在每个月固定的一个日期统计最准确，例如每月月初
 * 否则由于统计间隔不平均，导致的计算的挂牌量新增值等并不是按月的数据，在参考数据时会产生误解
 *
 * 还由于新成交的数据链家并不会马上划入历史成交数据中，所以历史成交量的统计会有延迟，当时成交的数据至少一个月后才能统计得到
 * 所以这里每次都要重新计算所有的涉及跨月的数据列
 *
 * 统计内容包括：
 * 当时的挂牌量
 * 当时的挂牌均价
 * 当月的成交量
 *
 * 当月挂牌量绝对增量
 * 当月挂牌量新增值
 * </pre>
 *
 * @author LiuQi - [Created on 2018-03-02]
 */
@Component
public class AnalysisSecondHandSellingTask implements TaskRunner {

    @Autowired
    private SecondHandSellingRepository sellingRepository;

    @Autowired
    private AnalysisSecondHandSellingRepository analysisSellingRepository;

    @Autowired
    private AnalysisSecondHandHistoryRepository analysisHistoryRepository;

    @Autowired
    private LocalCache cache;

    //挂牌数据
    private Map<Long, Map<String, AnalysisSecondHandSellingData>> sellingDataMap = new HashMap<>();

    //历史成交数据
    private Map<Long, Map<String, AnalysisSecondHandHistoryData>> historyDataMap = new HashMap<>();


    @Override
    public void run() {
        DebugLogger.info("开始查询数据");
        String currentMonth = Util.toYearMonth(LocalDate.now());

        //成交量数据
        List<AnalysisSecondHandHistoryData> historyDataList = analysisHistoryRepository.findAll();
        for (AnalysisSecondHandHistoryData historyData : historyDataList) {
            Map<String, AnalysisSecondHandHistoryData> map = historyDataMap.computeIfAbsent(historyData.getDistrictId(), key -> new HashMap<>());
            map.put(historyData.getDate(), historyData);
        }


        //历史挂牌统计数据
        List<AnalysisSecondHandSellingData> preMonthDataList = analysisSellingRepository.findAll();
        for (AnalysisSecondHandSellingData data : preMonthDataList) {
            if (!StringUtils.equals(data.getDate(), currentMonth)) {
                Map<String, AnalysisSecondHandSellingData> map = sellingDataMap.computeIfAbsent(data.getDistrictId(), key -> new HashMap<>());
                map.put(data.getDate(), data);
            }
        }


        //本次统计的源数据
        List<SecondHandSellingData> list = sellingRepository.findAll();
        DebugLogger.info(String.format("查询完成，共%s条数据，开始统计", list.size()));

        //本月的策挂牌量，均价数据
        for (SecondHandSellingData data : list) {
            if (!cache.isInterest(data.getDistrictId())) {
                continue;
            }
            Map<String, AnalysisSecondHandSellingData> map = sellingDataMap.computeIfAbsent(data.getDistrictId(), key -> new HashMap<>());
            AnalysisSecondHandSellingData analysis = map.computeIfAbsent(currentMonth, key -> new AnalysisSecondHandSellingData(data.getDistrictId(), currentMonth));
            analysis.setSaleAmount(analysis.getSaleAmount() + 1);//在售数
            analysis.setAvgPrice(analysis.getAvgPrice() + resolveNumber(data.getUnitPrice(), "元/平", 1));
        }

        //计算本月挂牌均价
        for (Map<String, AnalysisSecondHandSellingData> map : sellingDataMap.values()) {
            AnalysisSecondHandSellingData data = map.get(currentMonth);
            if (data != null) {
                double avgPrice = ((double) data.getAvgPrice()) / data.getSaleAmount();
                data.setAvgPrice((int) (Math.round(avgPrice)));
            }
        }

        //计算所有月份相关联的数据
        for (Map<String, AnalysisSecondHandSellingData> map : sellingDataMap.values()) {
            for (AnalysisSecondHandSellingData data : map.values()) {
                //成交量
                AnalysisSecondHandHistoryData historyData = getHistoryData(data);
                if (historyData != null) {
                    data.setDealAmount(historyData.getDealAmount());
                }

                //挂牌量新增值
                AnalysisSecondHandSellingData nextMonthData = getSellingData(data, 1);
                if (nextMonthData != null) {
                    //本月初挂牌总量 + 本月新增挂牌量 - 本月成交量 = 下个月初挂牌总量
                    //本月新增挂牌量 = 下个月初挂牌总量 + 本月成交量 - 本月初挂牌总量
                    long newSaleAmount = nextMonthData.getSaleAmount() + data.getDealAmount() - data.getSaleAmount();
                    data.setNewSaleAmount(newSaleAmount);

                    //本月挂牌量的绝对增量
                    data.setAbsoluteIncrAmount(nextMonthData.getSaleAmount() - data.getSaleAmount());
                } else {
                    data.setNewSaleAmount(0);
                    data.setAbsoluteIncrAmount(0);
                }
            }
        }

        ArrayList<AnalysisSecondHandSellingData> sellingDataList = sellingDataMap.values().stream()
                .collect(ArrayList::new, (list1, map) -> list1.addAll(map.values()), ArrayList::addAll);

        DebugLogger.info("统计完成，开始持久化操作");
        analysisSellingRepository.deleteAllByDate(currentMonth);
        analysisSellingRepository.save(sellingDataList);
        DebugLogger.info("数据持久化完成");
    }

    private AnalysisSecondHandHistoryData getHistoryData(AnalysisSecondHandSellingData data) {
        Map<String, AnalysisSecondHandHistoryData> map = historyDataMap.get(data.getDistrictId());
        return map == null ? null : map.get(data.getDate());
    }

    private AnalysisSecondHandSellingData getSellingData(AnalysisSecondHandSellingData data, int monthDiffer) {
        LocalDate localDate = Util.fromYearMonth(data.getDate());
        String date = Util.toYearMonth(localDate.plusMonths(monthDiffer));
        Map<String, AnalysisSecondHandSellingData> map = sellingDataMap.get(data.getDistrictId());
        return map == null ? null : map.get(date);
    }

    private long resolveNumber(String value, String suffix, int scale) {
        String result = StringUtils.substringBefore(value, suffix);
        if (NumberUtils.isCreatable(result)) {
            return (long) (NumberUtils.createDouble(result) * scale);
        }
        return 0;
    }
}
