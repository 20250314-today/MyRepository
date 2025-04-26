package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntroductionService {
    @Resource
    CategoryMapper categoryMapper;
   @Resource
   IntroductionMapper introductionMapper;
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
    public List<Introduction> selectAll(){
        return introductionMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            introduction.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Introduction> list = introductionMapper.selectAll(introduction);
//        筛选title
        for (Introduction dbIntroduction : list) {
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            Category category =  categoryMapper.selectById(categoryId);
            User user = userMapper.selectById(userId.toString());
            if(ObjectUtil.isNotEmpty(category)) {
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if(ObjectUtil.isNotEmpty(user)){
                dbIntroduction.setUserName(user.getName());
            }
        }
        return PageInfo.of(list);
    }

    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());
        introduction.setTime(DateUtil.now());
        introductionMapper.insert(introduction);
    }

    public void update(Introduction introduction) {
        introductionMapper.updateById(introduction);
    }
    public void deleteById(Integer id) {
        introductionMapper.deleteById(id);
    }



    public List<Introduction> selectAll(Introduction introduction) {
        List<Introduction> introductions = introductionMapper.selectAll(introduction);
        for (Introduction dbintroduction : introductions) {
            dbintroduction.setDescription(HtmlUtil.cleanHtmlTag(dbintroduction.getContent()));
        }
        return introductions;
    }
}
