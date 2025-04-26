package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.exception.CustomerException;
import com.example.mapper.ApplyMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {
   @Resource
   ApplyMapper applyMapper;
    public String apply(String name){
        if("apply".equals(name)){
            return "apply";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Apply> selectAll(){
        return applyMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            apply.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Apply> List = applyMapper.selectAll(apply);
        return PageInfo.of(List);

    }

    public void add(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setStatus("待审核");
        apply.setTime(DateUtil.now());
        apply.setUserId(currentUser.getId());
        applyMapper.insert(apply);
    }

    public void update(Apply apply) {
        applyMapper.updateById(apply);
    }
    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }

//    public void deleteBatch(List<Apply> list) {
//        for (Apply apply : list) {
//            this.deleteById(apply.getId());
//        }
//    }

    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

//    public Apply selectById(String id){
//        return applyMapper.selectById(id);
//    }

}
