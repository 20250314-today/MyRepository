package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Book;
import com.example.exception.CustomerException;
import com.example.mapper.BookMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
   @Resource
   BookMapper bookMapper;

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Book book) {
        PageHelper.startPage(pageNum,pageSize);
        List<Book> List = bookMapper.selectAll(book);
        return PageInfo.of(List);
    }

    public void add(Book book) {
      // Account currentUser =  TokenUtils.getCurrentUser();
        bookMapper.insert(book);
    }
    public void update(Book book) {
        bookMapper.updateById(book);
    }
    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }


    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }


}
