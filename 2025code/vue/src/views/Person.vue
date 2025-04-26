<template>
<div class="card" style="width:50%">
<div style="font-size: 15px;text-align: center">个人中心</div>
  <el-form ref="formRef" :model="data.user" label-width="80px" style="padding:20px 30px 10px 0">
    <el-form-item label="账号" prop="username">
      <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号"/>
    </el-form-item>
    <el-form-item label="名称" prop="name">
      <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入名称"/>
    </el-form-item>
    <el-form-item label="电话" prop="phone" >
      <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话"/>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱"/>
    </el-form-item>
    <el-form-item label="头像" prop="avatar">

      <el-upload
          list-type="picture"
          action="http://localhost:9999/files/upload"
          :headers="{token:data.user.token}"
          multiple
          :limit="3"
          :on-success="handleFileSuccess">
        <!--              :file-list="[{name: 'name', url: 'url'}, {name: 'name2', url: 'url'}]">-->
        <el-button size="small" type="primary">点击上传</el-button>
        <!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
      </el-upload>
    </el-form-item>
  </el-form>
  <div style="text-align: center">
    <el-button type="primary" style="padding:20px 30px" @click="update">保存</el-button>
  </div>
</div>
</template>
<script setup>
import {reactive} from "vue";
import request from '@/utils/request.js';
import {ElMessage} from "element-plus";
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
})
const handleFileSuccess=(res) =>{
  data.user.avatar=res.avatar
}
const update = () =>{
  let url
  if(data.user.role==='ADMIN'){
    url='/admin/update'
  }
  if(data.user.role==='USER'){
    url='/user/update'
  }
  request.post(url,data.user).then(res=>{
    if(res.code ==='200'){
      ElMessage.success('更新成功')
      localStorage.setItem("code_user",JSON.stringify(data.user))
      emit ('updateUser')
    }

  })
}

const emit = defineEmits(['updateUser'])
</script>
<style>

</style>