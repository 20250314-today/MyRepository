<template>
  <div class="es">
<!--     <el-button @click="router.push('/studentweb/practicedetail')" >返回</el-button>-->
    <p>试题</p>
    <div style="width: 45%;">
<!--      <mavon-editor-->
<!--          :subfield="false"-->
<!--          :autofocus="false"-->
<!--          v-model="data.paramData.reply"-->
<!--          ref="md"-->
<!--          style="width: 99%;"-->
<!--      >-->
<!--      </mavon-editor>-->
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRefContent"
          :mode="mode"
      />
      <Editor
          style="height:300px;overflow-y:hidden;"
          v-if="data.paramData.reply"
          v-model="data.paramData.reply"
          :mode="mode"
          :defaultConfig="editorConfig"
          @onCreated="(editor) => handleCreated(editor, 'content')"
      />
    </div>
    <p> 参考答案</p>
    <div style="width: 45%">
<!--      <mavon-editor-->
<!--          :subfield="false"-->
<!--          :autofocus="false"-->
<!--          v-model="data.paramData.answer"-->
<!--          ref="md"-->
<!--          style="width: 99%;"-->
<!--      >-->
<!--      </mavon-editor>-->
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRefAnswer"
          :mode="mode"
      />
      <Editor
          style="height:300px;overflow-y:hidden;"
          v-if="data.paramData.answer"
          v-model="data.paramData.answer"
          :mode="mode"
          :defaultConfig="editorConfig"
          @onCreated="(editor) => handleCreated(editor, 'answer')"
      />
    </div>
  </div>
  <div style="cflex:1;margin:10px;background-color: #f2f4ff;padding:10px">
    <RouterView @updateUser="updateUser"/>
  </div>
</template>

<script setup>
import {reactive, onMounted, shallowRef, onBeforeUnmount, ref} from "vue";
import { useRoute } from "vue-router";
import request from "@/utils/request.js";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue" // 正确包名
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import router from "@/router/index.js";


const route = useRoute(); // 使用 useRoute 获取路由对象
 const data = reactive({
   user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
   paramData: {
     reply: '',
     answer: '',
   }
 })
const updateUser =()=>{
  data.user = JSON.parse(localStorage.getItem("code_user")||'{}')
}
// 编辑器实例
const editorRefContent = shallowRef()  // 正题编辑器实例
const editorRefAnswer = shallowRef()   // 参考答案编辑器实例
const mode='default'
const editorConfig = {MENU_CONF:{}}
editorConfig.MENU_CONF['uploadImage']={
  headers:{
    token:data.user.token,
  },
  server:'http://localhost:9999/files/wang/upload',//服务端图片上传接口
  fieldName:'file'
}
// 编辑器创建回调
const handleCreated = (editor, type) => { // 正确定义函数
  if (type === 'content') {
    editorRefContent.value = editor
  } else if (type === 'answer') {
    editorRefAnswer.value = editor
  }
}
// 组件销毁时销毁编辑器
onBeforeUnmount(() => {
  // 销毁正题编辑器
  if (editorRefContent.value) {
    editorRefContent.value.destroy()
  }
  // 销毁参考答案编辑器
  if (editorRefAnswer.value) {
    editorRefAnswer.value.destroy()
  }
})
const load = () => {
  try {
    // 1. 检查参数是否存在
    if (!route.params.data2) {
      throw new Error("路由参数 data2 缺失");
    }

    // 2. 解码 URI 组件（如果跳转时编码过）
    const decodedData = decodeURIComponent(route.params.data2);

    // 3. 解析 JSON
    const data2 = JSON.parse(decodedData);
    console.log("解析后的 data2:", data2);

    // 4. 更新响应式数据
    data.paramData.reply = data2.reply || "";
    data.paramData.answer = data2.answer || "";
  } catch (error) {
    console.error("加载数据失败:", error.message);
    // 重置为默认值
    data.paramData.reply = "";
    data.paramData.answer = "";
  }
};
// 在组件挂载时调用 load
onMounted(() => {
  load();
});
</script>

<style scoped>
.es{
  display: flex;
  justify-content: space-between;
}
</style>