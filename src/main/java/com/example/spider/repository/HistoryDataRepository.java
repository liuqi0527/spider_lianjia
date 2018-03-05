package com.example.spider.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.spider.domain.HistoryData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-02-26]
 */
@Repository
public interface HistoryDataRepository extends JpaRepository<HistoryData, String> {

    @Override
    Page<HistoryData> findAll(Pageable pageable);

    @Override
    List<HistoryData> findAll();

    @Override
    HistoryData findOne(String id);

    List<HistoryData> findByCommunityId(String id);

    List<HistoryData> findByDistrictId(String id);

    List<HistoryData> findByTotalPriceContains(String key);

    @Query("select h from HistoryData h where (updateTime is null or updateTime <= ?1) and totalPrice <> '暂无价格'")
    List<HistoryData> findNeedUpdateDatas(LocalDateTime time);

    @Override
    <S extends HistoryData> S save(S s);
}
