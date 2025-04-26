package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Class;
import org.apache.ibatis.annotations.Mapper;

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
public interface ClassMapper extends BaseMapper<Class> {

    Integer queryCount(Class classVo);

    List<HashMap> queryData(Class classVo);

    List<HashMap> findList(Class classVo);
}
