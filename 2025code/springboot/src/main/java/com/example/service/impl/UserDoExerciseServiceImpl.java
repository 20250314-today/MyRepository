package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.UserDoExercise;
import com.example.mapper.UserDoExerciseMapper;
import com.example.service.UserDoExerciseService;
import com.example.vo.UserDoExerciseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userDoExerciseService")
public class UserDoExerciseServiceImpl extends ServiceImpl<UserDoExerciseMapper, UserDoExercise> implements UserDoExerciseService {

    @Autowired
    private UserDoExerciseMapper userDoExerciseDao;

    @Override
    public Map<String, Object> queryPage(UserDoExerciseVo userDoExerciseVo) {
        Integer total = userDoExerciseDao.queryCount(userDoExerciseVo);
        List<HashMap> data = userDoExerciseDao.queryData(userDoExerciseVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}