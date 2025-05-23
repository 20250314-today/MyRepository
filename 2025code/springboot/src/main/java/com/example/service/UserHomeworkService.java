package com.example.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserHomework;
import com.example.vo.UserHomeworkVo;

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
public interface UserHomeworkService extends IService<UserHomework> {


    Map<String, Object> queryPage(UserHomeworkVo userVo);


    Map<String, Object> findNotDoWork(UserHomeworkVo userVo);

    Map<String, Object> findNotDoHomework(UserHomeworkVo userVo);

    List<HashMap> findList(UserHomeworkVo userVo);
}

