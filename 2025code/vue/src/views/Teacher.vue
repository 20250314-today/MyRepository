<template>
  <div>
    <div class="card" style="margin-bottom:5px">
      <el-input clearable @clear="load" placeholder="请输入名称" v-model="data.name" style="width:240px;margin-right: 5px"></el-input>
      <el-input  clearable @clear="load" placeholder="请输入账号" v-model="data.username" style="width:240px;margin-right: 5px"></el-input>
      <el-button type="primary" @click="load">搜 索</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </div>
    <div class="card" style="margin-bottom:5px">
          <el-button type="success"  @click="handleAdd">新增</el-button>

          <el-button type="info" @click="exportData">导出</el-button>
      <el-upload
        action="http://localhost:9999/teacher/import"
        :show-file-list="false"
        :on-success="handleImportSuccess"
        style="display: inline-block;margin:0 10px 0 10px"
      >
        <el-button type="primary" > 导入</el-button>
      </el-upload>
          <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>
      <div>
      <div class="card" style="margin-bottom:5px" >
      <el-table :data="data.tableData" style="width: 100% "  @selection-change="handleSelectionChange"
                :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
<!--        :header-cell-style:加粗-->
        <el-table-column type="selection" width="55" />
<!--        批量选择-->
        <el-table-column  label="头像"  >
          <template #default="scope">
            <el-image :preview-src-list="[scope.row.avatar]" :preview-teleported="true"
                      v-if="scope.row.avatar" :src="scope.row.avatar"
                      style="width:40px;height:40px;border-radius: 50%;display: block" />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" width="180" />
        <el-table-column prop="name" label="名称" width="180" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作"width="100">
          <template #default="scope">
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
      </div>
    <el-dialog v-model="data.formVisible" title="用户信息" width="500" destroy-on-close>
<!--     destroy-on-close 重置dialog -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding:20px 30px 10px 0">
        <el-form-item label="账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="电话" prop="phone" >
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">

          <el-upload
             list-type="picture"
              action="http://localhost:9999/files/upload"
             :headers="{token:data.user.token}"
              multiple
              :limit="3"
              :on-success="handleFileSuccess">
<!--              :file-list="[{name: 'name', url: 'url'}, {name: 'name2', url: 'url'}]">-->
            <el-button size="small" type="primary">点击上传</el-button>
<!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>

<!--  /*在后面加图标*/-->
</template>

<script lang="ts" setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js"
import axios from "axios";
import {ElMessage, ElMessageBox} from "element-plus";
import {Search} from "@element-plus/icons-vue"
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  name:null,
  username:null,
  pageNum:1,
  pageSize:5,
  total:6,
  tableData:[],
  formVisible:false,
  form:{},
  rules:{
    username:[
      {required:true,message:'请填写账号',trigger:'blur'}
    ],
    name:[
      {required:true,message:'请填写名称',trigger:'blur'}
    ],
    phone:[
      {required:true,message:'请填写手机',trigger:'blur'}
    ],
    email:[
      {required:true,message:'请填写邮箱',trigger:'blur'}
    ]
  },
  rows:[],
  ids:[]
})
const handleFileSuccess=(res)=>{
  data.form.avatar=res.data
}
const deleteBatch=()=>{
  if(data.rows.length === 0){
    ElMessage.warning('请选择要删除的数据')
    return
  }
  ElMessageBox.confirm(
      '删除后无法恢复，是否删除该行数据？',
      '提示',  // 修正标题描述
      {
        type: "warning",
        confirmButtonText: "确定",  // 补充确认按钮文本
        cancelButtonText: "取消"    // 补充取消按钮文本
      }
  ).then(res => {
    request.delete('/teacher/deleteBatch',{data:data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {});
}
const exportData =()=>{
  let idsStr = data.ids.join(",")
  let url =`http://localhost:9999/teacher/export?username=${data.username === null?'':data.username}&name=${data.name=== null?'':data.username}`
  + `&ids=${idsStr}`
  + `&token=${data.user.token}`
  window.open(url)
}
const handleSelectionChange = (rows) => {//rows实际选择的数组
 data.rows=rows
  data.ids = data.rows.map(v => v.id)
}

const load=() =>{
  request.get('/teacher/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
      username:data.username
    }
  }).then(res =>{
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset=()=>{
  data.username=null
  data.name=null
  load()
}
const handleAdd=()=>{
  data.formVisible=true
  data.form={}
}
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/teacher/add', data.form).then(res => {
        if (res.code === '200') {
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
const handleEdit=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))//深度拷贝数据
  data.formVisible=true
}
const update=()=>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/teacher/update', data.form).then(res => {
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
const handleImportSuccess=(res)=>{
  if(res.code === '200') {
    ElMessage.success("导入成功")
    load()
  }
  else{
    ElMessage.error(res.msg)
  }
}
const save=()=>{
  data.form.id?update():add()
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
    request.delete('/user/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {});
}
const formRef=ref()
</script>