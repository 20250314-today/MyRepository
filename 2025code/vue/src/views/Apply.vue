<template>
  <div>
    <div class="card" style="margin-bottom:5px">
      <el-input :prefix-icon="Search" clearable @clear="load" placeholder="请输入标题" v-model="data.title" style="width:240px;
      margin-right: 5px" ></el-input>
      <el-button type="primary" @click="load">搜 索</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </div>
    <div class="card" style="margin-bottom:5px" >
      <el-button type="success"  @click="handleAdd" v-if="data.user.role === 'USER'">提交申诉</el-button>
    </div>
    <div class="card" style="margin-bottom:5px" >
      <el-table :data="data.tableData" style="width: 100% "  @selection-change="handleSelectionChange"
                :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
        <!--        批量选择-->
        <el-table-column prop="title" label="申诉标题" width="170" />
        <el-table-column prop="content" label="申诉内容" width="180" />
        <el-table-column prop="userName" label="申诉人" width="100" />
        <el-table-column prop="time" label="提交时间" width="180" />
        <el-table-column prop="status" label="申诉状态" width="180" >
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status ==='待审核'">{{scope.row.status}}</el-tag>
            <el-tag type="success" v-if="scope.row.status ==='审核通过'">{{scope.row.status}}</el-tag>
            <el-tag type="danger" v-if="scope.row.status ==='审核拒绝'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="审核说明" width="180" />
        <el-table-column label="操作" width="100" >
          <template #default="scope" v-if="data.user.role === 'USER'">
            <el-button :disabled="scope.row.status !== '待审核'"  icon="Edit" type="primary" circle @click="handleEdit(scope.row)"></el-button>
            <el-button :disabled="scope.row.status !== '待审核'" icon="Delete" type="danger" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button :disabled="scope.row.status !== '待审核'" type="primary"  @click="handleEdit(scope.row)">审核</el-button>
<!--            <el-button :disable="scope.row.status !== '待审核'" icon="Delete" type="danger" circle @click="del(scope.row.id)"></el-button>-->
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
    <el-dialog v-model="data.formVisible" title="申诉信息" width="500" destroy-on-close>
      <!--     destroy-on-close 重置dialog -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding:20px 30px 10px 0">
        <el-form-item label="申诉标题" prop="title" v-if="data.user.role === 'USER'">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入申诉标题" />
        </el-form-item>
        <el-form-item label="申诉内容" prop="content" v-if="data.user.role === 'USER'">
          <el-input v-model="data.form.content" autocomplete="off" placeholder="请输入申诉内容" type="textarea" :rows="3"/>
        </el-form-item>
        <el-form-item label="申诉状态" prop="status" v-if="data.user.role === 'ADMIN'">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="待审核" value="待审核" />
            <el-radio-button label="审核通过" value="审核通过" />
            <el-radio-button label="审核拒绝" value="审核拒绝" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申诉说明" prop="reason" v-if="data.user.role === 'ADMIN'&& data.form.status==='审核拒绝'">
        <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝说明" type="textarea" :rows="3"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">提交</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data=reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  pageNum:1,
  pageSize:5,
  total:0,
  title:null,
  tableData:[],
  formVisible:false,
  form:{},
  rules:{
    title:[
      {required:true,message:'请输入申诉标题',trigger:'blur'}
    ],
    content:[
      {required:true,message:'请输入申诉内容',trigger:'blur'}
    ]
  }
})
const load=() =>{
  request.get('/apply/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title,
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
const reset=()=>{
  data.title=null
  load()
}
const handleAdd=()=>{
  data.form={}
  // data.form.status='待审核'

  data.formVisible=true
}
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/apply/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('提交成功,等待管理员审核')
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
    request.delete('/apply/delete/' + id).then(res => {
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
      request.post('/apply/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('操作成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const formRef =ref()
</script>
<style>

</style>