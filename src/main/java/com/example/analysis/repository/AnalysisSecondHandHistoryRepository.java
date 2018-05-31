package com.example.analysis.repository;

import java.util.List;

import com.example.analysis.Domain.AnalysisSecondHandHistoryData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Repository
public interface AnalysisSecondHandHistoryRepository extends JpaRepository<AnalysisSecondHandHistoryData, AnalysisSecondHandHistoryData.Key> {

    @Override
    List<AnalysisSecondHandHistoryData> findAll();

    @Override
    void deleteAll();

    @Override
    void deleteAllInBatch();

    @Override
    <S extends AnalysisSecondHandHistoryData> List<S> save(Iterable<S> entities);
}
