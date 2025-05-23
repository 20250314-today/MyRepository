<template>
  <div>
    <el-button type="primary" @click="uploadddVideo()"> 新增课程</el-button>
    <div style="height: 20px"></div>
    <el-dialog title="请输入课程信息" v-model="this.goDealDialogVisible" width="50%">
          <el-form label-width="100px">
            <el-form-item label="课程标题">
              <el-input v-model="uploadVideo.topic" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="上传图片">
              <el-upload class="upload-demo" ref="upload" :data="uploadVideo"
                         action="http://localhost:9999/study/videoTotal/save"
                         :on-preview="handlePreview"
                         :on-remove="handleRemove" :before-remove="beforeRemove" multiple :limit="3"
                         :on-exceed="handleExceed" :file-list="uploadVideo.fileList"
                         :headers="{token:this.user.token}"
                         :on-success="handleCoverUploadSuccess">
                <el-button size="small" type="primary"> 上传封面</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件</div>
              </el-upload>
            </el-form-item>
          </el-form>

      <span slot="footer" class="dialog-footer">
                <el-button @click="goDealDialogVisible = false"> 取 消</el-button>
                <el-button type="primary" @click="submit(videoParam)"> 确 定</el-button>
            </span>
    </el-dialog>



    <div class="dem">

      <div class='demo' v-for="url in videoData">
        <div >
          <router-link   :to="{path:'/manager/teachervideo', query:{videoTotalId:url.id}}"   >
            <el-image :src=url.coverUrl></el-image>
          </router-link>
          <h3>{{url.topic}}</h3>
          <el-button type="primary" @click="deleteAllVideos(url.id)">删除视频</el-button>
        </div>
      </div>
    </div>
    <div style="height: 20px"></div>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.page"
                   :page-sizes="[10, 20, 30, 40]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper"
                   :total="page.total">
    </el-pagination>
  </div>

</template>

<script>
import Cookies from "js-cookie";
import request from "@/utils/request.js"
export default {
  name: "Video",
  data() {
    return {
      user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
      goDealDialogVisible: false,
      page: {
        page: 1, //初始页
        pageSize: 10,    //    每页的数据
        userId: '',
        roleId: '',
      },

      uploadVideo: {
        topic: '',
        userId: 0,
      },
      videoData: [],
      total: 0,
      videoParam: {
        fileList: '',
        title: '',
        userId: '',
      },
      deleteparam: {
        id: ''
      }

    }
  },
  created() {
    //this.page.userId = Cookies.get('userId')
    this.page.userId = parseInt(this.user.id)
    console.log('用户id:'+ this.user.id)
   // this.page.roleId = Cookies.get('roleId')
    this.videoParam.userId = parseInt(this.user.id)
    this.listAllVideos(this.page)
    console.log('token:',this.user.token)
   // console.log(this.$store.state.baseApi + this.url.coverUrl)
  },
  methods: {
    goDealHandleClose() {

    },
    handleCoverUploadSuccess(response) {
      if (response.code === '200') {
        const coverUrl = response.data; // 假设返回图片路径
        // 调用保存视频集接口
        request.post('/study/videoTotal/save', {
          topic: this.uploadVideo.topic,
          userId: this.uploadVideo.userId,
          coverUrl: coverUrl
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("创建成功");
            this.listAllVideos(this.page);
          }
        });
      }
    },
    deleteAllVideos(id) {
      this.deleteparam.id = id
      this.$confirm('此操作将永久删除该视频集下的所有视频, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post('/study/videoTotal/delete',this.deleteparam).then(resp => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.listAllVideos(this.page)
        })
        this.listAllVideos(this.page)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    getFullCoverUrl(item) {
      console.log('default:',this.defaultCover)
      if (!item?.coverUrl)
        return this.defaultCover
      console.log('url:',item.coverUrl)
      return `${item.coverUrl}`
    },
    uploadddVideo() {
      this.goDealDialogVisible = true
      this.uploadVideo.userId =  parseInt(this.user.id) || 0
      console.log("uploadID:",this.user.id)
      this.listAllVideos(this.page)
    },
    submit(videoParam) {
      this.$refs.upload.submit();
      this.goDealDialogVisible = false
      this.listAllVideos(this.page)
    },
    listAllVideos(page) {
      page.userId =parseInt(this.user.id)
      request.post('/study/videoTotal/pageVideo',page).then(resp => {
        this.videoData = resp.data.records
        console.log('videodata:',this.videoData)
        console.log('res:',resp)
        this.page.total = resp.data?.total || 0
      })
    },
    handleSizeChange(size) {
      this.page.pageSize = size;

    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.page.page = pageNum;
      this.listAllVideos(this.page)
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    }

  }
}
</script>

<style scoped>
.demo {
  width: 200px;
  /*height: 20%;*/
  text-align: center;
  line-height: 20%;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
  margin-right: 4px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 20px;

}

el-button {
  display: inline-block;
}

.course {
  display: inline-block;
}

.ta {
  display: flex;
  justify-content: space-between;
}

.dem {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.el-image__inner {
  vertical-align: top;
  height: 200px;
}

.demo:hover {
  display: block;
}
</style>