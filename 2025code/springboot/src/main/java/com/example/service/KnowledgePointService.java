package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.KnowledgePoint;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface KnowledgePointService extends IService<KnowledgePoint> {

    Map<String, Object> queryPage(KnowledgePoint knowledgePointVo);
}

