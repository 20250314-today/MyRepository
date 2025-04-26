package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Exercises;
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
public interface ExercisesMapper extends BaseMapper<Exercises> {

    Integer queryCount(Exercises exercisesVo);

    List<HashMap> queryData(Exercises exercisesVo);

    Integer findNotDoExercisesCount(Exercises exercisesVo);

    List<HashMap> findNotDoExercisesData(Exercises exercisesVo);
}
