package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserClass;
import com.example.vo.UserClassVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    Integer queryCount( UserClassVo userClassVo);

    List<HashMap> queryData( UserClassVo userClassVo);

    List<UserClass> selectByClassId( Integer classId);

    List<HashMap> selectList( UserClassVo userClassVo);
}
