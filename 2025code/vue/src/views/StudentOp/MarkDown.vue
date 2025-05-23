<template>
  <div>
    <router-link to="/studentweb/checkhomework">
      <el-button type="success">返回</el-button>
    </router-link>
    <div>

      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRef"
          :mode="mode"
      />
      <Editor
          style="height:300px;overflow-y:hidden; "
          v-if="this.homework.content"
          v-model="this.homework.content"
          :mode="mode"
          :defaultConfig="editorConfig"
          @onCreated="handleCreated"
      />
    </div>
    <div class="last">

      评分
      <el-input :disabled="homework.mode === '1'" placeholder="请输入内容" v-model="homework.score">

      </el-input>
      <span style="margin-top: 10px;display: block;"> 备注</span>

      <el-input :disabled="homework.mode === '1'" type="textarea" :rows="3" style="" placeholder="请输入"
                v-model="homework.remark">
      </el-input>
    </div>
  </div>
</template>

<script>

import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import '@wangeditor/editor/dist/css/style.css'
export default {
  name: "MarkDown",
  data() {
    return {
      formData: '123',
      homework: {},
      type: "zuoye",
      content: ''
    }
  },
  components: { Toolbar, Editor },
  setup() {

    // 编辑器实例
    const editorRef = shallowRef()  // 正题编辑器实例
    const mode = ref('default')
    const editorConfig = reactive({
      MENU_CONF: {
        uploadImage: {
          headers: {
            token: JSON.parse(localStorage.getItem('code_user') || '{}').token,
          },
          server: 'http://localhost:9999/files/wang/upload',
          fieldName: 'file'
        }
      }
    })

    // 组件销毁时销毁编辑器
    onBeforeUnmount(() => {
      // 销毁正题编辑器
      if (editorRef.value) {
        editorRef.value.destroy()
      }
      // 销毁参考答案编辑器
    })

    // 编辑器创建回调
    const handleCreated = (editor, type) => { // 正确定义函数
      if (type === 'content') {
        editorRef.value = editor
      }
    }

    return {
      editorRef,
      mode,
      editorConfig,
      handleCreated
    }
  },
  created() {
    console.log("params of homeworks:",this.$route.query.homework)
    // 1. 检查参数是否存在
    if (this.$route.query.homework) {
      // 2. 解码 URI 组件
      const decodedHomework = decodeURIComponent(this.$route.query.homework);
      // 3. 解析 JSON 字符串为对象
      this.homework = JSON.parse(decodedHomework);
      console.log("contenthhhh:",this.homework)
    }
    console.log("created");
   // console.log("params:",this.$route.params.homework);
  //  this.homework = this.$route.params.homework
    console.log("this.homework:",this.homework);
    // 4. 获取类型参数
    this.type = this.$route.query.type || 'zuoye';
    console.log(this.type);
    if (this.type === 'zuoye') {
      console.log("content:",this.homework.content)
      this.content = this.homework.content
    } else {
      this.content = this.homework.answer
    }

  }
}
</script>

<style scoped>
/* /deep/ .auto-textarea-wrapper .auto-textarea-input{
    height: 60vh;
    overflow-y: auto;
} */

.last {
  width: 95%;
  height: 200px;
  margin: 20px auto;

}
</style>