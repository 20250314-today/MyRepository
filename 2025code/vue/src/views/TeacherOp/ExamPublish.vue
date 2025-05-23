<template>
  <div class="exam-form" style="width:800px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="试卷标题">
        <el-input v-model="form.title"></el-input>
      </el-form-item>

      <div v-for="(q, index) in form.questions" :key="index">
        <h3>题目 {{ index+1 }}</h3>
        <el-form-item label="题目内容">
          <el-input type="textarea" v-model="q.content"></el-input>
        </el-form-item>

        <el-form-item label="题目类型">
          <el-select v-model="q.type">
            <el-option label="单选题" value="single"></el-option>
<!--            <el-option label="多选题" value="multiple"></el-option>-->
            <el-option label="填空题" value="fill"></el-option>
          </el-select>
        </el-form-item>

        <div v-if="q.type !== 'fill'">
          <div v-for="(opt, oIndex) in q.options" :key="oIndex">
            <el-input v-model="opt.content" placeholder="选项内容"></el-input>
            <el-checkbox v-model="opt.isCorrect">正确答案</el-checkbox>
          </div>
          <el-button @click="q.options.push({content: '', isCorrect: false})">添加选项</el-button>
        </div>
      </div>

      <el-button @click="form.questions.push({type:'single', options:[]})">添加题目</el-button>
      <el-button type="primary" @click="submitForm">提交试卷</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
const form = reactive({
  title: '',
  questions: []
})

const submitForm = async () => {
  try {
    await request.post('/api/exams', form)
    ElMessage.success('试卷创建成功')
  } catch (error) {
    ElMessage.error('提交失败')
  }
}
</script>