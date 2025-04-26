package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SubjectMapper;
import com.example.entity.Subject;
import com.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("subjectService")
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectMapper subjectDao;

    @Override
    public Map<String, Object> queryPage(Subject subjectVo) {
        Integer total = subjectDao.queryCount(subjectVo);
        List<HashMap> data = subjectDao.queryData(subjectVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<HashMap> queryList(Subject subjectVo) {
        List<HashMap> data = subjectDao.queryList(subjectVo);
        return data;
    }

    @Override
    public Map<String, Object> findPage(Subject subjectVo) {
        Integer total = subjectDao.findPageCount(subjectVo);
        List<HashMap> data = subjectDao.findPageData(subjectVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}