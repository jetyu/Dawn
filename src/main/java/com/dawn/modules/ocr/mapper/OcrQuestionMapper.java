package com.dawn.modules.ocr.mapper;

import com.dawn.modules.ocr.entity.OcrQuestionEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OcrQuestionMapper {
    int insertBatch(@Param("list") List<OcrQuestionEntity> list);
}
