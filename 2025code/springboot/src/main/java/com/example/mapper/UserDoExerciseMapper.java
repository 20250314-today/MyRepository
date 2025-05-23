package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserDoExercise;
import com.example.vo.UserDoExerciseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@Mapper
public interface UserDoExerciseMapper extends BaseMapper<UserDoExercise> {

    Integer queryCount(UserDoExerciseVo userDoExerciseVo);

    List<HashMap> queryData(UserDoExerciseVo userDoExerciseVo);
}
