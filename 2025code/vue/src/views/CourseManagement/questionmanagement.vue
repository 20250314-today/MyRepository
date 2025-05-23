<template>
  <div class="block" style="width:900px">
    <el-timeline>

      <div v-for="t in shduleData">
        <el-timeline-item :timestamp=t.createTime placement="top">
          <el-card>
            <div class="cs">
              <span><el-tag type="success" >{{t.senderName}}提问:</el-tag></span>
              <span >
                        <el-tag type="warning">问题来源：{{t.topic}}</el-tag>
                        </span>

              <span ><el-tag type="success" >{{t.recipientName}}回答</el-tag></span>
            </div>
            <el-divider content-position="center">下面是提问记录</el-divider>
            <div class="csc">
              <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入内容"
                  v-model=t.content>
              </el-input>
              <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入内容"
                  v-model=t.restore>
              </el-input>
            </div>

            <el-badge :value="count" class="item">
              <el-button size="small" @click="restor(t)">回复</el-button>
            </el-badge>
          </el-card>
        </el-timeline-item>
      </div>
    </el-timeline>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.page"
        :page-sizes="[3, 5, 7, 9]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="this.page.total">
    </el-pagination>
  </div>

</template>

<script>
import Cookies from "js-cookie";
import request from "@/utils/request.js"
export default {
  name: "CourseManagement",
  data() {
    return {
      shduleData: [],
      page:{
        page:1, //初始页
        pageSize:2,    //    每页的数据
        userId:''
      },
      count:0,
    }
  },
  created() {
    this.page.userId=parseInt(Cookies.get('userId'))
    console.log("tid:s ",Cookies.get("userId"))
    this.listShdule(this.page)
  },
  methods:{
    restor(shduleData){
      const updatedData = { ...shduleData, status: 1 }; // 深拷贝避免引用问题
      request.post('/study/askQuestions/update',updatedData).then(resp=>{
        if(resp.code=== '200'){
          this.$message({
            message: '恭喜你，回复成功 ',
            type: 'success'
          });
          this.count=this.count+1
          this.listShdule(this.page)
        }else{
          this.$message.error('回复失败');
        }
      })
    },
    handleSizeChange(size) {
      this.page.pageSize = size;
      this.listShdule(this.page);
      console.log(`每页 ${size} 条`);
    },
    handleCurrentChange(pageNum) {
      this.page.page = pageNum; // 修正这里
      this.listShdule(this.page);
      console.log(`当前页: ${pageNum}`);
    },
    listShdule(page){
      request.post('/study/askQuestions/list',page).then(resp=>{
        console.log("askData:",resp)
        this.shduleData=resp.data.data
        this.page.total = resp.data?.total || 0;
      })
    }
  },
}
</script>

<style scoped>
.item{
  display: flex;
  justify-content: flex-end;
  left:745px;
  top:6px
}
.csc{
  display: flex;
  justify-content: space-between;
}
.cs{
  display: flex;
  justify-content: space-between;
}
h4{
  display: inline !important;
}

</style>
