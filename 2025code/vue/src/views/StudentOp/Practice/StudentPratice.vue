<template>
  <div>
    <div class="subject">
    </div>
    <el-divider></el-divider>
    <div>
      <div>
        <el-table
            :data="data.ScoreData"
            :default-sort="{prop: 'createTime', order: 'descending'}"
            border
            style="width: 100%">
          <el-table-column
              sortable
              fixed
              prop="createTime"
              label="发布时间"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              fixed
              prop="title"
              label="试题名称"
              width="180">
          </el-table-column>
          <el-table-column
              sortable
              fixed
              prop="userName"
              label="发布人"
              width="180">
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button
                  size="mini"
                  @click="handleSubmit(scope.$index, scope.row)">开始答题</el-button>
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
            :total="data.totalCount">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Cookies from "js-cookie";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js"

const router = useRouter()

const data = reactive({

  ScoreData: [],
  page: {
    page: 1, // 初始页
    pageSize: 10, // 每页的数据
    classId: '',
    userId: ''
  },
  totalCount: 0 // 总记录数
})

// 获取考试列表
const listAllStudentsScore = (page) => {
  request.post('/study/exercises/findNotDoExercises',page).then(resp => {
    console.log("exerdata:",resp)
    data.ScoreData = resp.data.data || []
    data.totalCount = resp.data.total || 0
  }).catch(err => {
    console.error('获取考试列表失败', err)
    // 错误提示
    ElMessage.error('获取考试列表失败')
  })
}

// 开始答题
const handleSubmit = (index, row) => {
  // 添加参数校验
  if (!row?.id || !row?.content) {
    ElMessage.error('试题数据不完整')
    return
  }

  // 使用更安全的编码方式
  try {
    router.push({
      name: "MarkDownNotHomeWork",
      query: {
        id: row.id.toString(), // 确保字符串类型
        content: encodeURIComponent(row.content),
        data2: 'notwork'
      }
    })
  } catch (e) {
    console.error('路由跳转失败:', e)
    ElMessage.error('系统错误，请联系管理员')
  }
}

// 每页条数变化
const handleSizeChange = (size) => {
  data.page.pageSize = size
  listAllStudentsScore(data.page)
  console.log(`每页 ${size} 条`)
}

// 当前页变化
const handleCurrentChange = (pageNum) => {
  data.page.page = pageNum
  listAllStudentsScore(data.page)
  console.log(`当前页: ${pageNum}`)
}

onMounted(() => {
  data.page.classId = parseInt(Cookies.get('classId'))
  data.page.userId =parseInt( Cookies.get('userId'))
  listAllStudentsScore(data.page)
})
</script>

<style scoped>

</style>