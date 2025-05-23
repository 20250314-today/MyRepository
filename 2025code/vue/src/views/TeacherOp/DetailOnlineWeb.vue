<template>
  <div style="width:700px">
    <el-button type="primary"  v-if="this.user.role !=='USER'" style="width: 20%;" @click="addVideo()">新增视频</el-button>

    <div style="height: 20px"></div>
    <div class="containvideo">

      <el-dialog
          title="请输入视频集的内容"
          v-model="goDealDialogVisible"
          width="50%"
          :before-close="goDealHandleClose">
        <div style="margin: 10px;"></div>
        <el-form label-width="100px">
          <el-form-item label="视频标题">
            <el-input v-model="addVideoDetail.topic" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="视频排名">
            <el-input v-model="addVideoDetail.sort" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="上传视频">

            <el-upload style="margin-left:14%;margin-top:5%"
                       class="avatar-uploader el-upload--text"
                       :drag="{Plus}"
                       action="http://localhost:9999/study/videos/save"
                       :headers="{token:this.user.token}"
                       multiple
                       :show-file-list="false"
                       :data="addVideoDetail"
                       :on-success="handleVideoSuccess"
                       :before-upload="beforeUploadVideo"
                       :on-progress="uploadVideoProcess">
              <i v-if="Plus" class="el-icon-upload"></i>
              <div v-if="Plus" class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <el-progress v-if="videoFlag === true" type="circle" :percentage="videoUploadPercent" style="margin-top:30px;"></el-progress>
              <div class="el-upload__tip" slot="tip">只能上传mp4/flv/avi文件，且不超过300M</div>
            </el-upload>

          </el-form-item>

          <!--投诉处理结果-->

        </el-form>

        <span slot="footer" class="dialog-footer">
        <el-button @click="goDealDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
        </span>
      </el-dialog>

      <div class="vid" v-if="playerOptions.length > 0 && currentVideoUrl">
        <video-player
            class="video-player vjs-custom-skin"
            ref="videoPlayer"
            controls="controls"
            :playsinline="true"
            :options="playerOptions[count]"
            :key="playerOptions[count]?.sources[0]?.src"
            @play="onPlayerPlay($event,count)"
            @pause="onPlayerPause($event)"
        >
        </video-player>


        <div style="margin: 20px 0;"></div>

        留言： <p><el-input
          type="textarea"
          placeholder="请输入内容"
          v-model="Ask.content"
          maxlength="300"
          show-word-limit></el-input>
      </p>
        <el-button type="success"  style="width: 10%" @click="answer(Ask)">留言</el-button>

        <div style="margin: 200px 0;"></div>
      </div>
      <div class="videos">
        <ul>
          <vue-scroll :ops="ops"> 视频集
            <li v-for="(item, index) in videolist" :key="index">
              <div class="videoInfo">
                <el-button class="el" @click="choose(index)">{{ item.topic }}</el-button>
                <el-button type="primary" @click="deletOneVideo(item.id)" v-if="this.user.role === 'TEACHER'">删除视频</el-button>
              </div>
            </li>
          </vue-scroll>
        </ul>

      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request.js"

export default {
  name: "DetailOnlineWeb",
  data() {
    return {
      user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
      goDealDialogVisible:false,
      Plus: true,
      addVideoDetail:{
        topic:'',
        sort:0,
        videoTotalId:'',
      },
      Ask:{
        content:'',
        sender:'',
        recipient:'',
        videoId:'',
      },
      videolist: [],
      playsinline: true,
      playerOptions: [],
      count: '0',
      roleId:'',
      deleteOneVideoName:{
        id:''
      },
      video: {
        videoTotalId: '',
      },
      ops: {
        vuescroll: {},
        scrollPanel: {},
        rail: {
          keepShow: true,
        },
        bar: {
          hoverStyle: true,
          onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
          background: '#F5F5F5', //滚动条颜色
          opacity: 0.5, //滚动条透明度
          'overflow-x': 'hidden',
        },
        videoData: [],
        tagRoute:'',
      },
      videoFlag: false,
      videoUploadPercent: 0,
      videoForm: {
        videoId: '',
        videoUrl: ''
      }
    };
  },
  computed: {
    // 辅助计算属性，用于验证当前视频URL
    currentVideoUrl() {
      console.log("current:",this.playerOptions[this.count]?.sources[0]?.src)
      console.log('currentVideoUrl 计算属性被调用，count:', this.count);
      return this.playerOptions[this.count]?.sources[0]?.src;
    }
  },
  created() {
    console.log("useeId;",this.user.id)
    this.Ask.sender=this.user.id
    console.log("aksid:",this.Ask.sender)
    this.Ask.recipient = this.$route.query.userId;
    this.video.videoTotalId = this.$route.query.videoTotalId;
    console.log("videoID:",this.video.videoTotalId)
    this.addVideoDetail.videoTotalId = this.$route.query.videoTotalId;
    console.log("this.addvideoID:",this.addVideoDetail.videoTotalId)

    this.Ask.videoId = this.$route.query.videoTotalId;
    this.listAllStudentsScore(this.video)
  },

  watch: {
    // 监听视频列表变化，更新播放器选项
    videolist: {
      handler() {
        this.updatePlayerOptions();
      },
      deep: true
    }
  },

  methods: {
    submit(){
      this.goDealDialogVisible=false;
      this.listAllStudentsScore(this.video);
    },

    // 视频上传前执行
    beforeUploadVideo (file) {
      const isLt300M = file.size / 1024 / 1024 < 300
      if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(file.type) === -1) {
        this.$message.error('请上传正确的视频格式')
        return false
      }
      if (!isLt300M) {
        this.$message.error('上传视频大小不能超过300MB哦!')
        return false
      }
    },

    // 视频上传过程中执行
    uploadVideoProcess (event, file, fileList) {
      this.Plus = false
      this.videoFlag = true
      this.videoUploadPercent = Math.floor((event.loaded / event.total) * 100)
      console.log('当前进度:', this.videoUploadPercent)
    },

    // 视频上传成功是执行
    handleVideoSuccess (res, file) {
      this.Plus = false
      this.videoUploadPercent = 100
      console.log(res)
      // 如果为200代表视频保存成功
      if (res.code === '200') {
        // 接收视频传回来的名称和保存地址
        console.log('resVideo:',res)
        console.log("newname:",res.newVideoName)
        this.videoForm.videoId = res.newVideoName  // 修正属性名拼写错误
        this.videoForm.videoUrl = res.videoUrl      // 修正属性名大小写
        this.$message.success('视频上传成功！')

        // 上传成功后刷新视频列表
        this.listAllStudentsScore(this.video);
      } else {
        this.$message.error('视频上传失败')
      }
    },

    addVideo(){
      this.goDealDialogVisible=true
      this.addVideoDetail.videoTotalId = this.$route.query.videoTotalId;
    },

    answer(ask){
      request.post('/study/askQuestions/save',ask).then(resp=>{
        console.log("askresp:",resp)
        if(resp.code=== '200'){
          this.$message({
            message: '恭喜你，留言成功，请到问答社区查看结果',
            type: 'success'
          });
          this.$router.push("/studentweb/askandanswer")
        }else{
          this.$message.error('留言失败');
        }
      })
    },

    deletOneVideo(id){
      this.deleteOneVideoName.id=id
      console.log("deleteid:",this.deleteOneVideoName.id)
      request.post('/study/videos/delete',this.deleteOneVideoName).then(resp=>{
        if(resp.code=== '200'){
          this.$message({
            message: '恭喜你，删除视频成功',
            type: 'success'
          });
          this.listAllStudentsScore(this.video)
        }else{
          this.$message.error('删除失败');
        }
      })
    },

    choose(index) {
      this.count = index;
      console.log(index, '9999');
      // 手动更新播放器配置（如果有必要）
     // this.updatePlayerOptions();
      const videoItem = this.videolist[index];
      if (videoItem && videoItem.videoUrl) {
        let options = {
          playbackRates: [1.0, 1.25, 1.5, 2.0], // 播放速度
          autoplay: false, // 自动播放
          muted: false,    // 静音
          loop: false,     // 循环播放
          preload: "auto", // 预加载
          language: "zh-CN",
          aspectRatio: "16:9", // 宽高比
          fluid: true, // 自适应大小
          sources: [
            {
              type: "video/mp4",
              src: videoItem.videoUrl // 视频URL
            }
          ],
          poster: "", // 封面图
          notSupportedMessage: "此视频暂无法播放，请稍后再试",
          controlBar: {
            timeDivider: true,
            durationDisplay: true,
            remainingTimeDisplay: false,
            fullscreenToggle: true // 全屏按钮
          }
        };
        this.playerOptions[index] = options;
      }
    },
    // 更新视频播放器选项
    updatePlayerOptions() {
      this.playerOptions = [];

      for (let i = 0; i < this.videolist.length; i++) {
        if (!this.videolist[i].videoUrl) {
          console.error(`视频 ${i} 缺少 videoUrl 属性`);
          continue;
        }
        let options = {
          playbackRates: [1.0, 1.25, 1.5, 2.0], // 播放速度
          autoplay: false, // 自动播放
          muted: false,    // 静音
          loop: false,     // 循环播放
          preload: "auto", // 预加载
          language: "zh-CN",
          aspectRatio: "16:9", // 宽高比
          fluid: true, // 自适应大小
          sources: [
            {
              type: "video/mp4",
              src: this.videolist[i].videoUrl // 视频URL
            }
          ],
          poster: "", // 封面图
          notSupportedMessage: "此视频暂无法播放，请稍后再试",
          controlBar: {
            timeDivider: true,
            durationDisplay: true,
            remainingTimeDisplay: false,
            fullscreenToggle: true // 全屏按钮
          }
        };
        this.playerOptions.push(options);
      }

      console.log(`初始化了 ${this.playerOptions.length} 个视频播放器选项`);
    },

    onPlayerPlay(player, index) {
      // 暂停其他视频
      if (this.$refs.videoPlayer && this.$refs.videoPlayer.length) {
        for (let i = 0; i < this.$refs.videoPlayer.length; i++) {
          if (i !== index && this.$refs.videoPlayer[i].player) {
            this.$refs.videoPlayer[i].player.pause();
          }
        }
      }
    },

    onPlayerPause(player) {
      console.log('视频暂停');
    },

    listAllStudentsScore(page) {
      request.post('/study/videos/selectByVideoTotalId',page).then(resp => {
        this.videolist = resp.data || [];
        console.log('视频列表:', this.videolist);

        // 默认选中第一个视频
        if (this.videolist.length > 0) {
          this.count = 0;
        }
      })
    },

    goDealHandleClose(done) {
      this.goDealDialogVisible = false;
      done();
    }
  }
}
</script>
<style scoped>
.containvideo {
  display: flex;
  justify-content: space-between;

}

.item{
  width: 20px;
  height: 20px;
  display: flex;
  justify-content:flex-start;
}

.videoInfo{
  display: flex;
  justify-content: space-between;
}
.videos {
  width: 40%;
  height: 50%;
  border: thick double yellow;
}
li{
  list-style: none;
}
.vid {
  width: 80%;
  height: 20%;
}

el-button {
  display: inline-block;
  line-height: 1;
  white-space: nowrap;
  cursor: pointer;
  background: #FFF;
  border: 1px solid #DCDFE6;
  color: #606266;
  -webkit-appearance: none;
  text-align: center;
  box-sizing: border-box;
  outline: 0;
  margin: 0;
  transition: .1s;
  font-weight: 500;
  padding: 12px 20px;
  font-size: 14px;
  border-radius: 4px;
  width: 100%;
}
</style>