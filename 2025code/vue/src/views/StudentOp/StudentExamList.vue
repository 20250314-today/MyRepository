<template>
  <div class="container mx-auto px-4 py-8">
    <div class="bg-white rounded-lg shadow-lg p-6 mb-6">
      <h1 class="text-2xl font-bold mb-4">我的考试</h1>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="exam in availableExams" :key="exam.id" class="bg-white border border-gray-200 rounded-lg shadow hover:shadow-md transition-all duration-300">
          <div class="p-6">
            <h3 class="text-xl font-semibold mb-2">{{ exam.title }}</h3>
            <p class="text-gray-600 mb-4 line-clamp-2">{{ exam.description }}</p>

            <div class="mb-4">
              <div class="flex items-center mb-2">
                <i class="fa fa-calendar text-gray-400 mr-2"></i>
                <span class="text-sm text-gray-600">开始时间: {{ formatDateTime(exam.startTime) }}</span>
              </div>
              <div class="flex items-center">
                <i class="fa fa-clock-o text-gray-400 mr-2"></i>
                <span class="text-sm text-gray-600">结束时间: {{ formatDateTime(exam.endTime) }}</span>
              </div>
            </div>

            <div class="flex justify-between items-center">
              <span class="px-3 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                {{ getExamStatusText(exam) }}
              </span>

              <button @click="startExam(exam.id)" class="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-lg transition duration-200 ease-in-out transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                      :disabled="!canStartExam(exam)">
                进入考试
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="examHistory.length > 0" class="bg-white rounded-lg shadow-lg p-6 mb-6">
      <h2 class="text-xl font-semibold mb-4">考试历史</h2>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead>
          <tr>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">考试标题</th>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">开始时间</th>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">结束时间</th>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">得分</th>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
            <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="record in examHistory" :key="record.id">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900">{{ record.exam.title }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900">{{ formatDateTime(record.startTime) }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900">{{ formatDateTime(record.endTime) }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900">{{ record.score || '未完成' }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                      :class="getStatusColorClass(record.status)">
                  {{ getRecordStatusText(record.status) }}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
              <button class="text-blue-500 hover:text-blue-700" :disabled="record.status !== 'COMPLETED'">
                查看详情
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const availableExams = ref([]);
const examHistory = ref([]);
const studentId = ref(1); // 实际应从认证信息中获取

const formatDateTime = (dateTime) => {
  if (!dateTime) return '';
  return new Date(dateTime).toLocaleString();
};

const getExamStatusText = (exam) => {
  const now = new Date();
  if (now < new Date(exam.startTime)) {
    return '未开始';
  } else if (now > new Date(exam.endTime)) {
    return '已结束';
  } else {
    return '进行中';
  }
};

const canStartExam = (exam) => {
  const now = new Date();
  return now >= new Date(exam.startTime) && now <= new Date(exam.endTime);
};

const getRecordStatusText = (status) => {
  switch (status) {
    case 'NOT_STARTED': return '未开始';
    case 'IN_PROGRESS': return '进行中';
    case 'COMPLETED': return '已完成';
    case 'OVERDUE': return '已逾期';
    case 'SUBMITTED': return '已提交';
    default: return '未知状态';
  }
};

const getStatusColorClass = (status) => {
  switch (status) {
    case 'NOT_STARTED': return 'bg-gray-100 text-gray-800';
    case 'IN_PROGRESS': return 'bg-yellow-100 text-yellow-800';
    case 'COMPLETED': return 'bg-green-100 text-green-800';
    case 'OVERDUE': return 'bg-red-100 text-red-800';
    case 'SUBMITTED': return 'bg-blue-100 text-blue-800';
    default: return 'bg-gray-100 text-gray-800';
  }
};

const fetchAvailableExams = async () => {
  try {
    const response = await axios.get('/api/student/exam/available', {
      params: { studentId: studentId.value }
    });
    availableExams.value = response.data;
  } catch (error) {
    console.error('获取可用考试失败:', error);
  }
};

const fetchExamHistory = async () => {
  try {
    const response = await axios.get('/api/student/exam/history', {
      params: { studentId: studentId.value }
    });
    examHistory.value = response.data;
  } catch (error) {
    console.error('获取考试历史失败:', error);
  }
};

const startExam = (examId) => {
  // 导航到考试页面
  window.location.href = `/student/exam/${examId}`;
};

onMounted(() => {
  fetchAvailableExams();
  fetchExamHistory();
});
</script>

<style scoped>
.exam-card {
  transition: all 0.3s ease;
}

.exam-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
}
</style>