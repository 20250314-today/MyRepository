package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.common.Result;
import com.example.entity.Classs;
import com.example.service.ClassService;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RequestMapping("/class")
@RestController
public class ClassController {
    @Resource
    ClassService classService;

    @PostMapping("/add")
    public Result add(@RequestBody Classs clas){//RequestBody 接受前端传来的json对象
        classService.add(clas);
        return Result.success();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Classs clas){//RequestBody 接受前端传来的json对象
        classService.update(clas);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){//RequestBody 接受前端传来的路径参数
        classService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/selectAll")//完整的请求路径：http://ip:port//introduction//selectAll
    public Result selectAll(Classs clas) {
        List<Classs> introductionList=classService.selectAll(clas);
        return Result.success(introductionList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Classs clas) {
        PageInfo<Classs> pageInfo = classService.selectPage(pageNum, pageSize, clas);
        return Result.success(pageInfo);
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号","introductionname");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        List<Classs> list=reader.readAll(Classs.class);
        for (Classs clas:list){
            classService.add(clas);
        }
        return  Result.success();
    }
}
