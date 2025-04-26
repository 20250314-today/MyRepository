package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
public interface SubjectService extends IService<Subject> {

    Map<String, Object> queryPage(Subject subjectVo);

    List<HashMap> queryList(Subject subjectVo);

    Map<String, Object> findPage(Subject subjectVo);
}

