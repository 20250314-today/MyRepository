package com.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.UserHomework;
import com.example.mapper.UserHomeworkMapper;
import com.example.service.UserHomeworkService;
import com.example.vo.UserHomeworkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserHomeworkServiceImpl extends ServiceImpl<UserHomeworkMapper, UserHomework> implements UserHomeworkService {

    @Autowired
    private UserHomeworkMapper userDao;

    @Override
    public Map<String, Object> queryPage(UserHomeworkVo userVo) {
        Integer total = userDao.queryCount(userVo);
        List<HashMap> data = userDao.queryData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }


    @Override
    public Map<String, Object> findNotDoWork(UserHomeworkVo userVo) {
        Integer total = userDao.findNotDoWorkCount(userVo);
        List<HashMap> data = userDao.findNotDoWorkData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public Map<String, Object> findNotDoHomework(UserHomeworkVo userVo) {
        Integer total = userDao.findNotDoHomeworkCount(userVo);
        List<HashMap> data = userDao.findNotDoHomeworkData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<HashMap> findList(UserHomeworkVo userVo) {
        List<HashMap> data = userDao.findList(userVo);
        return data;
    }
}