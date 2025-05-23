package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.VideoTotalEntity;
import com.example.vo.VideoTotalVo;
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
public interface VideoTotalDao extends BaseMapper<VideoTotalEntity> {

    Integer queryCount(VideoTotalVo videoTotalVo);

    List<HashMap> queryData(VideoTotalVo videoTotalVo);

}
