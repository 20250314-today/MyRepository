package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.VideosEntity;
import com.example.vo.VideosVo;
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
public interface VideosDao extends BaseMapper<VideosEntity> {

    Integer queryCount(VideosVo videosVo);

    List<HashMap> queryData(VideosVo videosVo);
}
