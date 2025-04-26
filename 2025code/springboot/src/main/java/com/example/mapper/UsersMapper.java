package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UsersMapper extends BaseMapper<User> {

    Integer queryCount(User userVo);

    List<HashMap> queryData(User userVo);


    Integer findNotDoWorkCount(User userVo);

    List<HashMap> findNotDoWorkData(User userVo);

    Integer findNotDoHomeworkCount(User userVo);

    List<HashMap> findNotDoHomeworkData(User userVo);

    List<HashMap> findList(User userVo);
}
