package com.example.spider.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 历史成交数据
 *
 * @author LiuQi - [Created on 2018-02-26]
 */
@Getter
@Setter
@Entity
@Table(name = "second_hand_history_data")
public class SecondHandHistoryData {

    @Id
    private String id;
    private long communityId;
    private long districtId;
    private String title;
    private String link;

    private String dealDate;
    private String totalPrice;
    private String unitPrice;
    private String dealRecords;

    private String square;
    private String layoutType;
    private String floor;
    private String direct;
    private String hasLift;
    private String buildYear;
    private String decoration;
    private String heatingType;
    private String buildingType;
    private String buildingStruct;
    private String limitYear;//产权年限

    private String transactType;//商品房
    private String ownType;//共有产权、非公有产权
    private String houseType;//商品房
    private String taxInfo;


    private String hangPrice;
    private String hangTime;
    private String hangDuration;
    private String updatePriceCount;
    private String daikanCount;
    private String forcusCount;
    private String browseCount;

    private LocalDateTime updateTime;
}

