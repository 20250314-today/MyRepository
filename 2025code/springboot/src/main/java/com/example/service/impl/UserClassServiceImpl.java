package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.UserClass;
import com.example.mapper.UserClassMapper;
import com.example.service.UserClassService;
import com.example.vo.UserClassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userClassService")
public class UserClassServiceImpl extends ServiceImpl<UserClassMapper, UserClass> implements UserClassService {

    @Autowired
    private UserClassMapper userClassDao;

    @Override
    public Map<String, Object> queryPage(UserClassVo userClassVo) {
        Integer total = userClassDao.queryCount(userClassVo);
        List<HashMap> data = userClassDao.queryData(userClassVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }

    @Override
    public List<UserClass> selectByClassId(Integer classId) {
        List<UserClass> lists = userClassDao.selectByClassId(classId);
        return lists;
    }

    @Override
    public List<HashMap> selectList(UserClassVo userClassVo) {
        List<HashMap> lists = userClassDao.selectList(userClassVo);
        return lists;
    }
}