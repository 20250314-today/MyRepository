<template>
  <div class="bg" >
<div class="login">
  <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules" >
    <div style="margin:20px 0;text-align: center;font-size: 20px;font-weight: bold"> 欢 &nbsp;迎&nbsp;注&nbsp;册</div>
    <el-form-item  prop="username">
      <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" size="large" placeholder="请输入账号"/>
    </el-form-item>
    <el-form-item prop="password">
      <el-input v-model="data.form.password" autocomplete="off" prefix-icon="Lock" size="large" show-password  placeholder="请输入密码"/>
    </el-form-item>
    <el-form-item prop="confirmPassword">
      <el-input v-model="data.form.confirmPassword" autocomplete="off" prefix-icon="Lock" size="large" show-password  placeholder="确认密码"/>
    </el-form-item>
    <div>
      <el-button  size="large" type="primary" style="width:100%;background-color: #248243;border-color: #248243" @click="register">注册</el-button>
    </div>
    <div style="text-align: right;margin-top:5px">返&nbsp;回&nbsp;<a style="color:#248243" href="/login">登录</a></div>
      </el-form>
</div>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import request from '@/utils/request';  // 假设 request 的路径正确
const validatePass = (rule, value, callback) => {
  if (value !== data.form.password) {
    callback(new Error("密码不一致！"))
  } else {
    callback()
  }
}
const data=reactive({
  form:{},
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 5, message: '账号最少五位', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        confirmPassword:[
            {validator:validatePass,trigger: 'blur'},
          {required: true, message: '确认密码', trigger: 'blur'},
            ]
      }
}
)
const formRef =ref()
const register=()=>{
  formRef.value.validate((valid)=>{
    if(valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem("code_user", JSON.stringify(res.data || {}))
          ElMessage.success('注册成功')
          router.push('/')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
      })
}

</script>
<style scoped>
.bg{
  width:100%;
  height:100vh;
  overflow: hidden;
  background-image: url("@/assets/img/2.jpeg");
  background-size: cover;
  align-items: center;
  display: flex;
  justify-content: center;
}
.login{
  width: 350px;
  background-color: #cef7f7 ;
  opacity:0.7;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
  padding:40px 20px;
}
</style>