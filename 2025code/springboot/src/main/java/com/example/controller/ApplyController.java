package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.common.Result;
import com.example.entity.Apply;
import com.example.service.ApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RequestMapping("/apply")
@RestController
public class ApplyController {
    @Resource
    ApplyService applyService;
//    @DeleteMapping("/deleteBatch")
//    public Result deleteBatch(@RequestBody List<Apply> list){//RequestBody 接受前端传来的json对象
//        applyService.deleteBatch(list);
//        return Result.success();
//    }
    @PostMapping("/add")
    public Result add(@RequestBody Apply apply){//RequestBody 接受前端传来的json对象
        applyService.add(apply);
        return Result.success();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Apply apply){//RequestBody 接受前端传来的json对象
        applyService.update(apply);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){//RequestBody 接受前端传来的路径参数
        applyService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/selectAll")//完整的请求路径：http://ip:port//apply//selectAll
    public Result selectAll(Apply apply) {
        List<Apply> applyList=applyService.selectAll(apply);
        return Result.success(applyList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Apply apply) {
        PageInfo<Apply> pageInfo = applyService.selectPage(pageNum, pageSize, apply);
        return Result.success(pageInfo);
    }
//    @GetMapping("/export")
//    public void exportData(Apply apply, HttpServletResponse response) throws Exception {
//        String ids = apply.getIds();
//        if(StrUtil.isNotBlank(ids)){
//            String[] idsArr=ids.split(",");
//            apply.setIdsArr(idsArr);
//        }
//       // 拿到所有数据
//        List<Apply> list=applyService.selectAll(apply);
//        ExcelWriter writer=ExcelUtil.getWriter(true);
//        writer.addHeaderAlias("applyname","账号");
//        writer.addHeaderAlias("name","名称");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("email","邮箱");
//        writer.setOnlyAlias(true);
////        写出数据writer
//        writer.write(list);
////        输出的文件的名称以及输出流的头信息
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String filename= URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
//        response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
////        写出到输出流，并关闭writer
//        ServletOutputStream os=response.getOutputStream();
//        writer.flush(os);
//        writer.close();
//        os.close();
//    }
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号","applyname");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        List<Apply> list=reader.readAll(Apply.class);
        for (Apply apply:list){
            applyService.add(apply);
        }
        return  Result.success();
    }
}
