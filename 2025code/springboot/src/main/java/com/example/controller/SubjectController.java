package com.example.controller;

import com.example.common.Result;
import com.example.entity.Subject;
import com.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
@RestController
@RequestMapping("study/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody Subject subjectVo){
        subjectVo.setPage((subjectVo.getPage() - 1) * subjectVo.getPageSize());
        Map<String, Object> page = subjectService.queryPage(subjectVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody Subject subjectVo){
        subjectVo.setPage((subjectVo.getPage() - 1) * subjectVo.getPageSize());
        Map<String, Object> page = subjectService.findPage(subjectVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/queryList")
    public Result queryList(@RequestBody Subject SubjectVo) {
        List<HashMap> page = subjectService.queryList(SubjectVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @GetMapping("/findList")
    public Result findList() {
        List<Subject> list = subjectService.list();
        return Result.success(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		Subject subject = subjectService.getById(id);
        return Result.success(subject);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Subject subject){
        boolean save = subjectService.save(subject);
        if (save) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Subject subject){
        boolean update = subjectService.updateById(subject);
        if (update) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Subject subjectEntity){
        boolean b = subjectService.removeById(subjectEntity.getId());
        if (b) {
            return Result.success();
        }
        return Result.error("504");
    }

}
