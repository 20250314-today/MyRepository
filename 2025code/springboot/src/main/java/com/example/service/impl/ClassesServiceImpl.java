package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Classes;
import com.example.mapper.ClassesMapper;
import com.example.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("classesService")
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements ClassesService {

    @Autowired
    private ClassesMapper classDao;

    @Override
    public Map<String, Object> queryPage(Classes classVo) {
        Integer total = classDao.queryCount(classVo);
        List<HashMap> data = classDao.queryData(classVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<HashMap> findList(Classes classVo) {
        return classDao.findList(classVo);
    }
}