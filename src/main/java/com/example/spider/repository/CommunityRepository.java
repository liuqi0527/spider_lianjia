package com.example.spider.repository;

import java.util.List;

import com.example.spider.domain.CommunityData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Repository
public interface CommunityRepository extends JpaRepository<CommunityData, Long> {


    @Override
    CommunityData findOne(Long id);

    List<CommunityData> findByDistrictId(long id);

    @Override
    <S extends CommunityData> S save(S s);

//    void saveFetchHistoryTime(CommunityData data);
}
