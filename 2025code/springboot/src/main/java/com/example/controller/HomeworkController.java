package com.example.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Homework;
import com.example.entity.UserDoHomework;
import com.example.service.HomeworkService;
import com.example.service.UserClassService;
import com.example.service.UserDoHomeworkService;
import com.example.vo.HomeworkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("study/homework")
public class HomeworkController {

    @Autowired
    private UserClassService userClassService;

    @Autowired
    private UserDoHomeworkService userDoHomeworkService;

    @Autowired
    private HomeworkService homeworkService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody HomeworkVo homeworkVo) {
        homeworkVo.setPage((homeworkVo.getPage() - 1) * homeworkVo.getPageSize());
        Map<String, Object> page = homeworkService.queryPage(homeworkVo);
        return Result.success(page);
    }


    /**
     * 查询未做作业列表
     */
    @RequestMapping("/findNotDoHomework")
    public Result findNotDoHomework(@RequestBody HomeworkVo homeworkVo) {
        homeworkVo.setPage((homeworkVo.getPage() - 1) * homeworkVo.getPageSize());
        Map<String, Object> page = homeworkService.findNotDoHomework(homeworkVo);
        return Result.success(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        Homework homework = homeworkService.getById(id);

        return Result.success(homework);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Homework homework) {
        boolean save;
        if (homework.getId()!=null){
            save = homeworkService.updateById(homework);
            if (homework.getUserId()!=null && !Objects.equals(homework.getUserId(), "")){
                 userDoHomeworkService.updateModeByUserId(homework.getUserId(),homework.getId()+"",homework.getScore(),homework.getRemark());
            }

        }else {
            homework.setCreateTime(DateUtil.now());
            save = homeworkService.save(homework);
        }

        if (save) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Homework homework) {
        boolean b = homeworkService.updateById(homework);
        if (b) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Homework homework) {
        boolean b = homeworkService.removeById(homework.getId());
        if (b){
            QueryWrapper<UserDoHomework> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("homework_id",homework.getId());
            boolean remove = userDoHomeworkService.remove(queryWrapper);
            return Result.success();
        }
        return Result.error("504");
    }

}
