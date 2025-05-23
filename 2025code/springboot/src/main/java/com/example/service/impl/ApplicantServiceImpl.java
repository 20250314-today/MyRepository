package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.mapper.ApplicantMapper;
import com.example.entity.Applicant;
import com.example.service.ApplicantService;
import com.example.utils.TokenUtils;
import com.example.vo.ApplicantVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("applicantService")
public class ApplicantServiceImpl extends ServiceImpl<ApplicantMapper, Applicant> implements ApplicantService {
    @Resource
    ApplicantMapper applicantMapper;
    @Override
    public List<Applicant> selectAll(Applicant apply) {
        return applicantMapper.selectAll(apply);
    }
    @Override
    public PageInfo selectPage(Integer pageNum,Integer pageSize,Applicant applicant){
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            applicant.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Applicant> List = applicantMapper.selectAll(applicant);
        return PageInfo.of(List);
    }
    @Override
    public Map<String, Object> queryPage(ApplicantVo applicantVo) {
        Integer total = applicantMapper.queryCount(applicantVo);
        List<HashMap> data = applicantMapper.queryData(applicantVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}