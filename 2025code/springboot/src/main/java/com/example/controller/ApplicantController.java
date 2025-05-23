package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Applicant;
import com.example.entity.Apply;
import com.example.entity.UserClass;
import com.example.service.ApplicantService;
import com.example.service.UserClassService;
import com.example.vo.ApplicantVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-13 00:22:11
 */

@RequestMapping("/applicant")
@RestController
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;


    @Autowired
    private UserClassService userClassService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody ApplicantVo applicantVo) {
        applicantVo.setPage((applicantVo.getPage() - 1) * applicantVo.getPageSize());
        Map<String, Object> page = applicantService.queryPage(applicantVo);
        return Result.success(page);
    }
    @GetMapping("/selectAll")//完整的请求路径：http://ip:port//apply//selectAll
    public Result selectAll(Applicant apply) {
        List<Applicant> applyList=applicantService.selectAll(apply);
        return Result.success(applyList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Applicant apply) {
        PageInfo<Applicant> pageInfo = applicantService.selectPage(pageNum, pageSize, apply);
        return Result.success(pageInfo);
    }
    /**
     * 列表
     */
    @RequestMapping("/joinTrueOrFalse")
    public Result joinTrueOrFalse(@RequestBody ApplicantVo applicantVo) {
        QueryWrapper<UserClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", applicantVo.getUserId());
        List<UserClass> list = userClassService.list(queryWrapper);

        List<Applicant> list1 = applicantService.list(new QueryWrapper<Applicant>().
                eq("user_id", applicantVo.getUserId())
                .eq("status", 1));
        return Result.success((list.size() + list1.size()) != 0);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        Applicant applicant = applicantService.getById(id);
        return Result.success(applicant);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Applicant applicant) {
        boolean save = applicantService.save(applicant);
        if (save) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    public Result update(@RequestBody ApplicantVo applicantVo) {
        try {
            // 更新 Applicant
            Applicant applicantEntity = new Applicant();
            applicantEntity.setStatus(applicantVo.getStatus());
            applicantEntity.setId(applicantVo.getId());
            boolean updated = applicantService.updateById(applicantEntity);
            if (!updated) {
                System.out.print("更新 Applicant 失败，ID: {}"+applicantVo.getId());
                return Result.error("更新申请状态失败");
            }

            if (applicantVo.getStatus() == 2) {
                // 检查是否已存在班级
                //QueryWrapper：使用字符串表示列名，通过字符串拼接的方式构建查询条件，类似于传统的SQL查询
                //LambdaQueryWrapper：使用Lambda表达式表示列名，通过Lambda表达式的方式构建查询条件
                LambdaQueryWrapper<UserClass> classQuery = new LambdaQueryWrapper<>();
                classQuery.eq(UserClass::getUserId, applicantVo.getUserId());
                if (userClassService.exists(classQuery)) {
                    return Result.error("该学生已加入其他班级");
                }
                // 保存 UserClass
                UserClass userClass = new UserClass();
                userClass.setUserId(applicantVo.getUserId());
                userClass.setClassId(applicantVo.getClassId());
                userClassService.save(userClass);
            }
            return Result.success();
        } catch (DuplicateKeyException e) {
            System.out.print("重复加入班级: userId={}, classId={} "+applicantVo.getUserId()+" "+applicantVo.getClassId());
            return Result.error("学生已在该班级中");
        } catch (Exception e) {
            System.out.print("更新过程中发生异常 "+ e);
            return Result.error("系统错误，请稍后重试");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        applicantService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
