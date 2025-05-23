<template>
 <div style="width:1000px">
   <el-form ref="form" :model="state.videoForm" label-width="80px">
     <div class="release_title"><h1 style="color: red">本页为不分片上传视频，去分页上传请点击下方按钮</h1></div>
     <el-form-item label="">
       <el-upload class="avatar-uploader el-upload--text" :drag="state.Plus" :action="state.uploadUrl" multiple :show-file-list="false"
                  :data="{ SavePath:state.Path.url }"
                  :on-success="handleVideoSuccess"
                  :before-upload="beforeUploadVideo"
                  :headers="{token:state.user.token}"
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

     <el-form-item label="课程名称" prop="courseName">
       <el-input v-model="state.videoForm.courseName" autocomplete="off" placeholder="请输入课程名称"/>
     </el-form-item>
     <el-form-item label="课程描述" prop="content">
       <div style="border:1px solid #ccc;width:100%">
         <Toolbar
             style="border-bottom: 1px solid #ccc"
             :editor="editorRef"
             :mode="mode"
         />
         <Editor
             style="height:400px;overflow-y:hidden;"
             v-model="state.videoForm.content"
             :mode="mode"
             :defaultConfig="editorConfig"
             @onCreated="handleCreated"
         />
       </div>
       <!--            <el-input type="textarea" rows="4" v-model="data.form.content" autocomplete="off" placeholder="请输入公略内容"/>-->
     </el-form-item>
     <el-form-item label="图片" prop="videoPicture">
       <el-upload
           list-type="picture"
           action="http://localhost:9999/files/upload"
           :headers="{token:state.user.token}"
           multiple
           :limit="3"
           :on-success="handleFileSuccess">
         <!--              :file-list="[{name: 'name', url: 'url'}, {name: 'name2', url: 'url'}]">-->
         <el-button size="small" type="primary">点击上传</el-button>
         <!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
       </el-upload>
     </el-form-item>
     <!-- 操作按钮 -->
     <el-form-item>
         <div class="dialog-footer">
           <el-button @click="state.formVisible = false">取消</el-button>
           <el-button type="primary" @click="save">保存</el-button>
         </div>
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
import {ref, reactive, shallowRef, onBeforeUnmount} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import { useRouter } from 'vue-router'
import request from "@/utils/request.js";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
const editorConfig = {MENU_CONF:{}}
const baseUrl = request.baseUrl;
const editorRef=shallowRef()
const mode='default'
// const baseUrl = request.baseUrl;
const router = useRouter()

onBeforeUnmount(()=>{
  const editor = editorRef.value
  if(editor == null) return
  editor.destroy()
  console.log('token:',data.user.token)

})
const handleCreated=(editor)=>{
  editorRef.value=editor
}
// 响应式状态
const state = reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  courseName:null,
  tableData:[],
  categoryData:[],
  form:{},
  formVisible:false,
  content:null,
  uploadUrl: `http://localhost:9999/api/uploadVideo3`,
  videoForm: {
    videoId: '',
    videoUrl: '',
    courseName:'',
    content:''
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
editorConfig.MENU_CONF['uploadImage']={
  headers:{
    token:state.user.token,
  },
  server:'http://localhost:9999/files/wang/upload',//服务端图片上传接口
  fieldName:'file'
}
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
/*const load=() =>{
  request.get('/introduction/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title,
      form:{},
      formVisible:false,
    }
  }).then(res =>{
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset=()=>{
  data.title=null*/
/*  load()
}*/
const formRef=ref()
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/api/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('保存成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const save=()=>{
  // data.form.id?update():add()
  formRef.value.validate(valid=>{
    if(valid){
      state.form.id?update():add()
    }
  })
}
const handleFileSuccess=(res) =>{
  state.form.img=res.data
}
const update = () =>{
  request.post('/api/update',data.form).then(res=>{
    if(res.code ==='200'){
      ElMessage.success('更新成功')
      state.formVisible=false
      load()
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style></style>
