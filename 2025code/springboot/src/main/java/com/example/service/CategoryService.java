package com.example.service;
import com.example.entity.Category;
import com.example.exception.CustomerException;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
   @Resource
   CategoryMapper categoryMapper;
    public String category(String name){
        if("category".equals(name)){
            return "category";
        }
        else {
            throw new CustomerException("账号错误");
        }
    }
    public List<Category> selectAll(){
        return categoryMapper.selectAll(null);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, Category category) {
        PageHelper.startPage(pageNum,pageSize);
        List<Category> List = categoryMapper.selectAll(category);
        return PageInfo.of(List);
    }
    public void add(Category category) {
        categoryMapper.insert(category);
    }
    public void update(Category category) {
        categoryMapper.updateById(category);
    }
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }
    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

}
