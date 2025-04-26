<template>
 <div style="width:1000px">
   <el-form ref="form" :model="state.videoForm" label-width="80px">
     <div class="release_title"><h1 style="color: red">本页为不分片上传视频，去分页上传请点击下方按钮</h1></div>
     <el-form-item label="">
       <el-upload class="avatar-uploader el-upload--text" :drag="state.Plus" :action="state.uploadUrl" multiple :show-file-list="false"
                  :data="{ SavePath:state.Path.url }" :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo"
                  :on-progress="uploadVideoProcess">
         <i v-if="state.Plus" class="el-icon-upload"></i>
         <div v-if="state.Plus" class="el-upload__text">
           将文件拖到此处，或<em>点击上传</em>
         </div>
         <el-progress v-if="state.videoFlag == true" type="circle" :percentage="state.videoUploadPercent"
                      style="margin-top: 30px"></el-progress>
         <div class="el-upload__tip" slot="tip">
           只能上传mp4/flv/avi文件，且不超过5000M
         </div>
       </el-upload>
     </el-form-item>
     <el-form-item>
       <el-button type="primary" @click="goUploadSplit">去分片上传页面(推荐)</el-button>
       <el-button type="primary" @click="onSubmit">去分片播放页面</el-button>
       <el-button type="primary" @click="goPlayAll">去单片播放页面（推荐）</el-button>
       <el-button type="primary" @click="goPlayMerge">去边放边播播放页面(未完成)</el-button>
     </el-form-item>
   </el-form>
 </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from "@/utils/request.js";
const baseUrl = request.baseUrl;
// const baseUrl = request.baseUrl;
const router = useRouter()

// 响应式状态
const state = reactive({
  uploadUrl: `http://localhost:9999/api/uploadVideo3`,
  videoForm: {
    videoId: '',
    videoUrl: ''
  },
  videoFlag:false,
  Plus: true,
  Path:{ url:'D:/jliason/0723/videos'},
  allowedTypes: [
    'video/mp4',
    'video/ogg',
    'video/webm',
    'video/x-flv',
    'video/x-msvideo'
  ],
  videoUploadPercent:0,
  maxSizeMB: 5000 // 可配置的最大文件大小
})
const goUploadSplit = () =>{
  router.push('/manager/uploadsplit')
}
const goPlayAll = () =>{
 router.push('/manager/playall')
}
const goPlayMerge = () =>{
  router.push('/manager/playmerge')
}
//视频部分
// 视频上传前执行
const beforeUploadVideo = (file) =>{
  //文件大小
  const isLt300M = file.size / 1024 / 1024 < 5000
  //视频后缀检查
  if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(file.type) === -1) {
    ElMessage.error('请上传正确的视频格式')
    return false
  }
  if (!isLt300M) {
    ElMessage.error('上传视频大小不能超过5000MB哦!')
    return false
  }
}
// 视频上传过程中执行
const uploadVideoProcess = (event, file, fileList) =>{
  state.Plus = false
  state.videoFlag = true
  state.videoUploadPercent = + file.percentage.toFixed(0)
}
// 视频上传成功是执行
const handleVideoSuccess = (res, file) =>{
  state.Plus = false
  state.videoUploadPercent = 100
  console.log(res)
  // 如果为200代表视频保存成功
  if (res.code === '200') {
    // 接收视频传回来的名称和保存地址
    // 至于怎么使用看你啦~
    state.videoForm.videoId = res.newVidoeName
    state.videoForm.videoUrl = res.VideoUrl
    ElMessage.success('视频上传成功！')
  } else {
    ElMessage.error('视频上传失败，请重新上传！')
  }
}

// 提交处理
const onSubmit = async () => {
  // 处理表单提交逻辑
  // 可以使用 this.form 访问表单数据
  // 例如：this.form.videoId、this.form.videoUrl 等
  router.push('/manager/play')
}
</script>

<style></style>
