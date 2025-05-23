package com.example.controller;


import com.example.common.Result;
import com.example.entity.UserDoExercise;
import com.example.service.UserDoExerciseService;
import com.example.vo.UserDoExerciseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("study/userDoExercise")
public class UserDoExerciseController {
    @Autowired
    private UserDoExerciseService userDoExerciseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody UserDoExerciseVo userDoExerciseVo) {
        userDoExerciseVo.setPage((userDoExerciseVo.getPage() - 1) * userDoExerciseVo.getPageSize());
        Map<String, Object> page = userDoExerciseService.queryPage(userDoExerciseVo);
        return Result.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        UserDoExercise userDoExercise = userDoExerciseService.getById(id);

        return Result.success(userDoExercise);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserDoExerciseVo userDoExerciseVo) {
        UserDoExercise userDoExerciseEntity = new UserDoExercise();
        userDoExerciseEntity.setExerciseId(userDoExerciseVo.getExerciseId());
        userDoExerciseEntity.setReply(userDoExerciseVo.getContent());
        userDoExerciseEntity.setUserId(userDoExerciseVo.getUserId());
        boolean save = userDoExerciseService.save(userDoExerciseEntity);
        if (save){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody UserDoExercise userDoExercise) {
        boolean b = userDoExerciseService.updateById(userDoExercise);
        if (b){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        userDoExerciseService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
