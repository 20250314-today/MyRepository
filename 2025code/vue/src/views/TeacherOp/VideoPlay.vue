<template>
  <div class="release_wrap" style="width:1000px">
    <div class="release_title">播 放 视 频</div>
    <el-card class="release_card">
      <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
                 @click="jump_home">返回首页</el-button>

      <el-table stripe :data="data.tableData" style="width: 100%" height="600px">
        <el-table-column prop="videoName" label="视频名称" min-width="280">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="playVideo(scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :modal="false" title="视频播放" :visible.sync="data.dialogVisible" width="40%">
      <p class="video_title">{{ data.videoName }}</p>
      <video :src="data.videoUrl" controls="controls" width="100%" @canplay="getVidDur()" id="myvideo"></video>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref,onMounted,computed,} from "vue";
import { useRouter } from 'vue-router'
import request from "@/utils/request.js"
const router = useRouter()
const video = () => {
  let videoTime = document.getElementById("myvideo");
  console.log(videoTime.duration); //获取视频时长
  console.log(videoTime.currentTime); //获取视频当前播放时间
};

// const baseUrl = request.baseUrl;
 const  data = reactive( {
      title: "",
      videolist: "",
      //表格数据
      tableData: [],
      //弹框组件隐藏
      dialogVisible: false,
      //用于保存视频的id
      videoId: 0,
      //保存视频的名称
      videoName: '',
      videoUrl: '',
  })
// 生命周期
onMounted(async () => {
  await getVideoInfo()
})
const jump_home = () =>{
      router.push('/manager/upload')
    }
const getVidDur = () =>{
      video();
    }
    //获取video表格数据
const   getVideoInfo = () =>{
      request.get( 'http://localhost:9999/SelectVideo/table').then((res) => {
        console.log('res:', res)
        data.tableData = res.data;
      });
}
const  playVideo = (val) =>{
      // 显示弹框
      data.dialogVisible = true;
      // 保存视频名字
      data.videoName = val.videoName;
      // 保存视频id
      data.videoId = val.id;
      // 发送HEAD请求获取视频的总大小
     request.get('http://localhost:9999/SelectVideo/getVideoSizeById/' + data.videoId).then(res => {
        const totalSize = res.data;
        const chunkSize = Math.ceil(totalSize / 20); // 设置分片大小为总大小的1/5

        // 定义分片传输的函数
const loadVideoChunk = (startByte, endByte) => {
          return new Promise((resolve, reject) => {
           request.get('http://localhost:9999/SelectVideo/policemen/' + data.videoId, {
              headers: {
                Range: `bytes=${startByte}-${endByte}`
              },
              responseType: 'blob'
            }).then(response => {
              // 返回获取到的视频分片数据
              resolve(response.data);
            }).catch(error => {
              reject(error);
            });
          });
        };

        // 创建一个数组来保存所有分片的Promise
        const chunkPromises = [];

        // 获取所有分片的Promise
        for (let i = 0; i < 20; i++) {
          const startByte = i * chunkSize;
          const endByte = Math.min(startByte + chunkSize - 1, totalSize - 1);
          chunkPromises.push(loadVideoChunk(startByte, endByte));
        }

        // 执行所有分片请求，并在全部请求完成后开始播放视频
        Promise.all(chunkPromises).then(chunks => {
          // 将分片数据合并成完整的视频Blob
          const videoBlob = new Blob(chunks);
          const videoUrl = URL.createObjectURL(videoBlob);
          this.videoUrl = videoUrl;
        }).catch(error => {
          console.error('Failed to load video:', error);
        });

      }).catch(error => {
        console.error('Failed to get video size:', error);
      });

    }
</script>

<style>

</style>