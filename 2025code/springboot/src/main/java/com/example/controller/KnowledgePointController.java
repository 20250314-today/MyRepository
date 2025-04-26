package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.KnowledgePoint;
import com.example.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("study/knowledgePoint")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointService knowledgePointService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody  KnowledgePoint knowledgePointVo) {
        knowledgePointVo.setPage((knowledgePointVo.getPage() - 1) * knowledgePointVo.getPageSize());
        Map<String, Object> page = knowledgePointService.queryPage(knowledgePointVo);
        return Result.success(page);
    }

    @RequestMapping("/stuPointList")
    public Result stuPointList(@RequestParam("classId") String classId) {
        QueryWrapper<KnowledgePoint> queryWrapper= new QueryWrapper<>();
        QueryWrapper<KnowledgePoint> eq= queryWrapper.eq("class_id",classId);
        List<KnowledgePoint> resultData=knowledgePointService.list(eq);
        return Result.success(resultData);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        KnowledgePoint knowledgePoint = knowledgePointService.getById(id);

        return Result.success(knowledgePoint);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody KnowledgePoint knowledgePoint) {
        boolean save = knowledgePointService.save(knowledgePoint);
        if (save){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody KnowledgePoint knowledgePointVo) {
        KnowledgePoint knowledgePointEntity = new KnowledgePoint();
        knowledgePointEntity.setId(knowledgePointVo.getId());
        knowledgePointEntity.setClassId(knowledgePointVo.getClassId());
        knowledgePointEntity.setTitle(knowledgePointVo.getTitle());
        knowledgePointEntity.setContent(knowledgePointVo.getContent());
        boolean b = knowledgePointService.updateById(knowledgePointEntity);
        if (b){
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody KnowledgePoint knowledgePoint) {
        boolean remove = knowledgePointService.removeById(knowledgePoint.getId());
        if (remove){
            return Result.success();
        }
        return Result.error("504");
    }

}
