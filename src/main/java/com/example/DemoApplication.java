package com.example;

import com.example.analysis.task.AnalysisSecondHandHistoryTask;
import com.example.analysis.task.AnalysisSecondHandSellingTask;
import com.example.spider.repository.SecondHandHistoryDataRepository;
import com.example.spider.task.SecondHandHistoryDetailTask;
import com.example.spider.task.SecondHandHistoryListTask;
import com.example.spider.task.NewHouseTask;
import com.example.spider.task.SecondHandSellingTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner  {

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

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		fetchAndAnalysisAll();
	}

	private void fetchAndAnalysisAll() {
		DebugLogger.info("开始拉取新成交数据...");
		historyListTask.run();

		DebugLogger.info("开始拉取成交价格数据");
		historyDetailTask.run();
		DebugLogger.info("历史成交数据全部更新完成");

		DebugLogger.info("开始分析历史成交数据");
		analysisHistoryTask.run();
		DebugLogger.info("历史成交数据分析完成");

		//在售二手房数据需要创建新表，否则会覆盖以前数据
		DebugLogger.info("开始拉取挂牌二手房数据");
		secondHandTask.run();
		DebugLogger.info("挂牌二手房数据拉取完成");

		DebugLogger.info("开始分析挂牌二手房数据");
		analysisSecondHandTask.run();
		DebugLogger.info("挂牌二手房数据分析完成");
	}
}
