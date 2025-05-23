package com.example.controller;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

import com.example.common.Result;
import com.example.entity.Chunk;
import com.example.entity.Introduction;
import com.example.entity.VideoUpload;
import com.example.mapper.VideoUploadMapper;
import com.example.service.IntroductionService;
import com.example.service.UploadVideoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//接口：前端视频上传
@RestController
//一级地址
@RequestMapping("/api")
@Slf4j
public class uploadVideoController
{
    @Resource
    VideoUploadMapper videoUpload;
    @Value("${jiliason.profile}")
    String saveVideoFilePath;

    /**
     * 分片上传
     * 处理文件上传POST请求
     * 将上传的文件存放到服务器内
     *
     * @param chunk    文件块
     * @param response 响应
     * @return 上传响应状态
     * * 每一个上传块都会包含如下分块信息：
     * * chunkNumber: 当前块的次序，第一个块是 1，注意不是从 0 开始的。
     * * totalChunks: 文件被分成块的总数。
     * * chunkSize: 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
     * * currentChunkSize: 当前块的大小，实际大小。
     * * totalSize: 文件总大小。
     * * identifier: 这个就是每个文件的唯一标示。
     * * filename: 文件名。
     * * relativePath: 文件夹上传的时候文件的相对路径属性。
     * * 一个分块可以被上传多次，当然这肯定不是标准行为，但是在实际上传过程中是可能发生这种事情的，这种重传也是本库的特性之一。
     * *
     * * 根据响应码认为成功或失败的：
     * * 200 文件上传完成
     * * 201 文加快上传成功
     * * 500 第一块上传失败，取消整个文件上传
     * * 507 服务器出错自动重试该文件块上传
     */
    @Resource
    UploadVideoService uploadVideoService;
    @PostMapping("/add")
    public Result add(@RequestBody VideoUpload introduction){//RequestBody 接受前端传来的json对象
        uploadVideoService.add(introduction);
        return Result.success();
    }
    @PostMapping("/update")
    public Result update(@RequestBody VideoUpload introduction){//RequestBody 接受前端传来的json对象
        uploadVideoService.update(introduction);
        return Result.success();
    }
    @PostMapping("/fileUpload")
    public String uploadPost(@ModelAttribute Chunk chunk, HttpServletResponse response)
    {
        log.info(chunk.toString());
        // 设置
        Path absolutePath = Paths.get("").toAbsolutePath().resolve(saveVideoFilePath.substring(2) + "/videos");
        String absoluteDiskPath = absolutePath.toString();
        String SavePath = absoluteDiskPath;
        File file = new File(SavePath, chunk.getFilename());
        if (!file.getParentFile().exists())
        {
            //如果不存在，就创建一个这个路径的文件夹。
            file.getParentFile().mkdirs();
        }
        //第一个块,则新建文件
        if (chunk.getChunkNumber() == 1 && !file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                response.setStatus(500);
                return "exception:createFileException";
            }
        }

        //进行写文件操作
        try (
                //将块文件写入文件中
                InputStream fos = chunk.getFile().getInputStream(); RandomAccessFile raf = new RandomAccessFile(file, "rw"))
        {
            int len = -1;
            byte[] buffer = new byte[1024];
            raf.seek((chunk.getChunkNumber() - 1) * chunk.getChunkSize());
            while ((len = fos.read(buffer)) != -1)
            {
                raf.write(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (chunk.getChunkNumber() == 1)
            {
                file.delete();
            }
            response.setStatus(507);
            return "exception:writeFileException";
        }
        if (chunk.getChunkNumber().equals(chunk.getTotalChunks()))
        {
            response.setStatus(200);
            // TODO 向数据库中保存上传信息
            String videoNameText = chunk.getFilename();
            // 获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = videoNameText.substring(videoNameText.lastIndexOf(".") + 1).toLowerCase();
            // UUID(全局唯一标识符)randomUUID(随机生成标识符)toString(转成字符串)replaceAll(替换字符方法，因为随机生成的里面包括了 - ，这里意思是把 - 全部换成空)
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            // 视频名字拼接：唯一标识符加上点，再加上上面的视频后缀也就是MP4之类的。就组成了现在的视频名字，比如这样：c7bbc1f9664947a287d35dd7cdc48a95.mp4
            String newVideoName = pikId + "." + fileExt;
            System.out.println("原文件名：" + videoNameText);
            System.out.println("重构文件名防止上传同名文件：" + newVideoName);
            // 把已保存的原文件名文件改成新文件名
            try
            {
                Files.move(Paths.get(SavePath + "/" + videoNameText), Paths.get(SavePath + "/" + newVideoName));
            }
            catch (IOException e)
            {
                return e.toString();
            }
            //保存视频url路径地址
            String videoUrl = SavePath + "/" + newVideoName;
            //调用数据库接口插入数据库方法save，把视频原名，视频路径，视频的唯一标识码传进去存到数据库内
            videoUpload.save(videoNameText, videoUrl, newVideoName);
            return "over";
        }
        else
        {
            response.setStatus(201);
            return "ok";
        }
    }

    /**
     * 不分片上传
     *
     * @param file
     * @param SavePath
     * @return
     * @throws IllegalStateException
     */
    //二级地址
    @PostMapping("/uploadVideo3")
    @ResponseBody
    //Map<String,String>: map是键值对形式组成的集合，类似前端的数组但是里面是键值对形式的，前后两个string代表键和值都是字符串格式的。
    //post请求传入的参数：MultipartFile file(理解为springmvc框架给我们提供的工具类，代表视频流数据)，SavePath(前台传来的地址路径，也是用来后端保存在服务器哪个文件夹的地址)
    public Map<String, String> savaVideoTest(@RequestParam("file") MultipartFile file, @RequestParam String SavePath)
    //throws IllegalStateException写在方法的前面是可以抛出异常状态的，如果有错误会把错误信息发出来对应下面的try和catch
            throws IllegalStateException
    {
        Path absolutePath = Paths.get("").toAbsolutePath().resolve(saveVideoFilePath.substring(2) + "/videos");
        String absoluteDiskPath = absolutePath.toString();
        SavePath = absoluteDiskPath;
        //new一个map集合出来
        Map<String, String> resultMap = new HashMap<>();
        try
        {
            log.info("保存开始！");
            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            // 重构文件名称
            System.out.println("文件保存路径" + SavePath);
            //UUID(全局唯一标识符)randomUUID(随机生成标识符)toString(转成字符串)replaceAll(替换字符方法，因为随机生成的里面包括了 - ，这里意思是把 - 全部换成空)
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            //视频名字拼接：唯一标识符加上点，再加上上面的视频后缀也就是MP4之类的。就组成了现在的视频名字，比如这样：c7bbc1f9664947a287d35dd7cdc48a95.mp4
            String newVideoName = pikId + "." + fileExt;
            System.out.println("重构文件名防止上传同名文件：" + newVideoName);
            //保存视频的原始名字
            String videoNameText = file.getOriginalFilename();
            System.out.println("视频原名:" + videoNameText);
            //保存视频url路径地址
            String videoUrl = SavePath + "/" + newVideoName;
            //调用数据库接口插入数据库方法save，把视频原名，视频路径，视频的唯一标识码传进去存到数据库内
            videoUpload.save(videoNameText, videoUrl, newVideoName);
            //判断SavePath这个路径也就是需要保存视频的文件夹是否存在
            File filepath = new File(SavePath, file.getOriginalFilename());
            if (!filepath.getParentFile().exists())
            {
                //如果不存在，就创建一个这个路径的文件夹。
                filepath.getParentFile().mkdirs();
            }
            //保存视频：把视频按照前端传来的地址保存进去，还有视频的名字用唯一标识符显示，需要其他的名字可改这
            File fileSave = new File(SavePath, newVideoName);
            //下载视频到文件夹中
            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称：这里put代表添加进map集合内，和前端的push一样。括号内是前面字符串是键，后面是值
            resultMap.put("newVideoName", newVideoName);
            //正确保存视频成功，则设置返回码为200
            resultMap.put("code", "200");
            //返回视频保存路径
            resultMap.put("VideoUrl", SavePath + "/" + newVideoName);
            //到这里全部保存好了，把整个map集合返给前端
            log.info("保存成功！");
            return resultMap;

        }
        catch (Exception e)
        {
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
            //返回有关异常的详细描述性消息。
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("code", "400");
            //这时候错误了，map里面就添加了错误的状态码400并返回给前端看
            return resultMap;

        }
    }
}