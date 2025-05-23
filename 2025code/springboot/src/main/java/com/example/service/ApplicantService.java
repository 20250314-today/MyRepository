package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Applicant;
import com.example.entity.Apply;
import com.example.vo.ApplicantVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-13 00:22:11
 */
public interface ApplicantService extends IService<Applicant> {

    List<Applicant> selectAll(Applicant apply);

    PageInfo selectPage(Integer pageNum, Integer pageSize, Applicant applicant);
    Map<String, Object> queryPage(ApplicantVo applicantVo);
}

