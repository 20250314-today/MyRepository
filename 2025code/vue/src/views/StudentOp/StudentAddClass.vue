<template>
  <div>
    <div v-if="StudentClassData.length > 0">
      <div v-for="s in StudentClassData" :key="s.id">
        <div>
          <span>
            <el-tag type="success"> 班级：</el-tag>
            {{s.className}}
          </span>
          <p>
            <el-tag type="success"> 班主任：</el-tag>
            {{s.userName}}
          </p>
          <el-popover
              placement="top-start"
              title="班级说明"
              :width="200"
              trigger="hover"
          >
            <template #reference>
              <el-button
                  type="primary"
                  :disabled="flag"
                  @click="studentAddClass(s.id, s.userId)"
                  round
              >
                加入班级
              </el-button>
            </template>
            <div class="popover-content">
              <p>班级名称：{{ s.className }}</p>
              <p>班主任：{{ s.userName }}</p>
              <el-divider />
              <p>加入后需等待班主任审核</p>
            </div>
          </el-popover>

        </div>
        <el-divider>

        </el-divider>
      </div>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import request from '@/utils/request.js'
export default {
  name: "StudentManagement",
  data() {
    return {
      user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
      StudentClassData: [],
      page: {
        page: 1, //初始页
        pageSize: 10,    //    每页的数据
      },
      addClassStudent: {
        classId: '',
        id: ''
      },
      applicantHistory: [],
      flag: true,
      param: {
        id: ''
      }
    };
  },
  created() {
    this.param.userId = parseInt(this.user.id);
    this.isAllFlag(this.param);
    this.StudentClass(this.page);
    this.addClassStudent.userId =  parseInt(this.user.id);
    console.log('用户id:'+ this.addClassStudent.userId)
    console.log('类型:' + typeof this.user.id)

  },
  methods: {
    isAllFlag(param) {
     request.post('/applicant/joinTrueOrFalse',param).then(resp => {
        this.flag = resp.data;
      }).catch(error => {
        console.error('isAllFlag 方法出错:', error);
      });
    },
    handleSizeChange(size) {
      this.page.pageSize = size;
      this.StudentClass(this.page);
      console.log(`每页 ${size} 条`);
    },
    handleCurrentChange(pageNum) {
      this.page.page = pageNum; // 修正这里
      this.StudentClass(this.page);
      console.log(`当前页: ${pageNum}`);
    },
    async StudentClass(page) {
      try {
        const res = await request.post('/study/classes/list', {
          page: page.page,
          pageSize: page.pageSize
        });

        this.StudentClassData = res.data?.data || [];

        this.page.total = res.data?.total || 0;

      } catch (error) {
        this.StudentClassData = [];
        console.error('班级数据加载失败:', error);
      }
    },
    studentAddClass(classId,userId) {
      this.addClassStudent.classId = parseInt(classId);
      this.addClassStudent.teacherId = parseInt(userId)
      console.log('classid类型:' + typeof classId)
     request.post('/applicant/save',this.addClassStudent).then(resp => {
        if (resp.code ===  '200' ) {
          this.$message({
            message: '恭喜你，申请成功，待老师审核',
            type: 'success'
          });
          this.flag = true;
        } else {
          this.$message.error('不可加入该班级');
        }
      }).catch(error => {
        console.error('studentAddClass 方法出错:', error);
      });
    }
  }
};
</script>

<style scoped>
.right{
  display: flex;
  justify-content: center;
}
</style>