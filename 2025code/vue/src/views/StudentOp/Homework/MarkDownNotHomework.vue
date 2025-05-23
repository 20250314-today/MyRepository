
<template>
  <div>
    <div>
      <div> <el-button type="success" @click="goOthers()">返回</el-button>

        <el-button type="success" @click="submit(homdeworkData)">提交</el-button></div>
        <div style="width: 55%;">
          <div>
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRefContent"
                :mode="mode"
            />
            <Editor
                style="height:300px;overflow-y:hidden;"
                v-model="homdeworkData.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="(editor) => handleCreated(editor, 'content')"
            />
          </div>
        </div>
    </div>
  </div>
</template>
<script>
import Cookies from "js-cookie";
import request from "@/utils/request.js"
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, onMounted, reactive, ref, shallowRef,} from "vue";

;
export default {
  name: "MarkDownNotHomeWork",
  data(){
    return{
      homdeworkData:{
        content:'',
        homeworkId:'',
        exerciseId:'',
        userId:''
      },
      tag:'',
    }
  },
  props: {
    id: { type: String, required: true },
    content: { type: String, required: true },
    data2: { type: String, default: '' }
  },
  components: { Toolbar, Editor },
  setup() {
    const editorContainer = ref(null);
    // 编辑器实例
    const editorRefContent = shallowRef()  // 正题编辑器实例
    const editorRefAnswer = shallowRef()   // 参考答案编辑器实例
    const mode = ref('default')
    const editorConfig = reactive({
      EXTEND_CONF: {
        hoverbarKeys: {
          link: {
            zIndex: 10000 , // 提升悬停栏层级
            offset: [0, 10] // 调整悬停栏偏移量
          }
        }
      },
      MENU_CONF: {
        uploadImage: {
          headers: {
            token: JSON.parse(localStorage.getItem('code_user') || '{}').token,
          },
          server: 'http://localhost:9999/files/wang/upload',
          fieldName: 'file'
        }
      },
      modalAppendToBody: false,
      hoverbar: {
        zIndex: 10050, // 统一悬停层级
        offsetX: 10
      }
    })
    onMounted(() => {
      updateHeight()
      window.addEventListener('resize', updateHeight)
    })
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
      window.removeEventListener('resize', updateHeight)
    })

    // 编辑器创建回调
    const handleCreated = (editor, type) => { // 正确定义函数
      if (type === 'content') {
        editorRefContent.value = editor
      } else if (type === 'answer') {
        editorRefAnswer.value = editor
      }
    }

    return {
      editorContainer,
      editorRefContent ,
      editorRefAnswer ,
      mode,
      editorConfig,
      handleCreated
    }
  },
  created() {
    // 参数校验
    if (!this.$route.query.id || !this.$route.query.content) {
      console.error("缺少必要参数")
      this.$router.push('/error')
      return
    }
    console.log("data1:",this.$route.query.id)
    this.homdeworkData.userId=parseInt(Cookies.get('userId'))
    const decodedContent = decodeURIComponent(this.$route.query.content);
    this.homdeworkData.content = decodedContent.replace(/<\/?p>/g, '');
    console.log("content:",this.$route.query.content)
    this.homdeworkData.homeworkId=this.$route.query.id
    console.log("exersizeid:",this.$route.query.id)
    this.homdeworkData.exerciseId=this.$route.query.id
    this.tag=this.$route.query.data2
    updateHeight()
    window.addEventListener('resize', updateHeight)
  },

  methods:{
    goOthers(){
      this.$router.go(-1)

    },
    submit(home){
      console.log(this.tag,'oooo')
      if(this.tag !=='123'){
        request.post('/study/userDoExercise/save',home).then(resp=>{
          if(resp.code==='200'){
            this.$router.push("/studentweb/practicedetail")
            this.$message({
              message: '恭喜你，已完成练习',
              type: 'success'
            });
            this.tag='999'
          }else{
            this.$message.error('提交练习失败');
          }
        })
      }else{
       request.post('/study/userdohomework/save',home).then(resp=>{
          if(resp.code=== '200'){
            this.$router.push("/studentweb/checkhomework")
            this.$message({
              message: '恭喜你，已完成作业',
              type: 'success'
            });
          }else{
            this.$message.error('提交作业失败');
          }
        })
      }

    }
  }
}
const updateHeight = () => {
  const container = document.querySelector('.editor-wrapper');
  if (container) {
    const viewportHeight = window.innerHeight;
    const buttonHeight = 80; // 按钮和间距总高度
    let newHeight = viewportHeight - buttonHeight;
    // 确保高度不小于300px
    if (newHeight < 300) {
      newHeight = 300;
    }
    container.style.height = `${newHeight}px`;
  }
}
</script>

<style scoped>
/* 增强编辑器容器定位 */
.editor-container {
  position: relative !important;
  contain: layout;
}

/* 修正弹出层定位 */
.w-e-modal-wrapper {
  position: absolute !important;
  top: 50% !important;
  transform: translateY(-50%) !important;
}

.w-e-drop-panel {
  position: absolute !important;
  top: 100% !important;
}
</style>