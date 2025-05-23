package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Teacher;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.TeacherMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
   @Resource
   TeacherMapper teacherMapper;
    public String teacher(String name){
        if("teacher".equals(name)){
            return "teacher";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Teacher> selectAll(){
        return teacherMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Teacher teacher) {
        PageHelper.startPage(pageNum,pageSize);
        List<Teacher> List = teacherMapper.selectAll(teacher);
        return PageInfo.of(List);
    }

    public void add(Teacher teacher) {
        Teacher dbUser = teacherMapper.selectByUsername(teacher.getUsername());
        if (dbUser !=null){
            throw new CustomerException("账号已存在！");
        }
//        默认密码
        if (StrUtil.isBlank(teacher.getPassword())){
            teacher.setPassword("abc123");
        }
        if (StrUtil.isBlank(teacher.getName())){
            teacher.setName(teacher.getUsername());
        }

        teacher.setRole("TEACHER");
        teacherMapper.insert(teacher);
    }

    public void update(Teacher user) {
        teacherMapper.updateById(user);
    }
    public void deleteById(Integer id) {
        teacherMapper.deleteById(id);
    }

    public void deleteBatch(List<Teacher> list) {
        for (Teacher user : list) {
            this.deleteById(Integer.valueOf(user.getId()));
        }
    }

    public List<Teacher> selectAll(Teacher user) {
        return teacherMapper.selectAll(user);
    }

    public Teacher login(Account account) {
      Teacher dbUser=  teacherMapper.selectByUsername(account.getUsername());
      if (dbUser==null)
      {
          throw new CustomerException("账号不存在");
      }
        if (!dbUser.getPassword().equals(account.getPassword()))
        {
            throw new CustomerException("账号或密码错误");
        }
        String token= TokenUtils.createToken(dbUser.getId() + "-" +"TEACHER",dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public void register(Teacher user) {
        this.add(user);
    }
    public Teacher selectById(String id){
        return teacherMapper.selectById(id);
    }

    public void updatePassword(Account account) {
        if (!account.getNewPassword().equals(account.getNewPasswords())){
            throw new CustomerException("500","两次输入密码不一致！");
        }
        Account currentUser=TokenUtils.getCurrentUser();
        if(!account.getPassword().equals(currentUser.getPassword())){
            throw new CustomerException("500","原密码输入错误！");
        }
        Teacher user = teacherMapper.selectById(String.valueOf(currentUser.getId()));
        user.setPassword(account.getNewPassword());
        teacherMapper.updateById(user);
    }


}
