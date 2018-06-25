package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.spider.domain.AnalysisSecondHandHistoryData;
import com.example.spider.domain.AnalysisSecondHandSellingData;
import com.example.spider.repository.AnalysisSecondHandHistoryRepository;
import com.example.spider.repository.AnalysisSecondHandSellingRepository;
import com.example.spider.repository.SecondHandHistoryDataRepository;
import com.example.spider.task.*;
import com.example.util.LocalCache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    @Autowired
    private SecondHandHistoryListTask historyListTask;

    @Autowired
    private SecondHandHistoryDetailTask historyDetailTask;

    @Autowired
    private AnalysisSecondHandHistoryTask analysisHistoryTask;


    @Autowired
    private SecondHandSellingTask secondHandTask;

    @Autowired
    private AnalysisSecondHandSellingTask analysisSecondHandTask;


    @Autowired
    private SecondHandHistoryDataRepository historyDataRepository;

    @Autowired
    private NewHouseTask newHouseTask;


    @Autowired
    private AnalysisSecondHandSellingRepository analysisSellingRepository;

    @Autowired
    private AnalysisSecondHandHistoryRepository analysisHistoryRepository;


    @Autowired
    private LocalCache cache;

    private List<Pair<TaskRunner, String>> taskList = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        addTask(historyListTask, "拉取新成交数据");
        addTask(historyDetailTask, "更新成交价格");
        addTask(analysisHistoryTask, "分析历史成交数据");

        addTask(secondHandTask, "拉取在售二手房数据");
        addTask(analysisSecondHandTask, "分析在售二手房数据");
    }

    private void addTask(TaskRunner runner, String title) {
        taskList.add(Pair.of(runner, title));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        runTaskList();
    }

    private void runTaskList() {
        for (Pair<TaskRunner, String> pair : taskList) {
            if (pair.getLeft() != null) {
                DebugLogger.info(String.format("[开始] %s ...", pair.getRight()));
                pair.getLeft().run();
                DebugLogger.info(String.format("[完成] %s ", pair.getRight()));
            } else {
                DebugLogger.info(pair.getRight());
            }
        }
    }

    private void deleteUnInterestData() {
        //成交量数据
        List<AnalysisSecondHandHistoryData> deleteHistoryList = analysisHistoryRepository.findAll().stream()
                .filter(data -> !cache.isInterest(data.getDistrictId()))
                .collect(Collectors.toList());
        analysisHistoryRepository.delete(deleteHistoryList);


        //历史挂牌统计数据
        List<AnalysisSecondHandSellingData> deleteSellingList = analysisSellingRepository.findAll().stream()
                .filter(data -> !cache.isInterest(data.getDistrictId()))
                .collect(Collectors.toList());
        analysisSellingRepository.delete(deleteSellingList);
    }
}
