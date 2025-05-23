package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Exercises;
import com.example.vo.ExercisesVo;
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

    Integer queryCount(ExercisesVo exercisesVo);

    List<HashMap> queryData(ExercisesVo exercisesVo);

    Integer findNotDoExercisesCount(ExercisesVo exercisesVo);

    List<HashMap> findNotDoExercisesData(ExercisesVo exercisesVo);
}
