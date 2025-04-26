<template>
  <div class="es">
    <router-link to="/practicedetail"> <el-button >返回</el-button></router-link>
    <p>试题</p>
    <div style="width: 45%;">
      <mavon-editor
          :subfield="false"
          :autofocus="false"
          v-model="data.paramData.reply"
          ref="md"
          style="width: 99%;"
      >
      </mavon-editor>
    </div>
    <p> 参考答案</p>
    <div style="width: 45%">
      <mavon-editor
          :subfield="false"
          :autofocus="false"
          v-model="data.paramData.answer"
          ref="md"
          style="width: 99%;"
      >
      </mavon-editor>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js"
 const data = reactive({
   paramData: {
     reply: '',
     answer: '',
   }
 })
 const created = () =>{
   data.paramData.reply=this.$route.params.data2.reply
   data.paramData.answer=this.$route.params.data2.answer
}
</script>

<style scoped>
.es{
  display: flex;
  justify-content: space-between;
}
</style>