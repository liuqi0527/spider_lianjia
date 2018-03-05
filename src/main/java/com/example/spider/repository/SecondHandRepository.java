package com.example.spider.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.spider.domain.SecondHandData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Repository
public interface SecondHandRepository extends JpaRepository<SecondHandData, String> {


    @Override
    SecondHandData findOne(String id);

    List<SecondHandData> findByCommunityId(String id);

    List<SecondHandData> findByDistrictId(String id);

    List<SecondHandData> findByTotalPriceContains(String key);

    @Query("select h from SecondHandData h where (updateTime is null or updateTime <= ?1) and totalPrice <> '暂无价格'")
    List<SecondHandData> findNeedUpdateDatas(LocalDateTime time);

    @Override
    <S extends SecondHandData> S save(S s);
}
