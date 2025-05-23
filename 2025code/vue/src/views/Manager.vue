<template>
  <div>
<!--    头部区域开始-->
    <div style="height:60px;display:flex; ">
      <div style="width:240px;display:flex;align-items:center;padding-left: 20px;background-color: #3a5a6b" >
<!--        居中-->
        <img src="@/assets/img/计算器.png" style="width:30px;height:30px;border-radius: 50%" alt="">
        <span style="font-size:20px;font-weight:bold;color:#ffffff">在线教育平台</span>
      </div>
      <div style="flex:1;border-bottom: 1px solid #ddd;display:flex;align-items:center">
        <span  style="margin-left: 10px;cursor:pointer;margin-right:10px" @click="router.push('/manager/home')">
          首页</span> / <span style="margin-left: 10px">{{router.currentRoute.value.meta.name}}

      </span></div>
      <div style="width:fit-content;display:flex;align-items : center;padding-right: 20px;border-bottom: 1px solid #ddd">
        <div style="float: right;margin-right: 3%;margin-top: 10px;" v-if="data.user.role ==='USER'">
          <el-button type="success" @click="router.push('/studentweb/home')">返回首页</el-button>
        </div>
        <el-dropdown style="margin-left:10px">
          <div style="display: flex;align-items:center">
            <img v-if="data.user?.avatar" style="width:40px;height:40px;border-radius: 50%" :src="data.user?.avatar"/>
            <img v-else style="width:40px;height:40px;border-radius: 50%"
                 src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="margin-left: 5px">{{ data.user?.name }}</span>
          </div>
<template #dropdown>
  <el-dropdown-menu>
    <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
    <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
    <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
  </el-dropdown-menu>
</template>
        </el-dropdown>

      </div>
    </div>
<!--    头部区域结束-->

<!--    下方区域开始-->
<div  style="display: flex;" >
<!--  菜单区域开始-->
  <div  style="width:240px;" >
    <el-menu router :default-openeds="['1','2']"
             :default-active="router.currentRoute.value.path" style="min-height:calc(100vh - 60px)">
<!--      default-active="router.curruntRouter.value.path":动态获取当前展示的路由-->
<!--      default-openeds:展开二级菜单-->
      <el-menu-item index="/manager/home">
        <el-icon><House /></el-icon>
        <span>后台首页</span>
      </el-menu-item>
      <el-sub-menu index="1" >
        <template #title>
          <el-icon><Monitor /></el-icon>
          <span>信息管理</span>
        </template>
        <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>
        <el-menu-item index="/manager/notice" v-else>公告信息</el-menu-item>
        <el-menu-item index="/manager/category" >笔记分类</el-menu-item>
        <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manager/classadmin">班级管理</el-menu-item>
        <el-menu-item v-if="data.user.role === 'USER'" index="/manager/introduction">学习笔记</el-menu-item>
<!--        <el-menu-item index="/manager/study">在线学习</el-menu-item>-->
        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/studenthomework">作业管理</el-menu-item>
        <el-menu-item index="/manager/teacherupvideo">课程</el-menu-item>
        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/scoremanagement">练习</el-menu-item>
<!--        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/examine">试卷管理</el-menu-item>-->
        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/questionmanagement">问答社区</el-menu-item>
<!--        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/upload">上传视频</el-menu-item>-->
        <el-menu-item v-if="data.user.role !== 'TEACHER'" index="/manager/apply">审核</el-menu-item>
        <el-menu-item v-if="data.user.role === 'TEACHER'" index="/manager/teacherapplicant">审核</el-menu-item>
        <el-menu-item v-if="data.user.role !== 'ADMIN'" index="/manager/book">书籍推荐</el-menu-item>
<!--        <el-menu-item index="/manager/chat">AI聊天</el-menu-item>-->
        <el-menu-item index="/manager/deepseekchat">AI聊天</el-menu-item>
<!--        <el-menu-item index="/chat">聊天</el-menu-item>-->
      </el-sub-menu>
      <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
        <template #title>
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </template>
        <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
        <el-menu-item index="/manager/user">学生信息</el-menu-item>
        <el-menu-item index="/manager/teacher">教师信息</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
<!--  菜单区域结束-->

<!--  数据渲染区域开始-->
  <div style="cflex:1;margin:10px;background-color: #f2f4ff;padding:10px">
    <RouterView @updateUser="updateUser"/>
  </div>
<!--  数据渲染区域结束-->



</div>
<!--    下方区域结束-->

<!--    <RouterView />-->
<!--渲染子组件-->
  </div>
</template>
<script setup>
import router from "@/router/index.js";
import {reactive} from "vue"
import {ElMessage} from "element-plus";
const data=reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData:[],
})
const logout=()=>
{
  localStorage.removeItem('code_user')
  location.href='/login'
}
const loadIntroduction=()=>{
  request.get('introduction/selectAll').then(res=>{
    if(res.code === '200'){
      data.introductionData=res.data
    }
    else{
    ElMessage.error(res.msg)
    }
  })
}
const updateUser =()=>{
  data.user = JSON.parse(localStorage.getItem("code_user")||'{}')
}
</script>
<style>
.el-menu{
  background-color: #3a5a6b;
  border: none ;
  box-shadow: 0 0 8px rgba(0,0,0,.12);

}

.el-sub-menu__title{
  color:#ddd;
  background-color: #3a5a6b;
}
.el-menu-item{
  color:#ddd;
  height:50px;
}
.el-menu .is-active{
  background-color: #3a6b53;
  color:#fff;
}
.el-sub-menu__title:hover{
  background-color: #3a5a6b;
}
.el-menu-item:not(.is-active):hover{

  background-color: #3a6b3b;
  color:#333;
}
.el-tooltip__trigger{
  outline:none;
}
.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}

</style>