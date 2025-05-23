package com.example.controller;

import com.example.common.Result;

import com.example.entity.ExamQuestion;
import com.example.entity.StudentExamAnswer;
import com.example.service.ExamQuestionService;
import com.example.service.ExamService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExamController {

    @Autowired
    private ExamService examService;
    @Resource
    private ExamQuestionService examQuestionService;

    // 教师创建试卷
    @PostMapping("/teachersubmit")
    public Result createExam(@RequestBody ExamQuestion examDTO) {
        ExamQuestion exam = examQuestionService.createExam(examDTO);
        return Result.success(exam);
    }

    // 学生获取试卷
    @GetMapping("/exams/{examId}")
    public Result getExam(@PathVariable Integer examId) {
        ExamQuestion exam = examQuestionService.getExam(examId);
        return Result.success(exam);
    }

    // 提交答案
    @PostMapping("/submit")
    public Result submitAnswers(@RequestBody StudentExamAnswer submitDTO) {
        examQuestionService.submitAnswers(submitDTO);
        return Result.success();
    }
    @PostMapping("/examupdate")
    public Result answersUpdate(@RequestBody StudentExamAnswer submitDTO){//RequestBody 接受前端传来的json对象
        examQuestionService.answersUpdate(submitDTO);
        return Result.success();
    }
}