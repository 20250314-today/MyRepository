package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.HomeworkMapper;
import com.example.entity.Homework;
import com.example.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("homeworkService")
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkDao;

    @Override
    public Map<String, Object> queryPage(Homework homeworkVo) {
        Integer total = homeworkDao.queryCount(homeworkVo);
        List<HashMap> data = homeworkDao.queryData(homeworkVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public Map<String, Object> findNotDoHomework(Homework homeworkVo) {
        Integer total = homeworkDao.findNotDoHomeworkCount(homeworkVo);
        List<HashMap> data = homeworkDao.findNotDoHomeworkData(homeworkVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}