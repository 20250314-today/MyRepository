<template>
  <div >
  <div>
    <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
    >
    </el-menu>
    <div style="margin:0 100px">
      <el-carousel :interval="4000" height="400px" type="card">
        <el-carousel-item v-for="url in data.iData" :key="item">
          <div >
            <router-link   :to="{path:'/detailonlineweb', query:{videoTotalId:url.id,userId:url.userId}}"
                           style="display: block;"
            >
              <img :src="url.coverUrl" alt="" style="height:300px;width:400px">
              <!--              <el-image :src=url.coverUrl ></el-image>-->
            </router-link>
            <h3>{{url.topic}}</h3>
            <div class="ta">
              {{url.userName}}
              <!-- <el-tag> {{url.createTime}}</el-tag> -->
            </div>

          </div>

        </el-carousel-item>
      </el-carousel>
      <div class="dem">


      </div>
    </div>
  </div>
   <div style="background-color:#d8e2ec;min-height: 1000px">
     <div style="width:80%;margin:20px auto">
     <div style="font-size: 20px;border-left:5px solid #2fbd67;padding-left: 5px;height:30px;line-height: 30px">学习笔记</div>
     <div style="margin-top:20px">
       <el-row :gutter="10">
         <el-col :span="6" v-for="item in data.introductionData"
                 style="margin: 20px;background-color: #ffffff;border-radius: 5%;padding:10px">

<!--           <img :src="item.img" alt="" style="width:100%;height:260px;border-radius: 5%">-->
           <div v-html="item.content"></div>
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
   </div>
     <div style="width:80%;margin:20px auto">
       <div style="font-size: 20px;border-left:5px solid #2fbd67;padding-left: 5px;height:30px;line-height: 30px">书籍推荐</div>
       <div style="margin-top:20px">
         <div style="display: flex;margin-top:20px;grid-gap: 20px" v-for="item in data.book">
           <div style="flex:1">
             <img @click="navTo('/front/introductionDetail?id='+ item.id)" :src="item.img" alt="" style="width:100%;height:240px;display: block;cursor: pointer">
           </div>
           <div style="flex:3">
             <div style="font-weight: bold;font-size: 16px">{{item.name}}</div>
             <div style="display: flex;align-items: center;margin-top:10px;grid-gap: 10px">
               <div style="color:#666666">作者：{{item.author}}</div>
             </div>
             <div class="line" style="margin-top:10px;text-align: justify">简介：<span style="display:inline-block" v-html="item.description"></span></div>
           </div>
         </div>
       </div>
     </div>
   </div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import { ref, onMounted } from 'vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  introductionData:[],
  book:[],
  page:{
    page:1, //初始页
    pageSize:10,    //    每页的数据
    classId:''
  },
  iData:[],
  videos:[],
  videoss:{
    videoTotalId:''
  },
  course:{
    classId:''
  },
  onLineCou:[],
  total:0
})
const loadIntroduction=() =>{
  request.get('/introduction/selectAll').then(res =>{
    console.log("introduction:",res)
    if (res.code === '200') {
     data.introductionData=res.data
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
const loadBook=() =>{
  request.get('/book/selectAll').then(res =>{
    console.log("introduction:",res)
    if (res.code === '200') {
      data.book=res.data
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
loadBook()
loadIntroduction()
const logout=()=>
{
  localStorage.removeItem('code_user')
  location.href='/login'
}
const navTo=(url)=>{
  location.href =url;
}
// 获取图片数据
onMounted(async () => {
  data.page.classId=parseInt(data.user.classId)
  listAllStudentsScore(data.page)
});
const listAllStudentsScore = (page)=>{
  request.post('/study/videoTotal/pageVideo',page).then(resp=>{
    console.log("resp:",resp)
    data.iData=resp.data.records
    console.log("token3:",data.user.token)
    data.page.total = resp.data?.total || 0;
  })
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