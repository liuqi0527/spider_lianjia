package com.example.analysis.Domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "analysis_second_hand_history")
public class AnalysisSecondHandHistoryData {

    @EmbeddedId
    private Key key;
    private double avgPrice;
    private long dealAmount;

    @Transient
    private double totalPrice;
    @Transient
    private long totalPriceCount;

    public AnalysisSecondHandHistoryData() {
    }

    public AnalysisSecondHandHistoryData(Key key) {
        this.key = key;
    }

    public long getDistrictId() {
        return key.getDistrictId();
    }

    public LocalDate getDate() {
        return key.getDate();
    }


    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Key implements Serializable {
        private long districtId;
        private LocalDate date;

        public Key() {
        }

        public Key(long districtId, LocalDate date) {
            this.districtId = districtId;
            this.date = date;
        }
    }
}
