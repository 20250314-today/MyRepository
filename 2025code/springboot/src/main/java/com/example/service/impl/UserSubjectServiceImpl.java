package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserSubjectMapper;
import com.example.entity.UserSubject;
import com.example.service.UserSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userSubjectService")
public class UserSubjectServiceImpl extends ServiceImpl<UserSubjectMapper, UserSubject> implements UserSubjectService {

    @Autowired
    private UserSubjectMapper userSubjectDao;
    @Override
    public Map<String, Object> queryPage(UserSubject userSubjectVo) {
        Integer total = userSubjectDao.queryCount(userSubjectVo);
        List<HashMap> data = userSubjectDao.queryData(userSubjectVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}