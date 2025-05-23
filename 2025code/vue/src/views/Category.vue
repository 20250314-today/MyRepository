<template>
  <div>
    <div class="card" style="margin-bottom:5px">
      <el-input :prefix-icon="Search" clearable @clear="load" placeholder="请输入标题" v-model="data.title" style="width:240px;
      margin-right: 5px" ></el-input>
      <el-button type="primary" @click="load">搜 索</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </div>
    <div class="card" style="margin-bottom:5px" >
      <el-button type="success"  @click="handleAdd">新增</el-button>

      <el-button type="info" @click="exportData">导出</el-button>
      <el-upload
          action="http://localhost:9999/category/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
          style="display: inline-block;margin:0 10px 0 10px"
      >
        <el-button type="primary" > 导入</el-button>
      </el-upload>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>
      <div class="card" style="margin-bottom:5px" >
        <el-table :data="data.tableData" style="width: 100% "  @selection-change="handleSelectionChange"
                  :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
          <!--        批量选择-->
          <el-table-column prop="title" label="笔记标题" width="180" />
          <el-table-column label="操作"width="100" >
            <template #default="scope" >
              <el-button   icon="Edit" type="primary" circle @click="handleEdit(scope.row)"></el-button>
              <el-button  icon="Delete" type="danger" circle @click="del(scope.row.id)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total,sizes,prev, pager, next,jumper"
          :total="data.total"
          :page-sizes="[5,10,20]"
          @current-change="load"
          @size-change="load"
      />
    </div>
    <el-dialog v-model="data.formVisible" title="笔记信息" width="500" destroy-on-close>
      <!--     destroy-on-close 重置dialog -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding:20px 30px 10px 0">
        <el-form-item label="笔记标题" prop="title">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入笔记标题"/>
        </el-form-item>
<!--        <el-form-item label="公告内容" prop="content">-->
<!--          <el-input type="textarea" rows="4" v-model="data.form.content" autocomplete="off" placeholder="请输入公告内容"/>-->
<!--        </el-form-item>-->
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const formRef =ref()
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  title:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  rules:{
    title:[
      {required:true,message:'请输入笔记标题',trigger:'blur'}
    ],
  }
})
const load=() =>{
  request.get('/category/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title,
      username:data.username
    }
  }).then(res =>{
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
load()
const handleAdd=()=>{
  data.form={}
  data.formVisible=true
}
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/category/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('添加成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const save=()=>{
  // data.form.id?update():add()
  formRef.value.validate(valid=>{
    if(valid){
      data.form.id?update():add()
    }
  })
}
const del = (id) => {
  ElMessageBox.confirm(
      '删除后无法恢复，是否删除该行数据？',
      '提示',  // 修正标题描述
      {
        type: "warning",
        confirmButtonText: "确定",  // 补充确认按钮文本
        cancelButtonText: "取消"    // 补充取消按钮文本
      }
  ).then(res => {
    request.delete('/category/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {});
}
const handleEdit=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))//深度拷贝数据
  data.formVisible=true
}
const update=()=>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/category/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const reset=()=>{
  data.title=null
  load()
}
</script>
<style>

</style>