package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Homework;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface HomeworkService extends IService<Homework> {


    Map<String, Object> queryPage(Homework homeworkVo);

    Map<String, Object> findNotDoHomework(Homework homeworkVo);

}

