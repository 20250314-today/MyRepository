package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Classes;
import com.example.entity.Classes;

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
public interface ClassesService extends IService<Classes> {

    Map<String, Object> queryPage(Classes classVo);

    List<HashMap> findList(Classes classVo);
}

