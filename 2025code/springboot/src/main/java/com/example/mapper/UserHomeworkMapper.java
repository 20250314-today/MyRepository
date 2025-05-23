package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserHomework;
import com.example.vo.UserHomeworkVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@Mapper
public interface UserHomeworkMapper extends BaseMapper<UserHomework> {

    Integer queryCount(UserHomeworkVo userVo);

    List<HashMap> queryData(UserHomeworkVo userVo);


    Integer findNotDoWorkCount(UserHomeworkVo userVo);

    List<HashMap> findNotDoWorkData(UserHomeworkVo userVo);

    Integer findNotDoHomeworkCount(UserHomeworkVo userVo);

    List<HashMap> findNotDoHomeworkData(UserHomeworkVo userVo);

    List<HashMap> findList(UserHomeworkVo userVo);
}
