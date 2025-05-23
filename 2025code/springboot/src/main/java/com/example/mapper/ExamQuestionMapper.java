package com.example.mapper;

import com.example.entity.ExamQuestion;
import com.example.entity.StudentExamAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExamQuestionMapper {
    void insert(ExamQuestion question);
    ExamQuestion getExam(Integer examId);

    void submitAnswers(StudentExamAnswer book);

    void answersUpdate(StudentExamAnswer book);
}