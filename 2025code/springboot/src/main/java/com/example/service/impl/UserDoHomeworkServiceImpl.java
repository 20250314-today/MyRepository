package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.UserDoHomework;
import com.example.mapper.UserDoHomeworkMapper;
import com.example.service.UserDoHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userDoHomeworkService")
public class UserDoHomeworkServiceImpl extends ServiceImpl<UserDoHomeworkMapper, UserDoHomework> implements UserDoHomeworkService {

    @Autowired
    private UserDoHomeworkMapper userDoHomeworkDao;

    @Override
    public Map<String, Object> queryPage(UserDoHomework userDoHomeworkVo) {
        Integer total = userDoHomeworkDao.queryCount(userDoHomeworkVo);
        List<HashMap> data = userDoHomeworkDao.queryData(userDoHomeworkVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }



    @Override
    public Boolean updateModeByUserId(String userId, String homeworkId,String score,String remark) {
        return userDoHomeworkDao.updateModeByUserId(userId,homeworkId,score,remark);
    }
}