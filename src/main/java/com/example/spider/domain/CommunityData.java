package com.example.spider.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Getter
@Setter
@Entity
@Table(name = "community")
public class CommunityData {

    @Id
    private long id;

    private long districtId;
    private String name;

    private LocalDateTime fetchHistoryTime;
    private LocalDateTime fetchSecondHandTime;
}
