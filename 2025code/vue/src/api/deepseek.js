import axios from 'axios';

// 替换为你的DeepSeek API密钥（从官网控制台获取）
const API_KEY = 'sk-0981a2511137492d8dd5ca819db272de';
const API_ENDPOINT = 'https://api.deepseek.com';

export const sendDeepSeekRequest = async (prompt, signal) => {
    const config = {
        method: 'post',
        url: API_ENDPOINT,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${API_KEY}`
        },
        data: {
            model: 'deepseek-chat', // 使用DeepSeek基础模型
            messages: [{ role: 'user', content: prompt }],
            stream: true, // 启用流式响应（逐字输出）
            temperature: 0.8, // 控制回复创造性（0-1）
            max_tokens: 512 // 最大回复长度
        },
        signal, // 用于取消请求
        responseType: 'stream' // 处理流式数据
    };
    return axios(config);
};