package com.example.entity;

import lombok.Data;

@Data
public class QuestionOption {
    private Integer id;
    private Integer questionId;
    private String content;
    private Boolean isCorrect;
}