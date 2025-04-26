package com.example.controller;

import com.example.common.Result;
import com.example.entity.UserSubject;
import com.example.service.UserSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */
@RestController
@RequestMapping("study/userSubject")
public class UserSubjectController {
    @Autowired
    private UserSubjectService userSubjectService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody UserSubject userSubjectVo){
        userSubjectVo.setPage((userSubjectVo.getPage() - 1) * userSubjectVo.getPageSize());
        Map<String, Object> page = userSubjectService.queryPage(userSubjectVo);
        return Result.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		UserSubject userSubject = userSubjectService.getById(id);
        return Result.success(userSubject);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserSubject userSubject){
        boolean save = userSubjectService.save(userSubject);
        if (save) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody UserSubject userSubject){
        boolean update = userSubjectService.updateById(userSubject);
        if (update) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        boolean b = userSubjectService.removeByIds(Arrays.asList(ids));
        if (b) {
            return Result.success();
        }
        return Result.error("504");
    }

}
