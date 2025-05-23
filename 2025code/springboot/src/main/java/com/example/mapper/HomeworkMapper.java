package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Homework;
import com.example.vo.HomeworkVo;
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

    Integer queryCount(HomeworkVo homeworkVo);

    List<HashMap> queryData(HomeworkVo homeworkVo);

    List<HashMap> findNotDoHomeworkData(HomeworkVo homeworkVo);

    Integer findNotDoHomeworkCount(HomeworkVo homeworkVo);
}
