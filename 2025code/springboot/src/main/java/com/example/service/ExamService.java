package com.example.service;

import com.example.entity.Exam;
import com.example.entity.ExamQuestion;
import com.example.mapper.ExamMapper;
import com.example.mapper.ExamQuestionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamQuestionMapper questionMapper;

    public Exam createExam(Exam examDTO) {
        // 保存试卷
        Exam exam = new Exam();
        BeanUtils.copyProperties(examDTO, exam);
        examMapper.insert(exam);
        return exam;
    }


}