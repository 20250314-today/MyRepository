<template>
  <div>
    <h1 style="color:#303133" >学生在线学习系统--学生端</h1>
    <div>
      <el-menu :default-active="'1'"  class="el-menu-demo" :router="true" mode="horizontal">
        <el-menu-item  @click="router.push('/manager/user')">系统首页</el-menu-item>
        <el-menu-item index="/studentweb/home">首页</el-menu-item>
        <el-sub-menu index="1">
          <template #title>作业管理</template>
          <el-sub-menu index="1-1">
            <template #title>查看作业</template>
            <el-menu-item index="/studentweb/checkhomework">已完成</el-menu-item>
<!--            <el-menu-item index="/studentweb/nohomework">未完成</el-menu-item>-->
          </el-sub-menu>
          <el-menu-item index="/studentweb/askandanswer">问答</el-menu-item>
          <el-menu-item index="/studentweb/studentpractice">练习</el-menu-item>

        </el-sub-menu>
        <!-- 其他顶层菜单 -->
        <el-menu-item index="/studentweb/playall">在线学习</el-menu-item>
<!--        <el-menu-item index="/studenthome/studentmanagement">班级管理</el-menu-item>-->
        <el-menu-item index="/studentweb/apply">申请记录</el-menu-item>
        <el-menu-item index="/studentweb/practicedetail">习题提交记录</el-menu-item>
        <el-menu-item @click="logout">退出</el-menu-item>
      </el-menu>

    </div>
    <div style="cflex:1;margin:10px;background-color: #f2f4ff;padding:10px">
      <RouterView @updateUser="updateUser"/>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";

const updateUser =()=>{
  data.user = JSON.parse(localStorage.getItem("code_user")||'{}')
}
const logout=()=>
{
  localStorage.removeItem('code_user')
  location.href='/login'
}
</script>

<style scoped>
/*.blockl{*/
/*    display: flex;*/
/*    justify-content: space-between;*/
/*}*/
h1 {
  /* position: absolute; */
  margin-left: 40%;
}
.el-menu--horizontal>.el-submenu .el-submenu__title {
  height: 60px;
  line-height: 60px;
  border-bottom: 2px solid transparent;
  color: #ffffff !important;
  font-size: 20px !important;
}


.blockl {
  position: absolute;
  right: 0px;
}

.el-menu {
  border-right: solid 1px #e6e6e6;
  list-style: none;
  position: relative;
  margin: 0;
  padding-left: 0;
  background-color: #babae4;
}
.el-menu--horizontal>.el-menu-item[data-v-15228138] {
  float: left;
  height: 60px;
  line-height: 60px;
  margin: 0;
  border-bottom: 2px solid transparent;
  color: #ffffff;
  background-color: #babae4;
  font-size: 20px;
}
.el-menu--horizontal>.el-submenu .el-submenu__title {
  height: 60px;
  line-height: 60px;
  border-bottom: 2px solid transparent;
  color: #ffffff;
  font-size: 20px;
}
.cn {
  display: flex;
  justify-content: space-between;
}

.title {
  line-height: 100%;
}
</style>