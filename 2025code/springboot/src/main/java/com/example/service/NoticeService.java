package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
   @Resource
   NoticeMapper noticeMapper;
    public String notice(String name){
        if("notice".equals(name)){
            return "notice";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Notice> selectAll(){
        return noticeMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        PageHelper.startPage(pageNum,pageSize);
        List<Notice> List = noticeMapper.selectAll(notice);
        return PageInfo.of(List);
    }

    public void add(Notice notice) {
       Account currentUser =  TokenUtils.getCurrentUser();
       if ("USER".equals(currentUser.getRole())){
           throw new CustomerException("500","您无权限操作");
       }
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void update(Notice notice) {
        noticeMapper.updateById(notice);
    }
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

//    public void deleteBatch(List<Notice> list) {
//        for (Notice notice : list) {
//            this.deleteById(notice.getId());
//        }
//    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

//    public Notice selectById(String id){
//        return noticeMapper.selectById(id);
//    }

}
