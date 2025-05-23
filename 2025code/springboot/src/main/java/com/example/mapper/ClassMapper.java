package com.example.mapper;

import com.example.entity.Classs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassMapper {
    List<Classs> selectAll(Classs clas);

    void insert(Classs clas);

    void updateById(Classs clas);
    @Delete("delete from `class` where id = #{id}")
    void deleteById(Integer id);
//    @Select("select * from `category` where id=#{id}")
//    Introduction selectById(String id);
}
