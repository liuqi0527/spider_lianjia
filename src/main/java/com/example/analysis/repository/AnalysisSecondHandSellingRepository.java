package com.example.analysis.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.analysis.Domain.AnalysisSecondHandSellingData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
@Repository
public interface AnalysisSecondHandSellingRepository extends JpaRepository<AnalysisSecondHandSellingData, AnalysisSecondHandSellingData.Key> {

    @Override
    List<AnalysisSecondHandSellingData> findAll();

    void deleteAllByUpdateDate(LocalDate localDate);

    @Override
    <S extends AnalysisSecondHandSellingData> List<S> save(Iterable<S> entities);
}
