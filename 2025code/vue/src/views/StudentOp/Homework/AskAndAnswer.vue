<template>
  <div >
    <h1 class="ti">问答社区</h1>
    <el-divider></el-divider>
    <div v-for=" t in data.NotHomeWork">
      <h4 class="jk">问题来源：{{t.topic}}</h4>
      <span>{{t.senderName}}</span>
      问题描述： <p>{{t.content}}</p>
      <span>{{t.recipientName}}</span>
      问题回复：<p>{{t.restore}}</p>
      <!--            <router-link to="/markdown">-->
      <!--                <el-button type="primary" plain>查看详情</el-button>-->
      <!--            </router-link>-->

      <el-divider>状态：{{t.status}}</el-divider>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="data.page.page"
        :page-sizes="[3, 5, 7, 9]"
        :page-size="data.page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.page.total">
    </el-pagination>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from "vue";
import Cookies from "js-cookie";
import request from "@/utils/request.js"
const data = reactive({
      page:{
        page:1, //初始页
        pageSize:3,    //    每页的数据
        userId:0,
      },
      NotHomeWork:[],
    }
)

// 从响应式数据中解构出需要的变量
const { page, NotHomeWork } = data;

// 生命周期钩子：组件挂载后执行
onMounted(() => {
  data.page.userId = parseInt(Cookies.get('userId'))
  CheckWork(page);
});

// 分页大小改变时的处理函数
const handleSizeChange = (size) => {
  data.page.pageSize = size;
  CheckWork(page);
  console.log(`每页 ${size} 条`);
};

// 当前页码改变时的处理函数
const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum; // 修正为 page，与定义的属性名一致
  CheckWork(page);
  console.log(`当前页: ${pageNum}`);
};

// 获取数据的函数
const CheckWork = (valid) => {
  request.post('study/askQuestions/list',valid)
      .then((resp) => {
        console.log("resp:",resp)
        data.NotHomeWork = resp.data.data;
        data.page.total = resp.data.total || 0
        console.log(data.NotHomeWork);
      })
      .catch((error) => {
        console.error('获取数据失败:', error);
        // 可以在这里添加错误提示或处理逻辑
      });
};
</script>

<style scoped>
h4{
  display: flex;
  justify-content: center;
}
.right{
  display: flex;
  justify-content: flex-end;
}
.ti{
  display: flex;
  justify-content: center;
}
</style>