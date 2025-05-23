<template>
  <div>
    <div class="card" style="margin-bottom:5px">
      <el-input :prefix-icon="Search" clearable @clear="load" placeholder="请输入班级名称" v-model="data.title"
                style="width:240px;margin-right: 5px" ></el-input>
      <el-button type="primary" @click="load">搜 索</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </div>
    <div class="card" style="margin-bottom:5px" >
      <el-button type="success"  @click="handleAdd">新增</el-button>

      <el-button type="info" @click="exportData">导出</el-button>
      <el-upload
          action="http://localhost:9999/class/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
          style="display: inline-block;margin:0 10px 0 10px"
      >
        <el-button type="primary" > 导入</el-button>
      </el-upload>
    </div>
    <div class="card" style="margin-bottom:5px" >
      <el-table :data="data.tableData" style="width: 100% "  @selection-change="handleSelectionChange"
                :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column type="selection" width="55" />
        <!--        批量选择-->

        <el-table-column prop="className" label="班级名称" width="180" />
        <el-table-column prop="userName" label="教师" width="180" />

        <el-table-column prop="time" label="发布时间" />
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
    <div>
      <el-dialog v-model="data.formVisible" title="班级名称" width="60%" destroy-on-close>
        <!--     destroy-on-close 重置dialog -->
        <el-form ref="formRef" :model="data.form" label-width="80px" style="padding:20px 30px 10px 0">

          <el-form-item label="班级名称" prop="className">
            <el-input v-model="data.form.className" autocomplete="off" placeholder="请输入班级名称"/>
          </el-form-item>
          <el-form-item label="教师" prop="teacher">
            <el-select
                v-model="data.form.userId"
                placeholder="请选择教师"
                size="large"
                style="width: 240px"
            >
              <el-option
                  v-for="item in data.categoryData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="data.formVisible = false">取消</el-button>
            <el-button type="primary" @click="save">保存</el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog v-model="data.viewVisible" title="班级信息" width="60%" destroy-on-close>
        <div v-html="data.content" style="padding :0 10px">

        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {Search} from "@element-plus/icons-vue";
import Cookies from "js-cookie"
import {reactive, onBeforeUnmount, ref, shallowRef, onMounted} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  pageNum:1,
  pageSize:5,
  total:0,
  name:null,
  className:null,
  tableData:[],
  categoryData:[],
  form:{},
  formVisible:false,
  content:null,
  viewVisible:false
})
const loadCategory = () =>{
  request.get('/teacher/selectAll').then(res=>{
    if(res.code === '200'){
      data.categoryData = res.data
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()
const viewContent=(content)=>{
  data.content=content
  data.viewVisible=true

}
const load=() =>{
  request.get('/class/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
      form:{},
      formVisible:false,
    }
  }).then(res =>{
    console.log("adminclass:",Cookies.get("classId"))
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
const handleEdit=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))//深度拷贝数据
  data.formVisible=true
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
    request.delete('/class/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {});
}
const reset=()=>{
  data.name=null
  load()
}
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/class/add', data.form).then(res => {
        if (res.code === '200') {
          Cookies.set('classId',res.data.form.classId)
          console.log("adminclass:",Cookies.get("classId"))
          data.formVisible = false
          ElMessage.success('保存成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const handleAdd=()=>{
  data.form={}
  data.formVisible=true
}
const save=()=>{
  // data.form.id?update():add()
  formRef.value.validate(valid=>{
    if(valid){
      data.form.id?update():add()
    }
  })
}

const update = () =>{
  request.post('/class/update',data.form).then(res=>{
    if(res.code ==='200'){
      ElMessage.success('更新成功')
      data.formVisible=false
      load()
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
const formRef=ref()
</script>
<style>

</style>