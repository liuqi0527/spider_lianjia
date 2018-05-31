package com.example.spider.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.spider.domain.SecondHandSellingData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Repository
public interface SecondHandSellingRepository extends JpaRepository<SecondHandSellingData, String> {


    @Override
    SecondHandSellingData findOne(String id);

    List<SecondHandSellingData> findByCommunityId(String id);

    List<SecondHandSellingData> findByDistrictId(String id);

    List<SecondHandSellingData> findByTotalPriceContains(String key);

    @Query("select h from SecondHandSellingData h where (updateTime is null or updateTime <= ?1) and totalPrice <> '暂无价格'")
    List<SecondHandSellingData> findNeedUpdateDatas(LocalDateTime time);

    @Override
    <S extends SecondHandSellingData> S save(S s);
}
