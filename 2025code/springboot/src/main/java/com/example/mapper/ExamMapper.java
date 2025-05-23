package com.example.mapper;

import com.example.entity.ExamQuestion;
import org.apache.ibatis.annotations.Mapper;
import com.example.entity.Exam;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExamMapper {
    void insert(Exam exam);

}