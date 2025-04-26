package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserSubject;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
public interface UserSubjectService extends IService<UserSubject> {

    Map<String, Object> queryPage(UserSubject userSubjectVo);
}

