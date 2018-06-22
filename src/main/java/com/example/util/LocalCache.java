package com.example.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.example.spider.domain.CommunityData;
import com.example.spider.domain.DistrictData;
import com.example.spider.repository.DistrictRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi - [Created on 2018-06-01]
 */
@Component
public class LocalCache {

    private Map<Long, DistrictData> districtMap = new HashMap<>();

    @Autowired
    private DistrictRepository districtRepository;

    @PostConstruct
    private void init() {
        List<DistrictData> list = districtRepository.findAll();
        for (DistrictData districtData : list) {
            districtMap.put(districtData.getId(), districtData);
        }
    }

    public boolean isInterest(CommunityData communityData) {
        return isInterest(communityData.getDistrictId());
    }

    public boolean isInterest(long districtId) {
        DistrictData districtData = districtMap.get(districtId);
        return districtData != null && districtData.isInterest();
    }

}
