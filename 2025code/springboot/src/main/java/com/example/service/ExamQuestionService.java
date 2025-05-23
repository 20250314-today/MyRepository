package com.example.service;

import com.example.entity.Book;
import com.example.entity.Exam;
import com.example.entity.ExamQuestion;
import com.example.entity.StudentExamAnswer;
import com.example.mapper.ExamMapper;
import com.example.mapper.ExamQuestionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExamQuestionService {

    @Autowired
    private ExamQuestionMapper questionMapper;

    public  ExamQuestion createExam( ExamQuestion examDTO) {
        // 保存试卷
        ExamQuestion exam = new  ExamQuestion();
        BeanUtils.copyProperties(examDTO, exam);
        questionMapper.insert(exam);
        return exam;
    }
    public ExamQuestion getExam(Integer examId) {
        return questionMapper.getExam(examId);
    }
    public void submitAnswers(StudentExamAnswer book) {
        // Account currentUser =  TokenUtils.getCurrentUser();
        questionMapper.submitAnswers(book);
    }
    public void answersUpdate(StudentExamAnswer book) {
        questionMapper.answersUpdate(book);
    }

}