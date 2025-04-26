package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Homework;
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
public interface HomeworkMapper extends BaseMapper<Homework> {

    Integer queryCount(Homework homeworkVo);

    List<HashMap> queryData(Homework homeworkVo);

    List<HashMap> findNotDoHomeworkData(Homework homeworkVo);

    Integer findNotDoHomeworkCount(Homework homeworkVo);
}
