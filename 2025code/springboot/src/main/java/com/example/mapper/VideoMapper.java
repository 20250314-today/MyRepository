package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface VideoMapper {
    List<Course> selectAll(Course course);

    void insert(Course course);
@Select("select * from `course` where cname=#{cname}")
Course selectByCname(String cname);

    void updateById(Course course);
@Delete("delete from `course` where id = #{id}")
    void deleteById(Integer id);
@Select("select * from `course` where id=#{id}")
   Course selectById(Integer id);
}
