package com.example.controller;


import com.example.entity.VideoUpload;
import com.example.mapper.VideoUploadMapper;

import com.example.utils.NonStaticResourceHttpRequestHandler;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//前端获取后端视频流
@RestController
@RequestMapping("/SelectVideo")
@AllArgsConstructor
@Slf4j
public class SelectVideoController
{
    //引入返回视频流的组件
//    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    //把后端链接数据库接口引入进来
    @Resource
    VideoUploadMapper videoUploadMapper;

    @GetMapping("/getVideoSizeById/{videoId}")
    public long getVideoSizeById(@PathVariable("videoId") Integer videoId) throws IOException
    {
        VideoUpload videoPathList = videoUploadMapper.SelectVideoId(videoId);
        String videoPathUrl = videoPathList.getVideoUrl();
        Path filePath = Paths.get(videoPathUrl);

        if (Files.exists(filePath))
        {
            return Files.size(filePath);
        }
        return 0L;
    }

    //查询视频流的接口
    @GetMapping("/policemen/{videoId}")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable("videoId") Integer videoId) throws Exception
    {
        VideoUpload videoPathList = videoUploadMapper.SelectVideoId(videoId);
        String videoPathUrl = videoPathList.getVideoUrl();
        Path filePath = Paths.get(videoPathUrl);

        if (Files.exists(filePath))
        {
            String mimeType = Files.probeContentType(filePath);
            if (StringUtils.hasText(mimeType))
            {
                response.setContentType(mimeType);
            }

            // 设置支持部分请求（范围请求）的 'Accept-Ranges' 响应头
            response.setHeader("Accept-Ranges", "bytes");

            // 从请求头中获取请求的视频片段的范围（如果提供）
            long startByte = 0;
            long endByte = Files.size(filePath) - 1;
            String rangeHeader = request.getHeader("Range");
            // System.out.println("rangeHeader:" + rangeHeader);
            if (rangeHeader != null && rangeHeader.startsWith("bytes="))
            {
                String[] range = rangeHeader.substring(6).split("-");
                startByte = Long.parseLong(range[0]);
                if (range.length == 2)
                {
                    endByte = Long.parseLong(range[1]);
                }
            }

            // System.out.println("start:" + startByte + ",end:" + endByte);
            log.info("start:" + startByte + ",end:" + endByte);

            // 设置 'Content-Length' 响应头，指示正在发送的视频片段的大小
            long contentLength = endByte - startByte + 1;
            response.setHeader("Content-Length", String.valueOf(contentLength));

            // 设置 'Content-Range' 响应头，指示正在发送的视频片段的范围
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + Files.size(filePath));

            // 设置响应状态为 '206 Partial Content'
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 使用 'RangeFileChannel' 进行视频片段的传输，以高效地只读取文件的请求部分
            ServletOutputStream outputStream = response.getOutputStream();
            try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r"); FileChannel fileChannel = file.getChannel())
            {
                fileChannel.transferTo(startByte, contentLength, Channels.newChannel(outputStream));
            } finally
            {
                outputStream.close();
            }
        } else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    //查询视频表格列表的接口
    @GetMapping("/table")
    public List<VideoUpload> videoTable()
    {
        //调用搜索方法查询所有视频信息，成表格展示前端
        return videoUploadMapper.SelectVideoAll();
    }

}


