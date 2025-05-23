<template>
  <div>
    <el-table
        :data="data.tableData"
        :default-sort="{prop: 'date', order: 'descending'}"
        border
        style="width: 100%">
      <el-table-column
          prop="createTime"
          label="创建日期"
          width="180">
      </el-table-column>
      <el-table-column
          sortable
          fixed
          prop="account"
          label="账号"
          width="180">
      </el-table-column>
      <el-table-column
          prop="userName"
          label="姓名"
          width="180">
      </el-table-column>
      <el-table-column
          prop="studentPhone"
          label="学生电话">
      </el-table-column>
      <el-table-column
          prop="status"
          label="状态">
      </el-table-column>
      <el-table-column
          prop="charger"
          label="审核人">
      </el-table-column>


      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button  v-if="scope.row.status==='待审核'"
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">同意
          </el-button>
          <el-button  v-if="scope.row.status==='待审核'"
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">驳回
          </el-button>
        </template>
      </el-table-column>
    </el-table>
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
import { reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from "@/utils/request.js"

const data = reactive({
  user: JSON.parse(localStorage.getItem("code_user") || '{}'),
  tableData: [],
  page: {
    page: 1, //初始页
    pageSize: 10, // 每页的数据
    userId: '',
  },
  alertData: {},
  flag: true
})

onMounted(() => {
  data.page.userId = data.user.id
  listUnApplicant(data.page)
  console.log(data.page)
  // 这里的判断可能需要调整，因为tableData是数组
  // if(data.tableData.status==='待审核'){
  //   data.flag=false
  // }
})

const handleEdit = (index, row) => {
  data.alertData = row
  data.alertData.status = 2
  request.post('/applicant/update', data.alertData).then(resp => {
    if (resp.code ===  '200') {
      listUnApplicant(data.page)
      ElMessage({
        message: '您已同意该学生加入您的班级',
        type: 'success'
      });
    } else {
      ElMessage.error('操作失败');
    }
  })
}

const handleDelete = (index, row) => {
  data.alertData = row
  console.log("row:", row)
  data.alertData.status = 3
  request.post('/applicant/update', data.alertData).then(resp => {
    if (resp.code ===  '200') {
      listUnApplicant(data.page)
      ElMessage({
        message: '您已驳回该学生加入您的班级',
        type: 'success'
      });
    } else {
      ElMessage.error('操作失败');
    }
  })
}

const handleSizeChange = (size) => {
  data.page.pageSize = size;
  listUnApplicant(data.page)
  console.log(`每页 ${size} 条`);
}

const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum; // 修正为page
  listUnApplicant(data.page)
  console.log(`当前页: ${pageNum}`);
}

const listUnApplicant = (page) => {
  request.post('/applicant/list', page).then(resp => {
    console.log('完整响应:', resp) // 查看数据结构
    console.log('resp.data:', resp.data)
    data.tableData = resp.data.data || []
    data.page.total = resp.data?.total || 0
    console.log('审核1:', data.page.total)
  })
}
</script>

<style scoped>

</style>