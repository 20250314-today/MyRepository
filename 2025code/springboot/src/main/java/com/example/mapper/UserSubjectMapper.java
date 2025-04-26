package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserSubject;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
@Mapper
public interface UserSubjectMapper extends BaseMapper<UserSubject> {

    Integer queryCount(UserSubject userSubjectVo);

    List<HashMap> queryData(UserSubject userSubjectVo);
}
