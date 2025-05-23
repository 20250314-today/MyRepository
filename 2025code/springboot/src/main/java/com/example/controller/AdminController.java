package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
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

@RequestMapping("/admin")
@RestController
public class AdminController {
    @Resource
    AdminService adminService;
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list){//RequestBody 接受前端传来的json对象
        adminService.deleteBatch(list);
        return Result.success();
    }
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){//RequestBody 接受前端传来的json对象
        adminService.add(admin);
        return Result.success();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Admin admin){//RequestBody 接受前端传来的json对象
        adminService.update(admin);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){//RequestBody 接受前端传来的路径参数
        adminService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/selectAll")//完整的请求路径：http://ip:port//admin//selectAll
    public Result selectAll(Admin admin) {
        List<Admin> adminList=adminService.selectAll(admin);
        return Result.success(adminList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(pageInfo);
    }
    @GetMapping("/export")
    //HttpServletResponse response：用于输出 Excel 文件
    public void exportData(Admin admin, HttpServletResponse response) throws Exception {
        String ids = admin.getIds();
        if(StrUtil.isNotBlank(ids)){
            //获取请求参数中的ids（以逗号分隔）
            String[] idsArr=ids.split(",");
            admin.setIdsArr(idsArr);
        }
//        拿到所有数据,调用服务层方法获取符合条件的管理员列表
        List<Admin> list=adminService.selectAll(admin);
        //ExcelUtil.getWriter(true)：创建 Excel 写入器，true表示创建 xlsx 格式
        ExcelWriter writer=ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username","账号");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("email","邮箱");
        //setOnlyAlias(true)：只导出设置了别名的字段
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
    //MultipartFile file：接收上传的 Excel 文件
    public Result importData(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        List<Admin> list=reader.readAll(Admin.class);
        for (Admin admin:list){
            //调用服务层的add方法逐个插入数据
            adminService.add(admin);
        }
        return  Result.success();
    }
}
