package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {

    Integer queryCount(Subject subjectVo);

    List<HashMap> queryData(Subject subjectVo);

    List<HashMap> queryList(Subject subjectVo);

    Integer findPageCount(Subject subjectVo);

    List<HashMap> findPageData(Subject subjectVo);
}
