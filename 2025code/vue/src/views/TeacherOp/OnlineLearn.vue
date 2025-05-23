<template>
  <div >

    <div class="course" v-for="c in onLineCou">
      <el-button >{{c.subjectName}}</el-button>
    </div>
    <div class="dem">

      <div class='demo' v-for="url in iData">
        <div >
          <router-link   :to="{path:'/detailonlineweb', query:{videoTotalId:url.id,userId:url.userId}}"
                         style="display: block;"
          >
            <el-image :src=url.coverUrl ></el-image>
          </router-link>
          <h3>{{url.topic}}</h3>
          <div class="ta">
            {{url.userName}}
            <!-- <el-tag> {{url.createTime}}</el-tag> -->
          </div>

        </div>

      </div>

    </div>
    <div style="height: 40px"></div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total">
    </el-pagination>
  </div>
</template>
<script>
import Cookies from "js-cookie";
import request from "@/utils/request.js"
export default {
  name: "OnLineLearn",
  data() {
    return {
      user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
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

    }
  },
  created() {
    this.page.classId=parseInt(this.user.classId)
    this.course.classId=parseInt(this.user.classId)
    console.log("class:",this.user.classId)
    this.listAllCourse(this.course)
    this.listAllStudentsScore(this.page)
  },
  methods:{
    detailvideo(video){
      this.videoss.videoTotalId=video
      request.post('/study/videos/selectByVideoTotalId',this.videos).then(resp=>{
        this.videos=resp.data
        console.log("token:",this.user.token)
      })
    },
    handleSizeChange(size) {
      this.page.pageSize = size;
      this.listAllStudentsScore(this.page)
      // console.log(this.pageSize,'888')

      console.log(`每页 ${size} 条`);
    },
    handleCurrentChange(pageNum) {
      this.page.page = pageNum;
      this.listAllStudentsScore(this.page)
      console.log(`当前页: ${pageNum}`);
    },

    listAllCourse(page){
      request.post('/study/subject/queryList',page).then(resp=>{
        this.onLineCou=resp.data
        console.log("token2:",this.user.token)
      })
    },
    listAllStudentsScore(page){
      request.post('/study/videoTotal/pageVideo',page).then(resp=>{
        console.log("resp:",resp)
        this.iData=resp.data.data
        console.log("token3:",this.user.token)
        this.page.total = resp.data?.total || 0;
      })
    }
  }

}
</script>
<style scoped>
.el-image__inner {
  vertical-align: top;
  width: 200px;
  height: 100px;
}
.demo{
  width: 255px;
  text-align: center;
  line-height: 15%;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
  margin-right: 4px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-top: 20px;

}
el-button{
  display: inline-block;
}

.course{
  display: inline-block;
}
.ta{
  display: flex;
  justify-content: space-between;
  height: 30px;
  line-height: 30px;
}
.dem{
  display: flex;
  justify-content:flex-start;

  flex-wrap: wrap;
}

.demo:hover{
  display: block;
}
</style>