<template>
  <div>
    <div class="subject">
      <h1 class="nothomework">已完成作业详情</h1>
    </div>
    <el-divider></el-divider>
    <div v-for=" h in HomeWork ">
      <h2>{{h.subjectName}}</h2>
      <h2>{{h.userName}}</h2>
      <p>作业标题：{{h.title}}</p>
      <p> 完成时间：{{h.completionTime}}</p>
      <p> 批改状态:{{h.mode=='0'?'未批改':'已批改'}}</p>
      <!--            <p>{{h.content}}</p>-->
      <!-- <router-link to="/markdown"> -->
      <div class="bt">
        <el-button  type="primary" plain @click="addDetail(h,'zuoye')">查看详情</el-button>
        <el-button  type="primary" plain @click="addDetail(h,'daan')">查看参考答案</el-button>
      </div>
      <!-- </router-link> -->

      <el-divider>提交时间：</el-divider>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="data.page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="data.page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.HomeWork.length">
    </el-pagination>

  </div>
</template>

<script setup>
import {reactive,  onMounted,ref} from "vue";
import {workdo} from '../../../utils/studentweb/dohomework.js'
import Cookies from 'js-cookie'
import {useRouter} from 'vue-router';
import {useStore} from 'vuex';
const router = useRouter();
const store = useStore();

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
  CheckWork(page);
  console.log(`每页 ${size} 条`);
};

const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum;
  CheckWork(page);
  console.log(`当前页: ${pageNum}`);
};

const CheckWork = (valid) => {
  workdo(valid).then(resp => {
    data.HomeWork.value = resp.data.resultData.data;
    console.log(HomeWork.value);
  });
};

const addDetail = (h, type) => {
  router.push({
    name: 'MarkDown',
    params: { 'homework': h, type: type }
  });
  store.dispatch('add', h);
};

onMounted(() => {
  data.page.userId = Cookies.get('userId');
  CheckWork(page);
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