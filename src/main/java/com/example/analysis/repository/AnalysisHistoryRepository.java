package com.example.analysis.repository;

import java.util.List;

import com.example.analysis.Domain.AnalysisHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Repository
public interface AnalysisHistoryRepository extends JpaRepository<AnalysisHistory, AnalysisHistory.Key> {

    @Override
    List<AnalysisHistory> findAll();

    @Override
    void deleteAll();

    @Override
    void deleteAllInBatch();

    @Override
    <S extends AnalysisHistory> List<S> save(Iterable<S> entities);
}
