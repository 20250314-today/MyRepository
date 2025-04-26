package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:21
 */
public interface UserClassService extends IService<UserClass> {


    Map<String, Object> queryPage(UserClass userClassVo);

    List<UserClass> selectByClassId(Integer classId);

    List<HashMap> findList(UserClass userClassVo);
}

