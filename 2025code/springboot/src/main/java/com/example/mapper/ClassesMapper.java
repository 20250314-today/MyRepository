package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:22:45
 */
@Mapper
public interface ClassesMapper extends BaseMapper<Classes> {

    Integer queryCount( Classes classVo);

    List<HashMap> queryData( Classes classVo);

    List<HashMap> findList( Classes classVo);
}
