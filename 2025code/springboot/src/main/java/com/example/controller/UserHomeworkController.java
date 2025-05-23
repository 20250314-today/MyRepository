package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.common.Result;
import com.example.entity.UserHomework;
import com.example.service.UserClassService;
import com.example.service.UserHomeworkService;
import com.example.service.UserSubjectService;
import com.example.vo.UserHomeworkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("/study/user")
public class UserHomeworkController {
    @Autowired
    private UserHomeworkService userService;



    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody UserHomeworkVo userVo) {
        userVo.setPage((userVo.getPage() - 1) * userVo.getPageSize());
        Map<String, Object> page = userService.queryPage(userVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findList")
    public Result findList(@RequestBody UserHomeworkVo userVo) {
        List<HashMap> page = userService.findList(userVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findNotDoWork")
    public Result findNotDoWork(@RequestBody UserHomeworkVo userVo) {
        userVo.setPage((userVo.getPage() - 1) * userVo.getPageSize());
        Map<String, Object> page = userService.findNotDoWork(userVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findNotDoHomework")
    public Result findNotDoHomework(@RequestBody UserHomeworkVo userVo) {
        userVo.setPage((userVo.getPage() - 1) * userVo.getPageSize());
        Map<String, Object> page = userService.findNotDoHomework(userVo);
        return Result.success(page);
    }



}
