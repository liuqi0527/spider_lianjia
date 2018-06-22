package com.example.spider.domain;

import java.io.Serializable;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuQi - [Created on 2018-03-02]
 */
@Setter
@Getter
@Entity
@IdClass(AnalysisSecondHandSellingData.Key.class)
@Table(name = "analysis_second_hand_selling")
public class AnalysisSecondHandSellingData {

    @Id
    private long districtId;
    @Id
    private String date;

    private long avgPrice;

    private long saleAmount;//在售数量
    private long dealAmount;//成交数量
    private long absoluteIncrAmount;//相比上个月的绝对增量
    private long newSaleAmount;//新挂牌量 = 下个月挂牌总量 + 本月成交量 - 本月挂牌总量

    public AnalysisSecondHandSellingData() {
    }

    public AnalysisSecondHandSellingData(long districtId, String date) {
        this.districtId = districtId;
        this.date = date;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Key implements Serializable {

        private long districtId;
        private String date;

        public Key() {
        }

        public Key(long districtId, String date) {
            this.districtId = districtId;
            this.date = date;
        }
    }
}
