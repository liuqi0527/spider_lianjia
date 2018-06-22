package com.example.spider.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.spider.domain.AnalysisSecondHandSellingData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Repository
@Transactional
public interface AnalysisSecondHandSellingRepository extends JpaRepository<AnalysisSecondHandSellingData, AnalysisSecondHandSellingData.Key> {

    void deleteAllByDate(String date);

    @Override
    <S extends AnalysisSecondHandSellingData> List<S> save(Iterable<S> entities);
}
