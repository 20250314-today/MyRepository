package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.VideosEntity;
import com.example.service.VideosService;
import com.example.utils.FileUtil;
import com.example.vo.VideosVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;


/**
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */
@RestController
@RequestMapping("/study/videos")
public class VideosController {

    @Value("${file.videoPath}")
    private String videoPath;

    @Autowired
    private VideosService videosService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(@RequestBody VideosVo videosVo) {
        videosVo.setPage((videosVo.getPage() - 1) * videosVo.getPageSize());
        Map<String, Object> page = videosService.queryPage(videosVo);
        return Result.success(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/selectByVideoTotalId")
    public Result selectByVideoTotalId(@RequestBody VideosEntity videosVo) {
        QueryWrapper<VideosEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_total_id", videosVo.getVideoTotalId());
        queryWrapper.orderByAsc("sort");
        List<VideosEntity> list = videosService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        VideosEntity videos = videosService.getById(id);

        return Result.success(videos);
    }

    /**
     * 保存
     */
    @CrossOrigin
    @RequestMapping("/save")
    public Result save(@RequestParam Integer sort,
                       @RequestParam String topic,
                       @RequestParam Integer videoTotalId,
                       MultipartFile file) throws UnknownHostException {
        VideosEntity videos = new VideosEntity();
        videos.setSort(sort);
        videos.setTopic(topic);
        videos.setVideoTotalId(videoTotalId);
        videos.setPath(videoPath + file.getOriginalFilename());
        videos.setVideoUrl("http://localhost:9999/file/videoFile/" + file.getOriginalFilename());

        boolean save = videosService.save(videos);
        if (save) {
            try {
                FileUtil.uploadFile(file.getBytes(), videoPath, file.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody VideosEntity videos) {
        boolean b = videosService.updateById(videos);
        if (b) {
            return Result.success();
        }
        return Result.error("504");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody VideosEntity videosEntity) {
        VideosEntity byId = videosService.getById(videosEntity.getId());
        boolean b = FileUtil.deleteFile(byId.getPath());
        if (b) {
            boolean remove = videosService.removeById(videosEntity.getId());
            if (remove) {
                return Result.success();
            }
        }
        return Result.error("504");
    }

}
