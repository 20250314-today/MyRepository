package com.example.mapper;

import com.example.entity.Introduction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface IntroductionMapper {
    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);

    void updateById(Introduction introduction);
    @Delete("delete from `introduction` where id = #{id}")
    void deleteById(Integer id);
//    @Select("select * from `category` where id=#{id}")
//    Introduction selectById(String id);
}
