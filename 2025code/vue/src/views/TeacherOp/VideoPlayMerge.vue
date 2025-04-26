<template>
  <div class="release_wrap" style="width:1000px">
    <div class="release_title">播 放 视 频</div>
    <el-card class="release_card">
      <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
                 @click="jump_home">返回首页</el-button>

      <el-table stripe :data="state.tableData" style="width: 100%" height="600px">
        <el-table-column prop="videoName" label="视频名称" min-width="280">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="playVideo(scope.$index, scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :modal="false" title="视频播放" :visible.sync="state.dialogVisible" width="40%">
      <p class="video_title">{{ state.videoName }}</p>
      <video :src="state.videoUrl" controls="controls" width="100%"></video>

    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import request from "@/utils/request.js"

// const baseUrl = request.baseUrl;
const router = useRouter()
const baseUrl = request.baseUrl;
// 响应式状态
const state = reactive({
  title:"",
  videolist:"",
  tableData: [],
  dialogVisible: false,
  videoId: 0,
  videoName: '',
  videoUrl:'',
  mediaSource: null,
  sourceBuffer: null,
  chunkSize: 0,
  totalSize: 0,
})

// 视频元素引用
const videoRef = ref(null)

// 生命周期
onMounted(async () => {
  await getVideoInfo()
})

onBeforeUnmount(() => {
  // 清理 MediaSource
  if (state.mediaSource) {
    URL.revokeObjectURL(videoRef.value.src)
    state.mediaSource = null
  }
})

// 方法
const jump_home = () => {
  router.push('/manager/upload')
}

const getVideoInfo = () => {
  request.get('/SelectVideo/table').then((res) => {
    console.log('res:', res)
    this.tableData = res.data;
  })
}

const playVideo = async (i,val) => {
  // 显示弹框
  state.dialogVisible = true;
  // 保存视频名字
  state.videoName = val.videoName;
  // 保存视频id
  state.videoId = val.id;
  // 发送HEAD请求获取视频的总大小
  try {
    const res = await request.get('/SelectVideo/getVideoSizeById/' + state.videoId);
    state.totalSize = res.data;
    state.chunkSize = Math.ceil(state.totalSize / 20); // 设置分片大小为总大小的1/20

    // 创建 MediaSource 并为视频元素设置 URL
    state.mediaSource = new MediaSource();
    state.videoUrl = URL.createObjectURL(state.mediaSource);

    // 监听 MediaSource 的 sourceopen 事件，准备开始加载分片
      state.mediaSource.addEventListener('sourceopen', () => {
      state.sourceBuffer = state.mediaSource.addSourceBuffer('video/mp4; codecs="avc1.42E81E, mp4a.40.2"');
      state.sourceBuffer.addEventListener('updateend', () => { this.loadAndPlayChunks })
      loadAndPlayChunks(0, state.totalSize - 1); // 加载所有分片
    });
  } catch (error) {
    console.error('Failed to get video size:', error);
  }
}

const loadAndPlayChunks = (startByte, endByte) =>{
  const sourceBuffer = state.sourceBuffer;
  const videoId = state.videoId;

  const loadNextChunk = (nextStartByte) => {
    if (nextStartByte >= endByte) {
      return; // 已加载所有分片，退出循环
    }

    // 获取下一个分片的字节范围
    const nextEndByte = Math.min(nextStartByte + state.chunkSize - 1, endByte);

    // 获取分片数据

   request.get('/SelectVideo/policemen/' + state.videoId, {
          headers: {
            Range: `bytes=${nextStartByte}-${nextEndByte}`,
          },
          responseType: 'arraybuffer',
        })
        .then((response) => {
          const chunk = response.data;

          // 将分片追加到 SourceBuffer
          sourceBuffer.appendBuffer(chunk)

          // 递归调用加载下一个分片
          loadNextChunk(nextEndByte + 1);
        })
        .catch((error) => {
          console.error('Failed to load video:', error);
        });

  };

  // 从第一个分片开始加载
  loadNextChunk(startByte);
}

// const getVideoProgress = () => {
//   if (!videoRef.value) return
//   console.log('当前进度:', videoRef.value.currentTime)
// }
</script>
<style></style>
