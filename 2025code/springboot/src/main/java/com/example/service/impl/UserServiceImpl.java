package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.User;
import com.example.mapper.UsersMapper;
import com.example.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("usersService")
public class UserServiceImpl extends ServiceImpl<UsersMapper, User> implements UsersService {

    @Autowired
    private UsersMapper userDao;

    @Override
    public Map<String, Object> queryPage(User userVo) {
        Integer total = userDao.queryCount(userVo);
        List<HashMap> data = userDao.queryData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }



    @Override
    public Map<String, Object> findNotDoWork(User userVo) {
        Integer total = userDao.findNotDoWorkCount(userVo);
        List<HashMap> data = userDao.findNotDoWorkData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public Map<String, Object> findNotDoHomework(User userVo) {
        Integer total = userDao.findNotDoHomeworkCount(userVo);
        List<HashMap> data = userDao.findNotDoHomeworkData(userVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<HashMap> findList(User userVo) {
        List<HashMap> data = userDao.findList(userVo);
        return data;
    }

}