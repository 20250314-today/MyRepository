package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Exercises;
import com.example.mapper.ExercisesMapper;
import com.example.service.ExercisesService;
import com.example.vo.ExercisesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("exercisesService")
public class ExercisesServiceImpl extends ServiceImpl<ExercisesMapper, Exercises> implements ExercisesService {

    @Autowired
    private ExercisesMapper exercisesDao;

    @Override
    public Map<String, Object> queryPage(ExercisesVo exercisesVo) {
        Integer total = exercisesDao.queryCount(exercisesVo);
        List<HashMap> data = exercisesDao.queryData(exercisesVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public Map<String, Object> findNotDoExercises(ExercisesVo exercisesVo) {
        Integer total = exercisesDao.findNotDoExercisesCount(exercisesVo);
        List<HashMap> data = exercisesDao.findNotDoExercisesData(exercisesVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}