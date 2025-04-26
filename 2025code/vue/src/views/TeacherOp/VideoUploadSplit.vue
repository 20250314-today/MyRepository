<template>
  <div style="width: 1000px;">
    <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
               @click="jump_home">返回首页</el-button>
    <uploader :options="data.options" :autoStart="false" :fileStatusText="{
      success: '上传成功',
      error: '上传失败',
      uploading: '正在上传',
      paused: '暂停上传',
      waiting: '等待上传'
    }" @file-success="onFileSuccess" @file-added="fileAdded" @file-error="onFileError" class="uploader-example">
      <uploader-unsupport></uploader-unsupport>
      <uploader-drop>
        <uploader-btn :attrs="data.attrs" single>上传</uploader-btn>
      </uploader-drop>
      <uploader-list></uploader-list>
    </uploader>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import request from "@/utils/request.js";

// const baseUrl = request.baseUrl;
const router = useRouter()
const baseUrl = request.baseUrl;
// 配置参数
const data = reactive({
  options:{
    target:'http://localhost:9999/api/fileUpload',
    chunkSize: 50 * 1024 * 1024, // 50MB分片
    headers:{
      xtoken:localStorage.token
    },
    singleFile:true,
  },
  attrs:{
    accept:'*'
  },
})

// 上传状态
const jump_home = () =>{
  router.push('/manager/upload')
}
//大文件上传标签删除
const handleClose = () =>{
  console.log("handleClose")
}
//大文件上传所需
const fileAdded = (file) =>{
  //选择文件后暂停文件上传，上传时手动启动
  file.pause()

}
const onFileError = (file) =>{
  console.log('error', file)
}
const onFileSuccess = (rootFile, file, response, chunk) =>{
  console.log("上传成功")
}
</script>

<style scoped>
h1,
h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
}

.uploader-list>ul>li {
  width: 100%;
  color: red;
  margin-bottom: 0;
}

a {
  color: #42b983;
}

.fileupload {
  width: 50%;
  margin-left: 25%;
}
</style>
