package com.example.spider;

import java.io.File;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author LiuQi - [Created on 2018-02-24]
 */
public class TestMain {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        File file = new File(TestMain.class.getResource("/secondList.html").toURI());
        Document document = Jsoup.parse(file, "utf-8");

        Elements elements = document.select("ul.sellListContent").select("div.info");
        elements.forEach(element -> {
            Element titleElement = element.select("div.title>a").first();

            System.out.println(titleElement.text());

            String link = titleElement.attr("href");
            System.out.println(link);

            String id = StringUtils.substringBefore(StringUtils.substringAfterLast(link, "/"), ".");
            System.out.println(id);


            String totalPrice = element.select(".totalPrice").first().text();
            System.out.println(totalPrice);

            String unitPrice = element.select(".unitPrice").first().text();
            unitPrice = StringUtils.substringAfter(unitPrice, "单价");
            System.out.println(unitPrice);

            String[] infos = StringUtils.split(element.select(".houseInfo").text(), "/");
            String community = infos[0];
            String layoutType = infos[1];
            String sqart = infos[2];
            String direct = infos[3];
            String decort = infos[4];
            String lift = infos.length >= 6 ? infos[5] : "无电梯";
            System.out.println(sqart);

            String positionInfo = element.select("div.positionInfo").text();
            String split = StringUtils.contains(positionInfo, "/") ? "/" : " ";
            String[] positionInfos = StringUtils.split(positionInfo, split);
            String floor = positionInfos[0];
            String buildYears = positionInfos[1];
            System.out.println(floor);

            String followInfo = element.select(".followInfo").first().text();
            String[] followInfos = StringUtils.split(followInfo, "/");
            String focus = followInfos[0];
            String daikan = followInfos[1];

            String timeInfo = element.select(".timeInfo").text();

            String subWay = element.select(".subway").text();
            System.out.println(subWay);

            String textFree = element.select("taxfree").text();
            String five = element.select("five").text();
            System.out.println(StringUtils.join(textFree, five));


            System.out.println("--------------");
        });

    }

}
