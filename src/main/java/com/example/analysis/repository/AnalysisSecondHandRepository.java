package com.example.analysis.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.analysis.Domain.AnalysisHistory;
import com.example.analysis.Domain.AnalysisSecondHand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Repository
public interface AnalysisSecondHandRepository extends JpaRepository<AnalysisSecondHand, AnalysisSecondHand.Key> {

    @Override
    List<AnalysisSecondHand> findAll();

    void deleteAllByUpdateDate(LocalDate localDate);

    @Override
    <S extends AnalysisSecondHand> List<S> save(Iterable<S> entities);
}
