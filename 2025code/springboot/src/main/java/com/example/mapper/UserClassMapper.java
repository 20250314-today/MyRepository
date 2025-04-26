package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserClass;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:21
 */
@Mapper
public interface UserClassMapper extends BaseMapper<UserClass> {

    Integer queryCount(UserClass userClassVo);

    List<HashMap> queryData(UserClass userClassVo);

    List<UserClass> selectByClassId(Integer classId);

    List<HashMap> findList(UserClass userClassVo);
}
