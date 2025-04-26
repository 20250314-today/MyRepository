<template>
  <div>
    <div class="subject">
      <h1 class="dohomework">未完成作业详情</h1>
    </div>
    <div v-for="NH in data.NotHomeWork">
      <h2>{{NH.subjectName}}</h2>
      <span>{{NH.userName}}</span>
      <P>作业标题：{{NH.title}}</P>
      <!--            <router-link to="/markdownnothomework">-->
      <div class="bt">
        <el-button type="primary" plain  @click="addNotHomework(NH)">开始答题</el-button>
        <!--                    <el-button type="primary" plain  @click="addNotHomework(NH.answer)">查看参考答案</el-button>-->
      </div>
      <!--            </router-link>-->
      <el-divider>提交时间：</el-divider>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="NotHomeWork.length">
    </el-pagination>
  </div>
</template>

<script setup>
import {reactive, onMounted, ref} from "vue";
import Cookies from 'js-cookie'
import router from "@/router/index.js";
import {work} from "@/utils/studentweb/dohomework.js";
const data = reactive({
      page:{
        page:1, //初始页
        pageSize:10,    //    每页的数据
        userId:0,
        roleId:0,
        classId:0
      },
      NotHomeWork:[],
    })
// 组件挂载后执行
onMounted(() => {
  data.page.userId = Cookies.get('userId');
  data.page.roleId = Cookies.get('roleId');
  data.page.classId = Cookies.get('classId');
  CheckWork(data.page);
});

// 分页相关方法
const handleSizeChange = (size) => {
  data.page.pageSize = size;
  CheckWork(data.page);
  console.log(`每页 ${size} 条`);
};

const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum; // 修正为page
  CheckWork(data.page);
  console.log(`当前页: ${pageNum}`);
};

// 获取未完成作业
const CheckWork = (valid) => {
  work(valid).then(resp => {
    data.NotHomeWork = resp.data.resultData.data;
  }).catch(error => {
    console.error('获取未完成作业失败:', error);
    // 可以添加错误提示
  });
};

// 导航到答题页面
const addNotHomework = (homework) => {
  router.push({
    name: 'MarkDownNotHomeWork',
    params: {
      data1: homework,
      data2: '123'
    }
  });
};
</script>

<style scoped>
.bt{
  display: flex;
  justify-content: center;
}
.dohomework{
  display: flex;
  justify-content: center;
}
</style>