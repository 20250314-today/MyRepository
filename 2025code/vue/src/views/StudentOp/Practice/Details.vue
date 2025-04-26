<template>
  <div>
    <el-table
        :data="ApplicantHistory"
        border
        style="width: 100%">
      <el-table-column
          prop="createTime"
          label="提交时间"
          width="180">
      </el-table-column>
      <el-table-column
          prop="title"
          label="作业名称"
          width="180">
      </el-table-column>
      <el-table-column
          prop="userName"
          label="批改教师">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="ApplicantHistory.length">
    </el-pagination>
  </div>

</template>

<script setup>
import { reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import Cookies from "js-cookie";
// 假设这里的 practicesde 是从正确的路径导入的 API 函数
import { practicesde } from "../../../../api/studentweb/pradetail.js";

// 获取路由实例
const router = useRouter();

// 定义响应式数据
const data = reactive({
  ApplicantHistory: [],
  page: {
    page: 1, // 初始页
    pageSize: 10, // 每页的数据
    userId: "",
  },
});

// 从响应式数据中解构出需要的变量，方便使用
const { ApplicantHistory, page } = data;

// 生命周期钩子：组件挂载后执行
onMounted(() => {
  page.userId = Cookies.get("userId");
  detailHistory(page);
});

// 处理编辑操作，导航到指定页面
const handleEdit = (index, row) => {
  router.push({
    name: "pDetail",
    params: {
      data2: row,
    },
  });
};

// 处理分页大小改变的操作
const handleSizeChange = (size) => {
  page.pageSize = size;
  detailHistory(page);
  console.log(`每页 ${size} 条`);
};

// 处理当前页码改变的操作
const handleCurrentChange = (pageNum) => {
  page.page = pageNum; // 修正属性名，应该是 page 而不是 pageNum
  detailHistory(page);
  console.log(`当前页: ${pageNum}`);
};

// 获取详情历史数据的函数
const detailHistory = (page) => {
  practicesde(page)
      .then((resp) => {
        ApplicantHistory.value = resp.data.resultData.data;
        console.log(ApplicantHistory.value);
      })
      .catch((error) => {
        console.error("获取详情历史数据失败:", error);
        // 可以根据需求添加错误提示或其他处理逻辑
      });
};
</script>

<style scoped>

</style>