package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Applicant;
import com.example.vo.ApplicantVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-13 00:22:11
 */
@Mapper
public interface ApplicantMapper extends BaseMapper<Applicant> {

    Integer queryCount(ApplicantVo applicantVo);

    List<HashMap> queryData(ApplicantVo applicantVo);

    List<Applicant> selectAll(Applicant applicant);
}
