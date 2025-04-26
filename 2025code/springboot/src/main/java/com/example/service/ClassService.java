package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Class;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:22:45
 */
public interface ClassService extends IService<Class> {

    Map<String, Object> queryPage(Class classVo);

    List<HashMap> findList(Class classVo);
}

