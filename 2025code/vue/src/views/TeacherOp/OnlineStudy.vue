<template>
  <div class="card" style="width:1070px;position:absolute;left:240px">
    <div style="width:80%;margin:20px auto">
    <div style="font-size: 20px;border-left:5px solid #2fbd67;padding-left: 5px;height:30px;line-height: 30px">旅游攻略</div>
    <div style="margin-top:20px">
      <el-row :gutter="10">
        <el-col :span="6" v-for="item in data.introductionData" style="margin-bottom: 20px">

          <img :src="item.img" alt="" style="width:100%;height:260px;border-radius: 5%">
          <div style="font-size: 15px;margin:10px 0;font-weight: bold">{{item.title}}</div>
          <div style="display: flex;align-items: center;margin:5px">
            <img :src="item.userAvatar" alt="" style="width:30px;height:30px;border-radius:50%">
            <div style="margin:5px 5px">{{item.userName}}</div>

          </div>
          <div>{{item.time}}</div>
          <!--           <img src="@/assets/img/女生打雪仗.png" alt="">-->
        </el-col>
      </el-row>
    </div>
  </div></div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import lun1 from '@/assets/img/lun-2梯田.jpeg'
import lun2 from '@/assets/img/lun-2梯田.jpeg'
import lun3 from '@/assets/img/lun-3福建.jpeg'
import lun4 from '@/assets/img/lun-4紫地.jpeg'
import lun5 from '@/assets/img/lun-5贵州.jpeg'
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  introductionData:[],
  carouselData:[lun1,lun2,lun3,lun4,lun5]
})
const loadIntroduction=() =>{
  request.get('/introduction/selectAll').then(res =>{
    if (res.code === '200') {
      data.introductionData=res.data
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
loadIntroduction()
const logout=()=>
{
  localStorage.removeItem('code_user')
  location.href='/login'
}
const navTo=(url)=>{
  location.href =url;
}
</script>
<style>
.line{
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient:vertical;
  -webkit-line-clamp: 4;
  overflow: hidden;
}
</style>