<template>
  <div class="chat-container">
    <textarea v-model="inputText"></textarea>
    <button @click="handleChat" :disabled="!inputText || isStreaming">
      {{ isStreaming ? '生成中...' : '开始对话' }}
    </button>
    <div class="answer-box">
      <div v-if="isError" class="error-message">{{ errorMessage }}</div>
      <div v-else>{{ answer }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue';
import EventSourcePolyfill from 'eventsource-polyfill';

const inputText = ref('');
const answer = ref('');
const isStreaming = ref(false);
const isError = ref(false);
const errorMessage = ref('');

const handleChat = async () => {
  try {
    isStreaming.value = true;
    isError.value = false;
    errorMessage.value = '';
    answer.value = '';

    const eventSource = new EventSourcePolyfill(`/api/spark/chat-stream`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ content: inputText.value })
    });

    eventSource.onmessage = (event) => {
      try {
        // 假设event.data是JSON字符串，进行解析
        const data = JSON.parse(event.data);
        answer.value += data.message; // 假设后端返回的JSON中有message字段
      } catch (parseErr) {
        answer.value += event.data;
      }
    };

    eventSource.onerror = (error) => {
      eventSource.close();
      isStreaming.value = false;
      isError.value = true;
      errorMessage.value = '对话过程中发生错误，请重试';
    };

    onBeforeUnmount(() => {
      eventSource.close();
    });
  } catch (err) {
    console.error('对话失败:', err);
    isError.value = true;
    errorMessage.value = '对话初始化失败，请检查网络连接';
    isStreaming.value = false;
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.answer-box {
  border: 1px solid #ccc;
  padding: 10px;
  min-height: 100px;
}

.error-message {
  color: red;
}
</style>