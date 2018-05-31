package com.example.spider.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.spider.domain.SecondHandHistoryData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Repository
public interface SecondHandHistoryDataRepository extends JpaRepository<SecondHandHistoryData, String> {

    @Override
    Page<SecondHandHistoryData> findAll(Pageable pageable);

    @Override
    List<SecondHandHistoryData> findAll();

    @Override
    SecondHandHistoryData findOne(String id);

    List<SecondHandHistoryData> findByCommunityId(String id);

    List<SecondHandHistoryData> findByDistrictId(String id);

    List<SecondHandHistoryData> findByTotalPriceContains(String key);

    @Query("select h.id from SecondHandHistoryData h")
    Set<String> findIdList();

    @Query("select h from SecondHandHistoryData h where (updateTime is null or updateTime <= ?1) and totalPrice <> '暂无价格'")
    List<SecondHandHistoryData> findNeedUpdateDatas(LocalDateTime time);

    @Override
    <S extends SecondHandHistoryData> S save(S s);
}
