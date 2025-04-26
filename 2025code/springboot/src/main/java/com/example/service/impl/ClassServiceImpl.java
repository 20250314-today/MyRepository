package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ClassMapper;
import com.example.entity.Class;
import com.example.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("classService")
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    private ClassMapper classDao;

    @Override
    public Map<String, Object> queryPage(Class classVo) {
        Integer total = classDao.queryCount(classVo);
        List<HashMap> data = classDao.queryData(classVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<HashMap> findList(Class classVo) {
        return classDao.findList(classVo);
    }
}