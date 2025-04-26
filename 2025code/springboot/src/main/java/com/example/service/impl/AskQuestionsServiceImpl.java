package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.AskQuestions;
import com.example.mapper.AskQuestionsMapper;
import com.example.service.AskQuestionsService;
import com.example.vo.AskQuestionsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("askQuestionsService")
public class AskQuestionsServiceImpl extends ServiceImpl<AskQuestionsMapper, AskQuestions> implements AskQuestionsService {

    @Autowired
    private AskQuestionsMapper askQuestionsMapper;


    @Override
    public Map<String, Object> queryPage(AskQuestionsVo askQuestionsVo) {
        Integer total = askQuestionsMapper.queryCount(askQuestionsVo);
        List<HashMap> data = askQuestionsMapper.queryData(askQuestionsVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }


}