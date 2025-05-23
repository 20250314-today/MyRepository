<template>
  <div>
    <h2>学习时长统计</h2>
    <p>您已学习：{{ formatTime(learningTime) }}</p>
    <button @click="togglePause">{{ isPaused? '继续学习' : '暂停学习' }}</button>
    <button @click="resetTime">重置学习时长</button>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import request from '@/utils/request.js';

// 记录学习时长（以毫秒为单位）
const learningTime = ref(0);
// 记录开始时间
let startTime;
// 定时器 ID
let timer;
// 暂停状态
const isPaused = ref(false);
// 假设用户 ID，实际应用中从登录等获取
const userId = 'user123';

// 获取初始学习时长
const fetchInitialTime = async () => {
  try {
    const response = await request.get(`/learning-time/get?userId=${userId}`);
    learningTime.value = response.data;
  } catch (error) {
    console.error('获取学习时长失败', error);
  }
};

// 格式化时间函数
const formatTime = (milliseconds) => {
  const totalSeconds = Math.floor(milliseconds / 1000);
  const hours = Math.floor(totalSeconds / 3600).toString().padStart(2, '0');
  const minutes = Math.floor((totalSeconds % 3600) / 60).toString().padStart(2, '0');
  const seconds = (totalSeconds % 60).toString().padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
};

// 开始计时
const startTimer = () => {
  startTime = new Date();
  timer = setInterval(() => {
    const currentTime = new Date();
    learningTime.value += currentTime - startTime;
    // 定期保存学习时长到后端
    saveLearningTime();
    startTime = currentTime;
  }, 1000);
};

// 暂停计时
const pauseTimer = () => {
  clearInterval(timer);
  saveLearningTime();
};

// 保存学习时长到后端
const saveLearningTime = async () => {
  try {
    await request.post(`/learning-time/save`, { userId, duration: learningTime.value });
  } catch (error) {
    console.error('保存学习时长失败', error);
  }
};

// 切换暂停/继续状态
const togglePause = () => {
  if (isPaused.value) {
    startTimer();
  } else {
    pauseTimer();
  }
  isPaused.value =!isPaused.value;
};

// 重置学习时长
const resetTime = async () => {
  clearInterval(timer);
  learningTime.value = 0;
  try {
    // 可以在后端实现重置逻辑，这里简单设置为 0 并保存
    await request.post(`/learning-time/save`, { userId, duration: 0 });
  } catch (error) {
    console.error('重置学习时长失败', error);
  }
  isPaused.value = false;
};

onMounted(() => {
  fetchInitialTime();
  if (!isPaused.value) {
    startTimer();
  }
});

onUnmounted(() => {
  clearInterval(timer);
  saveLearningTime();
});
</script>

<style scoped>
h2 {
  margin-bottom: 10px;
}
button {
  margin-right: 10px;
  padding: 5px 10px;
}
</style>