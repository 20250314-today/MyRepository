package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.RoleEntity;
import com.example.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:22:45
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

    Integer queryCount(RoleVo roleVo);

    List<HashMap> queryData(RoleVo roleVo);
}
