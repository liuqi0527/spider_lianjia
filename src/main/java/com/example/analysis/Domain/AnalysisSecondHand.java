package com.example.analysis.Domain;

import java.io.Serializable;
import java.time.LocalDate;

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
@IdClass(AnalysisSecondHand.Key.class)
@Table(name = "analysis_second_hand")
public class AnalysisSecondHand {

    @Id
    private long districtId;
    @Id
    private LocalDate updateDate;
    private long saleAmount;
    private long avgPrice;

    public AnalysisSecondHand() {
    }

    public AnalysisSecondHand(long districtId, LocalDate updateDate) {
        this.districtId = districtId;
        this.updateDate = updateDate;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Key implements Serializable {

        private long districtId;
        private LocalDate updateDate;

        public Key() {
        }

        public Key(long districtId, LocalDate updateDate) {
            this.districtId = districtId;
            this.updateDate = updateDate;
        }
    }
}
