<template>
  <div class="anime-chat-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="star star-1">✨</div>
      <div class="star star-2">⭐</div>
      <div class="star star-3">💫</div>
      <div class="heart heart-1">💖</div>
      <div class="heart heart-2">💕</div>
      <div class="sparkle sparkle-1">✧</div>
      <div class="sparkle sparkle-2">✦</div>
    </div>

    <div class="chat-header">
      <div class="header-avatar">
        <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=AIAssistant" alt="AI Avatar" />
      </div>
      <div class="header-info">
        <h2>AI 助手酱 (◕‿◕)</h2>
        <p class="status">在线 | 随时为你服务~</p>
      </div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(message, index) in messages" 
        :key="index" 
        :class="['message', message.role]"
      >
        <div class="avatar">
          <img 
            :src="message.role === 'user' ? 'https://api.dicebear.com/7.x/avataaars/svg?seed=User' : 'https://api.dicebear.com/7.x/avataaars/svg?seed=AIAssistant'" 
            :alt="message.role === 'user' ? 'User' : 'AI'" 
          />
        </div>
        <div class="message-bubble">
          <div class="message-content">
            <p>{{ message.content }}</p>
          </div>
          <div class="message-time">{{ formatTime(message.timestamp) }}</div>
        </div>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="message ai">
        <div class="avatar">
          <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=AIAssistant" alt="AI" />
        </div>
        <div class="message-bubble loading">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <div class="input-wrapper">
        <input 
          v-model="inputMessage" 
          @keyup.enter="sendMessage"
          placeholder="输入消息和AI酱聊天吧~ (｡•̀ᴗ-)✧"
          :disabled="loading"
        />
        <button @click="sendMessage" :disabled="loading || !inputMessage.trim()">
          <span>发送</span>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M22 2L11 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'

const inputMessage = ref('')
const messages = ref([])
const loading = ref(false)
const messagesContainer = ref(null)

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || loading.value) return
  
  const userMessage = inputMessage.value.trim()
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: userMessage,
    timestamp: new Date()
  })
  
  // 清空输入框
  inputMessage.value = ''
  
  // 显示加载状态
  loading.value = true
  
  try {
    // 调用API获取AI回复
    const response = await fetch(`http://localhost:10086/api/ai/stream?message=${encodeURIComponent(userMessage)}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    // 处理流式响应
    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let aiResponse = ''
    
    // 添加AI消息占位符
    const aiMessageIndex = messages.value.length
    messages.value.push({
      role: 'ai',
      content: '',
      timestamp: new Date()
    })
    
    // 读取流数据
    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      
      const chunk = decoder.decode(value, { stream: true })
      aiResponse += chunk
      
      // 更新AI消息内容
      messages.value[aiMessageIndex].content = aiResponse
      await scrollToBottom()
    }
    
  } catch (error) {
    console.error('Error sending message:', error)
    messages.value.push({
      role: 'ai',
      content: '抱歉，发生错误，请稍后重试。(＞﹏＜)',
      timestamp: new Date()
    })
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}
</script>

<style scoped>
/* 二次元风格聊天容器 */
.anime-chat-container {
  position: relative;
  display: flex;
  flex-direction: column;
  height: 85vh;
  width: 900px;
  max-width: 90vw;
  margin: 20px auto;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(138, 43, 226, 0.3);
  background: linear-gradient(135deg, #fff5f9 0%, #f0f8ff 100%);
  border: 3px solid #ffb6c1;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.star, .heart, .sparkle {
  position: absolute;
  font-size: 20px;
  opacity: 0.7;
  animation: float 6s infinite ease-in-out;
}

.star-1 { top: 10%; left: 5%; animation-delay: 0s; }
.star-2 { top: 20%; right: 10%; animation-delay: 1s; }
.star-3 { bottom: 30%; left: 15%; animation-delay: 2s; }
.heart-1 { top: 40%; right: 20%; animation-delay: 3s; }
.heart-2 { bottom: 20%; right: 5%; animation-delay: 4s; }
.sparkle-1 { top: 60%; left: 10%; animation-delay: 5s; }
.sparkle-2 { bottom: 10%; left: 25%; animation-delay: 6s; }

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(10deg); }
}

/* 头部样式 */
.chat-header {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background: linear-gradient(to right, #ff9a9e, #fad0c4);
  border-bottom: 3px solid #ffb6c1;
  position: relative;
  z-index: 1;
}

.header-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 10px rgba(255, 182, 193, 0.8);
}

.header-info {
  margin-left: 15px;
}

.header-info h2 {
  margin: 0;
  color: #fff;
  font-size: 1.4em;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  font-weight: bold;
}

.status {
  margin: 5px 0 0;
  color: #fff;
  font-size: 0.9em;
  opacity: 0.9;
}

/* 消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  position: relative;
  z-index: 1;
}

.message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  max-width: 85%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message.ai {
  align-self: flex-start;
}

.avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #ffb6c1;
  box-shadow: 0 0 8px rgba(255, 182, 193, 0.6);
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-width: 60px;
  word-wrap: break-word;
  word-break: break-word;
  overflow-wrap: break-word;
}

.message.user .message-bubble {
  background: linear-gradient(to right, #ff7eb3, #ff758c);
  color: white;
  border-bottom-right-radius: 4px;
}

.message.ai .message-bubble {
  background: white;
  color: #333;
  border: 2px solid #ffd1dc;
  border-bottom-left-radius: 4px;
}

.message-content p {
  margin: 0;
  line-height: 1.5;
  word-wrap: break-word;
}

.message-time {
  font-size: 0.75em;
  opacity: 0.7;
  margin-top: 5px;
  text-align: right;
}

/* 加载动画 */
.typing-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 5px 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ff7eb3;
  animation: typing 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

/* 输入区域 */
.chat-input {
  padding: 15px 20px;
  background: rgba(255, 255, 255, 0.9);
  border-top: 3px solid #ffb6c1;
  position: relative;
  z-index: 1;
}

.input-wrapper {
  display: flex;
  gap: 10px;
}

.chat-input input {
  flex: 1;
  padding: 12px 20px;
  border: 2px solid #ffd1dc;
  border-radius: 25px;
  outline: none;
  font-size: 15px;
  color: #333;
  background: white;
  transition: all 0.3s ease;
}

.chat-input input::placeholder {
  color: #aaa;
}

.chat-input input:focus {
  border-color: #ff7eb3;
  box-shadow: 0 0 10px rgba(255, 126, 179, 0.3);
}

.chat-input button {
  padding: 12px 24px;
  background: linear-gradient(to right, #ff7eb3, #ff758c);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 15px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 126, 179, 0.4);
}

.chat-input button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 126, 179, 0.6);
}

.chat-input button:active:not(:disabled) {
  transform: translateY(0);
}

.chat-input button:disabled {
  background: #ccc;
  cursor: not-allowed;
  box-shadow: none;
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 8px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: linear-gradient(#ff9a9e, #fad0c4);
  border-radius: 10px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(#ff7eb3, #ff758c);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .anime-chat-container {
    height: 90vh;
    margin: 10px;
    border-radius: 15px;
  }
  
  .chat-header {
    padding: 12px 15px;
  }
  
  .header-avatar img {
    width: 40px;
    height: 40px;
  }
  
  .header-info h2 {
    font-size: 1.2em;
  }
  
  .chat-messages {
    padding: 15px;
  }
  
  .message {
    max-width: 90%;
  }
  
  .avatar img {
    width: 35px;
    height: 35px;
  }
  
  .chat-input {
    padding: 12px 15px;
  }
  
  .chat-input input {
    padding: 10px 15px;
    font-size: 14px;
  }
  
  .chat-input button {
    padding: 10px 18px;
    font-size: 14px;
  }
}
</style>