package com.example.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.VideoTotalEntity;
import com.example.vo.VideoTotalVo;

import java.util.Map;

/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface VideoTotalService extends IService<VideoTotalEntity> {

    Map<String, Object> queryPage(VideoTotalVo videoTotalVo);

    Page<VideoTotalEntity> pageVideoTotal(VideoTotalVo videoTotalVo);

}

