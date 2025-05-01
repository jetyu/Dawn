package com.dawn.modules.ocr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawn.modules.ocr.entity.OcrResult;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {
}
