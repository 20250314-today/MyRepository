package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.AskQuestions;
import com.example.vo.AskQuestionsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:21
 */
@Mapper
public interface AskQuestionsMapper extends BaseMapper<AskQuestions> {

    Integer queryCount(AskQuestionsVo askQuestionsVo);

    List<HashMap> queryData(AskQuestionsVo askQuestionsVo);

}
