package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
   AdminMapper adminMapper;
    public String admin(String name){
        if("admin".equals(name)){
            return "admin";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Admin> selectAll(){
        return adminMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> List = adminMapper.selectAll(admin);
        return PageInfo.of(List);
    }

    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin !=null){
            throw new CustomerException("账号已存在！");
        }
//        默认密码
        if (StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("abc123");
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            this.deleteById(admin.getId());
        }
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin login(Account account) {
      Admin dbAdmin=  adminMapper.selectByUsername(account.getUsername());
      if (dbAdmin==null)
      {
          throw new CustomerException("账号不存在");
      }
        if (!dbAdmin.getPassword().equals(account.getPassword()))
        {
            throw new CustomerException("账号或密码错误");
        }
        String token=TokenUtils.createToken(dbAdmin.getId() + "-" +"ADMIN",dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }
    public Admin selectById(String id){
        return adminMapper.selectById(id);
    }

    public void updatePassword(Account account) {
        if (!account.getNewPassword().equals(account.getNewPasswords())){
            throw new CustomerException("500","两次输入密码不一致！");
        }
        Account currentUser=TokenUtils.getCurrentUser();
        if(!account.getPassword().equals(currentUser.getPassword())){
            throw new CustomerException("500","原密码输入错误！");
        }
       Admin admin = adminMapper.selectById(currentUser.getId().toString());
        admin.setPassword(account.getNewPassword());
        adminMapper.updateById(admin);
    }
}
