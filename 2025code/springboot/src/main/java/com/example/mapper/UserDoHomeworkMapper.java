package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserDoHomework;
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
public interface UserDoHomeworkMapper extends BaseMapper<UserDoHomework> {

    Integer queryCount(UserDoHomework userDoHomeworkVo);

    List<HashMap> queryData(UserDoHomework userDoHomeworkVo);
    Boolean updateModeByUserId(String userId, String homeworkId,String score,String remark);
}
