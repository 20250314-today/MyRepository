<template>
  <div style="width:950px">
    <el-button @click="test()" type="primary"> 发布试题</el-button>
    <el-table
        :data="ScoreData"
        :default-sort = "{prop: 'date', order: 'descending'}"
        border
        style="width: 100%">
      <el-table-column
          sortable
          fixed
          prop="createTime"
          label="发布时间"
          width="180">
      </el-table-column>
      <el-table-column
          sortable
          fixed
          prop="title"
          label="试题名称"
          width="180">
      </el-table-column>
      <el-table-column
          sortable
          fixed
          prop="userName"
          label="发布人"
          width="180">
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">查看详情</el-button>
          <el-button
              size="mini"
              @click="handleHomework(scope.$index, scope.row)">查看已完成情况</el-button>
          <el-button
              size="mini"
              @click="handleNotHomework(scope.$index, scope.row)">查看未完成情况</el-button>
        </template>
      </el-table-column>
    </el-table>



    <el-dialog title="未完成情况" v-model="dialogTableVisible">
      <el-table :data="NotHomework">
        <el-table-column prop="account" label="账号" width="150"></el-table-column>
        <el-table-column prop="userName" label="姓名" width="150"></el-table-column>
        <el-table-column prop="sexName" label="性别" width="200"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.page"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total">
      </el-pagination>
    </el-dialog>


    <el-dialog title="已完成情况" v-model="dialogdoTableVisible">
      <el-table
          :data="Homework"
          style="width: 100%">
        <el-table-column
            label="完成时间"
            width="180">
          <template v-slot="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="replies"
            label="姓名"
            width="180">
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button
                size="mini"
                @click="handle(scope.$index, scope.row)">查看答题情况</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.page"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total">
      </el-pagination>
    </el-dialog>



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
import Cookies from 'js-cookie'
import request from "@/utils/request.js"
export default {
  name: "ScoreManagment",
  data() {
    return {
      NotHomework:[],
      Homework:[],
      ScoreData:[],
      page:{
        page:1, //初始页
        pageSize:10,    //    每页的数据
        userId:'',
        roleId:'',
        exerciseId:'',
        classId:''
      },
      dialogTableVisible:false,
      dialogdoTableVisible:false,

    }
  },
  created() {
    this.page.userId=Cookies.get('userId')
    this.page.roleId=Cookies.get('roleId')
    this.listAllStudentsScore(this.page)

  },

  methods:{
    test(){
      this.$router.push({
        name:'Detail',
        query:{
          type:"试题",
          tag:'test'
        },
      })
    },
    handleHomework(index,row){
      this.page.exerciseId=row.id;
      //Vue.delete(this.page,"userId")
      request.delete(this.page,'userId')
      request.post('/study/userDoExercise/list',this.page).then(resp=>{
        console.log("done:",resp)
        this.Homework=resp.data.data

      })
      this.dialogdoTableVisible=true
    },


    handleNotHomework(index,row){
      this.page.exerciseId=row.id;
      this.page.classId=row.classId
      request.post('/study/user/findNotDoWork',this.page).then(resp=>{
        console.log("notdone:",resp)
        this.NotHomework=resp.data.data

      })
      this.dialogTableVisible=true
    },

    handle(index,row){
      this.$router.push({
        name:'DetailMark',
        params:{
         content:row.reply,
          data3:'888'
        }
      })
    },
    handleEdit(index,row){
      this.$router.push({
        name:'DetailMark',
        params:{
          data1:row.content
        }
      })
    },
    handleSizeChange(size) {
      this.page.pageSize = size;
      this.listAllStudentsScore(this.page)
      // console.log(this.pageSize,'888')

      console.log(`每页 ${size} 条`);
    },
    handleCurrentChange(pageNum) {
      this.page.pageNum = pageNum;
      this.listAllStudentsScore(this.page)
      console.log(`当前页: ${pageNum}`);
    },

    listAllStudentsScore(page){
      request.post('/study/exercises/list',page).then(resp=>{
       // console.log("exer:",resp)
        this.ScoreData= resp.data.data
        this.page.total = resp.data?.total || 0;
      })
    }
  }
}
</script>

<style scoped>

</style>
