package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.VideoUpload;
import com.example.mapper.VideoUploadMapper;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UploadVideoService {
    @Resource
    VideoUploadMapper videoUploadMapper;
    public void add(VideoUpload introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setTid(currentUser.getId());
        introduction.setTime(DateUtil.now());
        videoUploadMapper.insert(introduction);
    }
    public void update(VideoUpload introduction) {
        videoUploadMapper.updateById(introduction);
    }
}
