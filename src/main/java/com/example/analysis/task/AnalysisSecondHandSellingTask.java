package com.example.analysis.task;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.DebugLogger;
import com.example.TaskRunner;
import com.example.analysis.Domain.AnalysisSecondHandSellingData;
import com.example.analysis.repository.AnalysisSecondHandSellingRepository;
import com.example.spider.domain.SecondHandSellingData;
import com.example.spider.repository.SecondHandSellingRepository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-03-02]
 */
@Component
public class AnalysisSecondHandSellingTask implements TaskRunner {

    private LocalDate now = LocalDate.now();

    @Autowired
    private SecondHandSellingRepository secondHandRepository;

    @Autowired
    private AnalysisSecondHandSellingRepository analysisSecondHandRepository;

    private Map<Long, AnalysisSecondHandSellingData> map = new HashMap<>();

    @Override
    public void run() {
        DebugLogger.info("开始查询数据");
        List<SecondHandSellingData> list = secondHandRepository.findAll();
        int size = list.size();

        DebugLogger.info(String.format("查询完成，共%s条数据，开始统计", size));

        for (int index = 0; index < size; index++) {
            SecondHandSellingData data = list.get(index);

            AnalysisSecondHandSellingData analysis = map.computeIfAbsent(data.getDistrictId(), k -> new AnalysisSecondHandSellingData(data.getDistrictId(), now));
            analysis.setSaleAmount(analysis.getSaleAmount() + 1);
            analysis.setAvgPrice(analysis.getAvgPrice() + resolveNumber(data.getUnitPrice(), "元/平", 1));
        }
        map.values().forEach(this::calculateAvgPrice);

        DebugLogger.info("统计完成，开始持久化操作");
        analysisSecondHandRepository.deleteAllByUpdateDate(now);
        analysisSecondHandRepository.save(map.values());
        DebugLogger.info("数据持久化完成");
    }

    private long resolveNumber(String value, String suffix, int scale) {
        String result = StringUtils.substringBefore(value, suffix);
        if (NumberUtils.isCreatable(result)) {
            return (long) (NumberUtils.createDouble(result) * scale);
        }
        return 0;
    }

    private void calculateAvgPrice(AnalysisSecondHandSellingData secondHand) {
        double avgPrice = ((double) secondHand.getAvgPrice()) / secondHand.getSaleAmount();
        secondHand.setAvgPrice((int)(Math.round(avgPrice)));
    }
}
