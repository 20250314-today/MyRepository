package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.KnowledgePointMapper;
import com.example.entity.KnowledgePoint;
import com.example.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("knowledgePointService")
public class KnowledgePointServiceImpl extends ServiceImpl<KnowledgePointMapper, KnowledgePoint> implements KnowledgePointService {

    @Autowired
    private KnowledgePointMapper knowledgePointDao;

    @Override
    public Map<String, Object> queryPage(KnowledgePoint knowledgePointVo) {
        Integer total = knowledgePointDao.queryCount(knowledgePointVo);
        List<HashMap> data = knowledgePointDao.queryData(knowledgePointVo);
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return result;
    }
}