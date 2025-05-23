package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Teacher;
import com.example.entity.User;
import com.example.service.TeacherService;
import com.example.service.UserService;
import com.example.service.UsersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RequestMapping("/teacher")
@RestController
public class TeacherController {
    @Resource
    TeacherService teacherService;
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Teacher> list){
        //RequestBody 接受前端传来的json对象
        teacherService.deleteBatch(list);
        return Result.success();
    }
    @PostMapping("/add")
    public Result add(@RequestBody Teacher user){//RequestBody 接受前端传来的json对象
        teacherService.add(user);
        return Result.success();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Teacher user){//RequestBody 接受前端传来的json对象
        teacherService.update(user);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){//RequestBody 接受前端传来的路径参数
        teacherService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/selectAll")//完整的请求路径：http://ip:port//user//selectAll
    public Result selectAll(Teacher user) {
        List<Teacher> userList=teacherService.selectAll(user);
        return Result.success(userList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Teacher user) {
        PageInfo<Teacher> pageInfo = teacherService.selectPage(pageNum, pageSize, user);
        return Result.success(pageInfo);
    }

    @GetMapping("/export")
    public void exportData(Teacher user, HttpServletResponse response) throws Exception {
        String ids = user.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr=ids.split(",");
            user.setIdsArr(idsArr);
        }
//        拿到所有数据
        List<Teacher> list=teacherService.selectAll(user);
        ExcelWriter writer=ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username","账号");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("email","邮箱");
        writer.setOnlyAlias(true);
//        写出数据writer
        writer.write(list);
//        输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String filename= URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
//        写出到输出流，并关闭writer
        ServletOutputStream os=response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        List<Teacher> list=reader.readAll(Teacher.class);
        for (Teacher user:list){
            teacherService.add(user);
        }
        return  Result.success();
    }
}
