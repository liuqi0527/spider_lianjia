package com.example.spider.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuQi - [Created on 2018-06-01]
 */
@Getter
@Setter
@Entity
@Table(name = "district")
public class DistrictData {

    @Id
    private long id;
    private long cityId;
    private String name;
    private String pinYin;
    private int bizCount;
    private boolean interest;
    
}
