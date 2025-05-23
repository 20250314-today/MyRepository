package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserDoExercise;
import com.example.vo.UserDoExerciseVo;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface UserDoExerciseService extends IService<UserDoExercise> {


    Map<String, Object> queryPage(UserDoExerciseVo userDoExerciseVo);
}

