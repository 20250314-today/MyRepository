<template>
  <div>
    <div class="card" style="margin-bottom:5px;width:800px">
      <el-input :prefix-icon="Search" clearable @clear="load" placeholder="请输入标题" v-model="data.name" style="width:240px;
      margin-right: 5px" ></el-input>
      <el-button type="primary" @click="load">搜 索</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </div>
    <div class="card" style="margin-bottom:5px">
      <el-button type="success"  @click="handleAdd">新增</el-button>

      <el-button type="info" @click="exportData">导出</el-button>
      <el-upload
          action="http://localhost:9999/user/import"
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
          <el-table-column  label="图书封面" width="100" >
            <template #default="scope">
              <el-image :preview-src-list="[scope.row.img]" :preview-teleported="true"
                        v-if="scope.row.img" :src="scope.row.img"
                        style="width:50px;height:50px;display: block" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="书名" width="180" />
          <el-table-column prop="author" label="作者" width="180" />
          <el-table-column prop="description" label="简介" width="180" >
            <template v-slot="scope">
              <el-button type="primary" @click="viewContent(scope.row.description)">查看全文</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="书籍价格" />
          <el-table-column label="操作" width="100" >
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
    <el-dialog v-model="data.formVisible" title="图书信息" width="500" destroy-on-close>
      <!--     destroy-on-close 重置dialog -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding:20px 30px 10px 0">
        <el-form-item label="书籍名字" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入书籍名字"/>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="data.form.author" autocomplete="off" placeholder="请输入作者名字"/>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="data.form.price" autocomplete="off" placeholder="请输入价格"/>
        </el-form-item>
<!--        <el-form-item label="库存" prop="num">-->
<!--          <el-input v-model="data.form.num" autocomplete="off" placeholder="请输入库存"/>-->
<!--        </el-form-item>-->
        <el-form-item label="封面" prop="img">

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
        <el-form-item label="简介" prop="description">
          <div style="border:1px solid #ccc;width:100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height:300px;overflow-y:hidden; "
                v-model="data.form.description"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
          <!--            <el-input type="textarea" rows="4" v-model="data.form.content" autocomplete="off" placeholder="请输入公略内容"/>-->
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="data.viewVisible" title="简介" width="60%" destroy-on-close>
      <div v-html="data.description" style="padding :0 10px">

      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";

const formRef =ref()
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  name:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  viewVisible:false,
  description:null,
  rules:{
    img:[
      {required:true,message:'请输入上传图书封面',trigger:'blur'}
    ],
   name:[
      {required:true,message:'请输入书籍名称',trigger:'blur'}
    ],
    // price:[
    //   {required:true,message:'请输入价格',trigger:'blur'}
    // ],
    author:[
      {required:true,message:'请输入作者名字',trigger:'blur'}
    ],
    description:[
      {required:true,message:'请输入简介',trigger:'blur'}
    ],
  }
})

const editorRef=shallowRef()
const mode='default'
const editorConfig = {MENU_CONF:{}}
editorConfig.MENU_CONF['uploadImage']={
  headers:{
    token:data.user.token,
  },
  server:'http://localhost:9999/files/wang/upload',//服务端图片上传接口
  fieldName:'file'
}
const handleCreated=(editor)=>{
  editorRef.value=editor
}
onBeforeUnmount(()=>{
  const editor = editorRef.value
  if(editor == null) return
  editor.destroy()

})
const load=() =>{
  request.get('/book/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
    }
  }).then(res =>{
    if (res.code === '200') {
      data.tableData = res.data?.list
      console.log('book:',data.tableData)
      console.log('token:',data.user.token)
      data.total = res.data?.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
load()
const viewContent=(description)=>{
  data.description=description
  data.viewVisible=true

}
const handleAdd=()=>{
  data.form={}
  data.formVisible=true
}
const add=()=> {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('book/add', data.form).then(res => {
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
    request.delete('/book/delete/' + id).then(res => {
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
      request.post('book/update', data.form).then(res => {
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
  data.name=null
  load()
}
const handleFileSuccess=(res)=>{
  data.form.img=res.data
}
</script>
<style>

</style>