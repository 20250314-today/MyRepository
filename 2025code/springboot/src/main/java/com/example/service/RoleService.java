package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.RoleEntity;
import com.example.vo.RoleVo;


import java.util.Map;

/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:22:45
 */
public interface RoleService extends IService<RoleEntity> {


    Map<String, Object> queryPage(RoleVo roleVo);
}

