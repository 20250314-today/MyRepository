<template>
  <div class="bg" ref="vantaRef">
<div class="login" >
  <el-form ref="formRef" :model="data.form" :rules="data.rules" >
    <div style="margin:20px 0;text-align: center;font-size: 20px;font-weight: bold"> 欢 &nbsp;迎&nbsp;登&nbsp;录</div>
    <el-form-item  prop="username">
      <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" size="large" placeholder="请输入账号"/>
    </el-form-item>
    <el-form-item prop="password">
      <el-input v-model="data.form.password" autocomplete="off" prefix-icon="Lock" size="large" show-password  placeholder="请输入密码"/>
    </el-form-item>
    <el-form-item>
      <el-select size="large" style="width:100% " v-model="data.form.role">
        <el-option

            label="管理员"
            value="ADMIN">
        </el-option>
        <el-option

            label="学生"
            value="USER">
        </el-option>
        <el-option

            label="教师"
            value="TEACHER">
        </el-option>
      </el-select>
    </el-form-item>
    <div>
      <el-button size="large" type="primary" style="width:100%" @click="login">登录</el-button>
    </div>
    <div style="text-align: right;margin-top:5px">还没有账号?请<a style="color:#0095ff" href="/register">注册</a></div>
      </el-form>
</div>
  </div>
</template>
<script setup>
import {reactive,ref,onMounted, onBeforeUnmount} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import request from '@/utils/request';  // 假设 request 的路径正确
import * as THREE from 'three'
import BIRDS from 'vanta/src/vanta.birds'
import {useStore} from "vuex";
import Cookies from 'js-cookie'
const store = useStore()
const vantaRef = ref();
let vantaEffect = null;
const data=reactive({
      user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
      form:{role:'ADMIN'},
         rules: {
           username: [
             {required: true, message: '请输入账号', trigger: 'blur'},
              {min: 5, message: '账号最少五位', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
      }
}
)
const formRef =ref()
const login=()=>{
  formRef.value.validate((valid)=>{
    if(valid){
      request.post('/login',data.form).then(res=>{
        if (res.code === '200') {
          const token = res.data.token;
         // store.commit('setToken', token); // 存入Vuex
//localStorage.setItem('token', token); // 持久化存储
          const userId = res.data.id

         // console.log('用户id1：'+ userId)
          Cookies.set('classId',res.data.classId)
        //  console.log('classId：'+res.data.classId)
        //  console.log('用户id2：'+ res.data.id)
        //  console.log('登录响应数据:', res.data);
          localStorage.setItem("code_user",JSON.stringify(res.data || {}))
          ElMessage.success('登录成功')
          // router.push('/')
          if("USER" === res.data.role){
            Cookies.set('userId',userId)
            location.href = '/studentweb/home'
          }
          else if("ADMIN" === res.data.role){
            Cookies.set('adminId',userId)
            router.push('/')
          }
          else if("TEACHER" === res.data.role){
            Cookies.set('teacherId',userId)
            router.push('/')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const initVanta = () => {
  vantaEffect = BIRDS({
    el: vantaRef.value,
    THREE: THREE,
    mouseControls: true,
    touchControls: true,
    gyroControls: false,
    backgroundColor: 0xe0dce6,
    baseColor: 0xd9,
    minHeight: 200.0,
    minWidth: 200.0,
    amplitudeFactor: 1.1, //振幅因子
    xOffset: -2, //移动到X轴
    yOffset: -1, //移动到Y轴
    scale: 1.0,
    color1: 0xff0000,
    color2: 0xd1ff
  })
}

const resizeHandler = () => {
  if (vantaEffect) {
    vantaEffect.resize()
  }
}
// 生命周期
onMounted(() => {
  initVanta()
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  vantaEffect?.destroy()
  window.removeEventListener('resize', resizeHandler)
})
</script>
<style>
.bg{
  width:100%;
  height:100vh;
  overflow: hidden;
 /* background-image: url("@/assets/img/2.jpeg");*/
  background-size: cover;
  align-items: center;
  display: flex;
  justify-content: center;
}
.login{
  z-index: 999;
  width: 350px;
  background-color: #cef7f7 ;
  opacity:0.7;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
  padding:40px 20px;
}
</style>