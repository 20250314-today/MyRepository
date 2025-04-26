<template>
  <div class="release_wrap">
    <div class="release_title">播 放 视 频</div>
    <el-card class="release_card">
      <el-button type="primary" round icon="el-icon-arrow-left" style="margin-bottom: 40px"
                 @click="jump_home">返回首页</el-button>

      <el-table stripe :data="tableData" style="width: 100%" height="600px">
        <el-table-column prop="videoName" label="视频名称" min-width="280">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="playVideo(scope.$index, scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :modal="false" title="视频播放" :visible.sync="dialogVisible" width="40%">
      <p class="video_title">{{ videoName }}</p>
      <video :src="videoUrl" controls="controls" width="100%" @canplay="getVidDur()" id="myvideo"></video>
    </el-dialog>
  </div>
</template>