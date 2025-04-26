package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface UsersService extends IService<User> {


    Map<String, Object> queryPage(User userVo);


    Map<String, Object> findNotDoWork(User userVo);

    Map<String, Object> findNotDoHomework(User userVo);

    List<HashMap> findList(User userVo);
}

