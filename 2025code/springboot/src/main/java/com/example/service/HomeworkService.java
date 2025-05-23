package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Homework;
import com.example.vo.HomeworkVo;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface HomeworkService extends IService<Homework> {


    Map<String, Object> queryPage(HomeworkVo homeworkVo);

    Map<String, Object> findNotDoHomework(HomeworkVo homeworkVo);

}

