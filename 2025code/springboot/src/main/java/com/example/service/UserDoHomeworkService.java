package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserDoHomework;

import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
public interface UserDoHomeworkService extends IService<UserDoHomework> {


    Map<String, Object> queryPage(UserDoHomework userDoHomeworkVo);

    Boolean updateModeByUserId(String userId, String homeworkId,String score,String remark);

}

