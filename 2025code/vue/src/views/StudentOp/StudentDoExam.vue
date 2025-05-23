<template>
  <div class="exam-container">
    <h1>{{ exam.title }}</h1>
    <div class="timer">剩余时间: {{ formatTime(remainingTime) }}</div>

    <div v-for="(q, index) in exam.questions" :key="q.id">
      <div class="question">
        <h3>第{{ index+1 }}题 ({{ q.score }}分)</h3>
        <p>{{ q.content }}</p>

        <div v-if="q.type === 'single'">
          <el-radio-group v-model="answers[q.id]">
            <el-radio
                v-for="opt in q.options"
                :key="opt.id"
                :label="opt.id"
            >
              {{ opt.content }}
            </el-radio>
          </el-radio-group>
        </div>

        <div v-else-if="q.type === 'multiple'">
          <el-checkbox-group v-model="answers[q.id]">
            <el-checkbox
                v-for="opt in q.options"
                :key="opt.id"
                :label="opt.id"
            >
              {{ opt.content }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <div v-else>
          <el-input v-model="answers[q.id]"></el-input>
        </div>
      </div>
    </div>

    <el-button type="primary" @click="submit">提交试卷</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
const props = defineProps(['examId'])
const exam = ref({})
const answers = ref({})
const remainingTime = ref(0)
let timer = null

// 获取试卷详情
const loadExam = async () => {
  const res = await request.get(`/api/exams/${props.examId}`)
  exam.value = res.data
  remainingTime.value = exam.value.duration * 60 // 转换为秒
}

// 倒计时
const startTimer = () => {
  timer = setInterval(() => {
    if(remainingTime.value > 0) {
      remainingTime.value--
    } else {
      submit()
    }
  }, 1000)
}

// 提交答案
const submit = async () => {
  await request.post('/api/exams/submit', {
    examId: props.examId,
    answers: answers.value
  })
  ElMessage.success('提交成功')
}

onMounted(() => {
  loadExam()
  startTimer()
})

onBeforeUnmount(() => {
  clearInterval(timer)
})
</script>