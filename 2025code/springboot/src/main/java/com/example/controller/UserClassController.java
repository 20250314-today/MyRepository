package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.UserClass;
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
 * @date 2022-02-12 00:24:21
 */
@RestController
@RequestMapping("study/userClass")
public class UserClassController {
    @Autowired
    private UserClassService userClassService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody  UserClass userClassVo) {
        userClassVo.setPage((userClassVo.getPage() - 1) * userClassVo.getPageSize());
        Map<String, Object> page = userClassService.queryPage(userClassVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findList")
    public Result findList(@RequestBody  UserClass userClassVo) {
        List<HashMap> data = userClassService.findList(userClassVo);
        return Result.success(data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        UserClass userClass = userClassService.getById(id);

        return Result.success(userClass);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserClass userClass) {
        boolean save = userClassService.save(userClass);
        if (save){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody UserClass userClass) {
        boolean b = userClassService.updateById(userClass);
        if (b){
            return Result.success();
        }
        return Result.error("504");

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody UserClass userClass) {
        QueryWrapper<UserClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userClass.getUserId());
        boolean remove = userClassService.remove(queryWrapper);
        if (remove){
            return Result.success();
        }
        return Result.error("504");
    }

}
