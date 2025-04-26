package com.example.mapper;

import com.example.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BookMapper {
    List<Book> selectAll(Book book);

    void insert(Book book);

    void updateById(Book book);
@Delete("delete from `book` where id = #{id}")
    void deleteById(Integer id);

}
