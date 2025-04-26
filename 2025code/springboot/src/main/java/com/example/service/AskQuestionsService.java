package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.AskQuestions;
import com.example.vo.AskQuestionsVo;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:21
 */
public interface AskQuestionsService extends IService<AskQuestions> {

    Map<String, Object> queryPage(AskQuestionsVo askQuestionsVo);
}

