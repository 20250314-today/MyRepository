<template>
  <div>
    <h1 style="color:#303133" >学生在线学习系统--学生端</h1>

    <div>
      <el-menu :default-active="'1'"  class="el-menu-demo" :router="true" mode="horizontal"
               @select="handleSelect">
        <el-menu-item index="/home">首页</el-menu-item>
        <el-submenu index="1">
          <template slot="title">作业管理</template>
          <el-submenu index="1-1">
            <template slot="title">查看作业</template>
            <el-menu-item index="/checkhomework">已完成</el-menu-item>
            <el-menu-item index="/nohomework">未完成</el-menu-item>
          </el-submenu>
          <el-menu-item index="/askandanswer">问答</el-menu-item>
          <el-menu-item index="/studentpractice">练习</el-menu-item>

        </el-submenu>
        <el-menu-item index="/manager/playall">在线学习</el-menu-item>
        <el-menu-item index="/studentmanagement">班级管理</el-menu-item>
        <el-menu-item index="/applicanthistory">申请记录</el-menu-item>
        <el-menu-item index="/practicedetail">习题提交记录</el-menu-item>
        <el-menu-item index="/essentiainfo">基本信息</el-menu-item>
<!--        <div class="cn">-->


<!--          <div class="blockl">-->

<!--            <el-submenu index="2">-->
<!--              <template slot="title">-->
<!--                <div class="demo-fit">-->
<!--                  <div class="block">-->
<!--                    <i  style="font-size: 50px;color: #ffffff" class="el-icon-user-solid"></i>-->

<!--                    &lt;!&ndash;                                            <el-avatar shape="square" :size="50" :fit="fit" :src="url"></el-avatar>&ndash;&gt;-->


<!--                  </div>-->
<!--                </div>-->
<!--              </template>-->
<!--              <el-menu-item index="/checkhomework" @click="logout()">退出</el-menu-item>-->
<!--              <el-menu-item @click="change()">修改密码</el-menu-item>-->
<!--            </el-submenu>-->
<!--          </div>-->
<!--        </div>-->
      </el-menu>

    </div>


  </div>
</template>

<script setup>
import Cookies from "js-cookie";
const data = reactive( {
      changePassword:{
        password:'',
        newPassword: '',
        id:''
      },
      dialogFormVisible: false,
      info: {
        password: '',
        newPassword: '',
        id: ''
      },
      drawer: false,
})
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