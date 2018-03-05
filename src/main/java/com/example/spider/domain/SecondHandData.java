package com.example.spider.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 在售二手房数据
 *
 * @author LiuQi - [Created on 2018-02-26]
 */
@Getter
@Setter
@Entity
@Table(name = "second_hand_data_03_02")
public class SecondHandData {

    @Id
    private String id;
    private long communityId;
    private long districtId;
    private String title;
    private String link;

    private String totalPrice;
    private String unitPrice;

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
    private String limitYear;//产权年限*

    private String transactType;//商品房*
    private String ownType;//共有产权、非公有产权*
    private String houseType;//商业办公类 & 普通住宅*
    private String taxInfo;
    private String locationDetail;


    private String hangTime;
    private String updatePriceCount;
    private String daikanCount;
    private String forcusCount;
    private String browseCount;

    private LocalDateTime updateTime;
}

