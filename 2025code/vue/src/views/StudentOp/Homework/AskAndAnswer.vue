<template>
  <div>
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
        :page-sizes="[10, 20, 30, 40]"
        :page-size="data.page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.NotHomeWork.length">
    </el-pagination>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from "vue";
import {askandanswer} from '../../../utils/studentweb/askandanswer.js'
import Cookies from "js-cookie";
const data = reactive({
      page:{
        page:1, //初始页
        pageSize:10,    //    每页的数据
        userId:0,
      },
      NotHomeWork:[],
    }
)

// 从响应式数据中解构出需要的变量
const { page, NotHomeWork } = data;

// 生命周期钩子：组件挂载后执行
onMounted(() => {
  page.userId = Cookies.get('userId');
  CheckWork(page);
});

// 分页大小改变时的处理函数
const handleSizeChange = (size) => {
  page.pageSize = size;
  CheckWork(page);
  console.log(`每页 ${size} 条`);
};

// 当前页码改变时的处理函数
const handleCurrentChange = (pageNum) => {
  page.page = pageNum; // 修正为 page，与定义的属性名一致
  CheckWork(page);
  console.log(`当前页: ${pageNum}`);
};

// 获取数据的函数
const CheckWork = (valid) => {
  askandanswer(valid)
      .then((resp) => {
        NotHomeWork.value = resp.data.resultData.data;
        console.log(NotHomeWork.value);
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