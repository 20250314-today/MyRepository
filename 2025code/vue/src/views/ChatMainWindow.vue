<template>
  <div class="chat-container">
    <!-- 消息展示区 -->
    <div class="message-list">
<!--      :class="['message', msg.role]：根据消息角色（user或assistant）添加不同的 CSS 类，用于区分用户和 AI 的消息样式。-->
      <div v-for="(msg, index) in messages" :key="index"

           :class="['message', msg.role]">
        <div class="role-tag">{{ msg.role === 'user' ? '我' : 'AI' }}</div>
        <div class="content" v-html="msg.content"></div>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="input-area">
<!--      @keydown.enter.exact.prevent：监听回车键（避免组合键触发），阻止默认换行行为，调用sendMessage方法。-->
<!--      :disabled="isLoading"：发送过程中禁用输入框和按钮，防止重复请求。-->
      <textarea
          v-model="inputMessage"
          @keydown.enter.exact.prevent="sendMessage"
          :disabled="isLoading"
          placeholder="输入消息..."
          rows="2"
      ></textarea>
<!--      canSend计算属性：判断是否允许发送-->
      <button @click="sendMessage" :disabled="!canSend">
        {{ isLoading ? '生成中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import request from"@/utils/request.js"
// 配置信息（建议使用环境变量）
const API_KEY = import.meta.env.VITE_DEEPSEEK_API_KEY || ''
const MODEL = 'deepseek-chat' // 根据文档选择模型

const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
// 新增：定义错误消息状态
const errorMessage = ref('')

const canSend = computed(() => {
  return inputMessage.value.trim() && !isLoading.value
})

const sendMessage = async () => {
  if (!canSend.value) return
  errorMessage.value = '' // 重置错误信息

  try {
    isLoading.value = true
    const userMessage = inputMessage.value.trim()
    inputMessage.value = ''

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: userMessage
    })
// 打印请求信息（调试用）
    console.log('发送请求:', {
      model: MODEL,
      messages: [...messages.value, { role: 'user', content: userMessage }],
      temperature: 0.7,
      max_tokens: 1000
    })
    // 调用API
    const response = await request.post('/api/chat', {
      model: MODEL,
      messages: [
        ...messages.value,
        { role: 'user', content: userMessage }
      ],
      temperature: 0.7,
      max_tokens: 1000
    }, {
      headers: {
        'Authorization': `Bearer ${API_KEY}`,
        'Content-Type': 'application/json'
      }
    })
    // 打印完整响应用于调试
    console.log('API响应:', response)
// 验证API响应结构
    if (
        !response ||
        !response.choices ||
        !response.choices[0] ||
        !response.choices[0].message ||
        !response.choices[0].message.content
    ) {
      throw new Error('API返回格式不符合预期')
    }
    // 添加AI回复
    const aiResponse = JSON.stringify(response.choices[0].message.content)
    messages.value.push({
      role: 'assistant',
      content: aiResponse
    })

  } catch (error) {
    console.error('API请求失败:', error)
    messages.value.push({
      role: 'assistant',
      content: '抱歉，请求出现错误，请稍后再试'
    })
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.chat-container {
  width: 900px;
  margin: 20px auto;
  height: 80vh;
  display: flex;
  flex-direction: column;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #f9fafb;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 80%;
  align-self: flex-start;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.role-tag {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #3b82f6;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.message.user .role-tag {
  background: #10b981;
}

.content {
  padding: 12px 16px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.message.user .content {
  background: #3b82f6;
  color: white;
}

.input-area {
  padding: 20px;
  border-top: 1px solid #e5e7eb;
  display: flex;
  gap: 12px;
}

textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  resize: none;
}

button {
  padding: 0 24px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: opacity 0.2s;
}

button:disabled {
  background: #9ca3af;
  cursor: not-allowed;
  opacity: 0.7;
}
</style>