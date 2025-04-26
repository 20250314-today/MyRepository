package com.example.controller;

import com.example.common.Result;
import com.example.entity.Exercises;
import com.example.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 
 *
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("study/exercises")
public class ExercisesController {
    @Autowired
    private ExercisesService exercisesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody  Exercises exercisesVo){
        exercisesVo.setPage((exercisesVo.getPage() - 1) * exercisesVo.getPageSize());
        Map<String, Object> page = exercisesService.queryPage(exercisesVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/findNotDoExercises")
    public Result findNotDoExercises(@RequestBody  Exercises exercisesVo){
        exercisesVo.setPage((exercisesVo.getPage() - 1) * exercisesVo.getPageSize());
        Map<String, Object> page = exercisesService.findNotDoExercises(exercisesVo);
        return Result.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		Exercises exercises = exercisesService.getById(id);

        return Result.success(exercises);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Exercises exercises){
        boolean save = exercisesService.save(exercises);
        if (save){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Exercises exercises){
        boolean b = exercisesService.updateById(exercises);
        if (b){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Exercises exercises){
        boolean remove = exercisesService.removeById(exercises.getId());
        if (remove){
            return  Result.success();
        }
        return  Result.error("504");
    }

}
