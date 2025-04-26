package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Class;
import com.example.entity.UserClass;
import com.example.service.ClassService;
import com.example.service.UserClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:22:45
 */
@RestController
@RequestMapping("study/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    @Autowired
    private UserClassService userClassService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody Class classVo) {
        classVo.setPage((classVo.getPage() - 1) * classVo.getPageSize());
        Map<String, Object> page = classService.queryPage(classVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findList")
    public Result findList(@RequestBody Class classVo) {
        List<HashMap> page = classService.findList(classVo);
        return Result.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        Class classEntity = classService.getById(id);
        return Result.success(classEntity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Class classEntity) {
        boolean save = classService.save(classEntity);
        if (save){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Class classEntity) {
        boolean b = classService.updateById(classEntity);
        if (b){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Class classEntity) {
        classService.removeById(classEntity.getId());

        //删除对应班级的所有人员
        QueryWrapper<UserClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classEntity.getId());
        userClassService.remove(queryWrapper);

        return Result.success();
    }

}
