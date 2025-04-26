package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@Mapper
public interface KnowledgePointMapper extends BaseMapper<KnowledgePoint> {

    Integer queryCount(KnowledgePoint knowledgePointVo);

    List<HashMap> queryData(KnowledgePoint knowledgePointVo);
}
