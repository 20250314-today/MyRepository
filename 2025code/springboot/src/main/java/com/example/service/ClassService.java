package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.*;
import com.example.entity.Classs;
import com.example.exception.CustomerException;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    @Resource
    TeacherMapper teacherMapper;
    @Resource
    ClassMapper classMapper;
    @Resource
    UserMapper userMapper;
    public String introduction(String name){
        if("introduction".equals(name)){
            return "introduction";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Classs> selectAll(Classs clas){
        return classMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Classs clas) {
        PageHelper.startPage(pageNum,pageSize);
        List<Classs> list = classMapper.selectAll(clas);
//        筛选title
        for (Classs dbIntroduction : list) {
            Integer userId = dbIntroduction.getUserId();
            Teacher teacher =  teacherMapper.selectById(String.valueOf(userId));
            if(ObjectUtil.isNotEmpty(teacher)){
                dbIntroduction.setUserName(teacher.getName());
            }
        }
        return PageInfo.of(list);
    }

    public void add(Classs clas) {
        clas.setTime(DateUtil.now());
        classMapper.insert(clas);
    }

    public void update(Classs clas) {
        classMapper.updateById(clas);
    }
    public void deleteById(Integer id) {
        classMapper.deleteById(id);
    }

}