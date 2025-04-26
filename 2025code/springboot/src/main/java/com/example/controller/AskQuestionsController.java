package com.example.controller;

import com.example.common.Result;
import com.example.entity.AskQuestions;
import com.example.service.AskQuestionsService;
import com.example.vo.AskQuestionsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:21
 */
@RestController
@RequestMapping("study/askQuestions")
public class AskQuestionsController {
    @Autowired
    private AskQuestionsService askQuestionsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody AskQuestionsVo askQuestionsVo) {
        askQuestionsVo.setPage((askQuestionsVo.getPage() - 1) * askQuestionsVo.getPageSize());
        Map<String, Object> page = askQuestionsService.queryPage(askQuestionsVo);
        return Result.success(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        AskQuestions askQuestions = askQuestionsService.getById(id);
        return Result.success(askQuestions);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody AskQuestions askQuestions) {
        boolean save = askQuestionsService.save(askQuestions);
        if (save) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody AskQuestionsVo askQuestionsVo) {
        AskQuestions askQuestionsEntity = new AskQuestions();
        askQuestionsEntity.setStatus(askQuestionsVo.getStatus());
        askQuestionsEntity.setId(askQuestionsVo.getId());
        askQuestionsEntity.setRestore(askQuestionsVo.getRestore());
        boolean b = askQuestionsService.updateById(askQuestionsEntity);
        if (b) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody AskQuestions askQuestionsEntity) {
        boolean remove = askQuestionsService.removeById(askQuestionsEntity.getId());
        if (remove) {
            return Result.success();
        }
        return Result.error("504");
    }

}

