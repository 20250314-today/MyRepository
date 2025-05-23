<template>
  <div class="release_wrap" style="width:1000px">
    <div class="release_title">播 放 视 频</div>
    <el-card class="release_card">
      <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
                 @click="jump_home">返回首页</el-button>

      <el-table stripe :data="state.tableData" style="width: 100%" height="600px">
        <el-table-column prop="videoName" label="视频名称" min-width="280">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="mini" type="primary" @click="playVideo(scope.$index, scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog  title="视频播放"  width="40%">
      <p class="video_title">{{state.videoName }}</p>
      <video :src="`${state.videoUrl}/${state.videoId}`"
             controls="controls" width="100%"
             @canplay="getVidDur()"
             @error="handleVideoError"
             id="myvideo">
      </video>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from "@/utils/request.js";
import IPconfig from "./IPconfig";
const baseUrl = IPconfig.baseUrl;
const router = useRouter()
// 响应式状态
const state = reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  title:"",
  videolist:"",
  tableData: [],
  dialogVisible: false,
  videoId: 0,
  videoName: '',
  videoElement: null,
  videoUrl: baseUrl + '/SelectVideo/policemen'
})


// 生命周期
onMounted(async () => {
  await getVideoInfo()
})

// 方法
const jump_home = () => {
  router.push('/manager/upload')
}
var video = () => {
  let videoTime = document.getElementById("myvideo");
  console.log(videoTime.duration); //获取视频时长
  console.log(videoTime.currentTime); //获取视频当前播放时间
}
const getVidDur = () =>{
  video();
}

const getVideoInfo = () =>{

  request.get( baseUrl +'/SelectVideo/table').then((res) => {
    //console.log('res:', res)
    state.tableData = res;
  })
}

const playVideo = (i, val) => {
  //显示弹框
  state.dialogVisible = true;
  //保存视频名字
  state.videoName = val.videoName;
  //保存视频id
  state.videoId = val.id;
  console.log(i, val);
  console.log('视频地址:', `${state.videoUrl}/${val.id}`)
 /* state.dialogVisible = true
  state.videoName = val.videoName
  state.videoId = val.id
  console.log(val);
  console.log('视频地址:', `${state.videoUrl}/${val.id}`)*/

  // 直接在新标签页测试（调试用）
  //window.open(`${state.videoUrl}/${val.id}`, '_blank')
}
const handleVideoError = () => {
  console.error('视频加载失败，请检查视频地址或网络连接。');
}
</script>

<style>

</style>
