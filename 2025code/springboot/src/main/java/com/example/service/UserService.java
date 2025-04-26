package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
   @Resource
   UserMapper userMapper;
    public String user(String name){
        if("user".equals(name)){
            return "user";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<User> selectAll(){
        return userMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, User user) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> List = userMapper.selectAll(user);
        return PageInfo.of(List);
    }

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser !=null){
            throw new CustomerException("账号已存在！");
        }
//        默认密码
        if (StrUtil.isBlank(user.getPassword())){
            user.setPassword("abc123");
        }
        if (StrUtil.isBlank(user.getName())){
            user.setName(user.getUsername());
        }

        user.setRole("USER");
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteById(user.getId());
        }
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public User login(Account account) {
      User dbUser=  userMapper.selectByUsername(account.getUsername());
      if (dbUser==null)
      {
          throw new CustomerException("账号不存在");
      }
        if (!dbUser.getPassword().equals(account.getPassword()))
        {
            throw new CustomerException("账号或密码错误");
        }
        String token= TokenUtils.createToken(dbUser.getId() + "-" +"USER",dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public void register(User user) {
        this.add(user);
    }
    public User selectById(String id){
        return userMapper.selectById(id);
    }

    public void updatePassword(Account account) {
        if (!account.getNewPassword().equals(account.getNewPasswords())){
            throw new CustomerException("500","两次输入密码不一致！");
        }
        Account currentUser=TokenUtils.getCurrentUser();
        if(!account.getPassword().equals(currentUser.getPassword())){
            throw new CustomerException("500","原密码输入错误！");
        }
        User user = userMapper.selectById(currentUser.getId().toString());
        user.setPassword(account.getNewPassword());
        userMapper.updateById(user);
    }
}
