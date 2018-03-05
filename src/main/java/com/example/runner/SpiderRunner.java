package com.example.runner;

import com.example.spider.task.HistoryDetailTask;
import com.example.spider.task.HistoryListTask;
import com.example.spider.task.NewHouseTask;
import com.example.spider.task.SecondHandTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Component
public class SpiderRunner implements CommandLineRunner {

    @Autowired
    private HistoryDetailTask historyDetailTask;

    @Autowired
    private HistoryListTask historyListTask;

    @Autowired
    private SecondHandTask secondHandTask;

    @Autowired
    private NewHouseTask newHouseTask;

    @Override
    public void run(String... args) throws Exception {
//        historyDetailTask.run();
//        secondHandTask.run();
    }
}
