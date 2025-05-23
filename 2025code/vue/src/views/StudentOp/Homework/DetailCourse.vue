<template>
  <div>
    <router-link to="/askandanswer">
      <el-button type="primary">返回</el-button>
    </router-link>

    <h1>所有课程信息</h1>
    <div class="block">
      <el-timeline>
        <el-timeline-item timestamp="2025/4/12" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2025/4/12 20:46</p>
            <div >
              <el-badge :value="12" class="item">
                <el-button size="small">提问</el-button>
              </el-badge>
            </div>
            <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入内容"
                v-model="textarea">
            </el-input>
            <el-button size="small" class="center" type="success">发布</el-button>
          </el-card>
        </el-timeline-item>

      </el-timeline>
    </div>
  </div>

</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js"
const data = reactive({
      textarea: ''
  })
</script>

<style scoped>
.center{
  display: flex;
  justify-content: center;
}
</style>