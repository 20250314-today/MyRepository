package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Exercises;
import com.example.vo.ExercisesVo;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface ExercisesService extends IService<Exercises> {


    Map<String, Object> queryPage(ExercisesVo exercises);

    Map<String, Object> findNotDoExercises(ExercisesVo exercises);

}

