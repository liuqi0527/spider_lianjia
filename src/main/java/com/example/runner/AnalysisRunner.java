package com.example.runner;

import com.example.analysis.task.AnalysisHistoryTask;
import com.example.analysis.task.AnalysisSecondHandTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Component
public class AnalysisRunner implements CommandLineRunner {

    @Autowired
    private AnalysisHistoryTask analysisHistoryTask;

    @Autowired
    private AnalysisSecondHandTask analysisSecondHandTask;

    @Override
    public void run(String... args) throws Exception {
//        analysisHistoryTask.run();
        analysisSecondHandTask.run();
    }
}
