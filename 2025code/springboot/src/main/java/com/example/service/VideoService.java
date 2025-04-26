package com.example.service;


import com.example.entity.Course;
import com.example.exception.CustomerException;
import com.example.mapper.VideoMapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
   @Resource
   VideoMapper videoMapper;
    public List<Course> selectAll(){
        return videoMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Course course) {
        PageHelper.startPage(pageNum,pageSize);
        List<Course> List = videoMapper.selectAll(course);
        return PageInfo.of(List);
    }

    public void add(Course course) {
        videoMapper.insert(course);
    }

    public void update(Course course) {
        videoMapper.updateById(course);
    }
    public void deleteById(Integer id) {
        videoMapper.deleteById(id);
    }

    public void deleteBatch(List<Course> list) {
        for (Course course : list) {
            this.deleteById(course.getId());
        }
    }

    public List<Course> selectAll(Course course) {
        return videoMapper.selectAll(course);
    }
    public Course selectById(Integer id){
        return videoMapper.selectById(id);
    }

}
