<template>
  <div>
    <div class="subject">
      <h1 class="nothomework">已完成作业详情</h1>
    </div>
    <el-divider></el-divider>
    <div v-for=" h in data.HomeWork ">
      <h2>{{h.subjectName}}</h2>
      <h2>{{h.userName}}</h2>
      <p>作业标题：{{h.title}}</p>
      <p> 完成时间：{{h.completionTime}}</p>
      <p> 批改状态:{{h.mode=='0'?'未批改':'已批改'}}</p>

      <div class="bt">
        <el-button  type="primary" plain @click="addDetail(h,'zuoye')">查看详情</el-button>
        <el-button  type="primary" plain @click="addDetail(h,'daan')">查看参考答案</el-button>
      </div>
      <el-divider>提交时间：</el-divider>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="data.page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="data.page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.page.total">
    </el-pagination>

  </div>
</template>

<script setup>
import {reactive,  onMounted,ref} from "vue";
import Cookies from 'js-cookie'
import {useRouter} from 'vue-router';
import { useStore } from 'vuex';
import request from "@/utils/request.js"
const router = useRouter();
const store = useStore();
console.log('store.state.homework:', store.state.HomeWork); // 应输出 { homeworkList: [] }
const data = reactive({
      page:{
        page:1, //初始页
        pageSize:10,    //    每页的数据
        userId:0,
      },
      HomeWork:[],
    })
const handleSizeChange = (size) => {
  data.page.pageSize = size;
  CheckWork(data.page);
  console.log(`每页 ${size} 条`);
};

const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum;
  CheckWork(data.page);
  console.log(`当前页: ${pageNum}`);
};

const CheckWork = (valid) => {
  request.post('/study/userdohomework/list',valid).then(resp => {
    console.log("checkhome:",resp)
    data.HomeWork = resp.data.data;
    data.page.total = resp.data.total || 0
    console.log(data.HomeWork.value);
  });
};
const addDetail = (h, type) => {
  // 1. 序列化为 JSON 字符串
  const homeworkJson = JSON.stringify(h);
  // 2. 编码避免特殊字符问题
  const encodedHomework = encodeURIComponent(homeworkJson);

  router.push({
    name: 'MarkDown',
    query: {
      homework: encodedHomework, // 传递编码后的字符串
      type: type }
  });
  // ✅ 直接调用根 Action（无需命名空间）
  store.dispatch('updateHomework', h);
};

onMounted(() => {
  data.page.userId = parseInt(Cookies.get('userId'))
  CheckWork(data.page);
});
</script>

<style scoped>
.right{
  display: flex;
  justify-content: center;
}
.bt{
  display: flex;
  justify-content: center;
}
.nothomework{
  display: flex;
  justify-content: center;
}
</style>