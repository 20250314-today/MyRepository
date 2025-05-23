<template>
  <div>
    <el-button @click="router.push('/manager/scoremanagement')" type="success">返回</el-button>
    <div style="">
      <mavon-editor
          :subfield="false"
          :autofocus="false"
          v-model="data.content"
          ref="md"
          style="width: 99%; height: 90vh"
      />
    </div>

    <!-- 数据渲染区域 -->
    <div style="flex: 1; margin: 10px; background-color: #f2f4ff; padding: 10px">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script>
import { reactive, onMounted } from "vue";
import { useRoute } from "vue-router";
import router from "@/router";
import { ElMessage } from "element-plus";

export default {
  name: "DetailMark",
  setup() {
    const data = reactive({
      user: JSON.parse(localStorage.getItem('code_user') || "{}"),
      content: ""
    });

    const updateUser = () => {
      data.user = JSON.parse(localStorage.getItem("code_user") || '{}');
    };

    onMounted(() => {
      const route = useRoute();

      data.content = decodeURIComponent(route.params.content || "")
          .replace(/&nbsp;/g, " ")
          .replace(/&lt;/g, "<")
          .replace(/&gt;/g, ">");
    });
    console.log("content:",data.content)
    return { data, router, updateUser };
  }
};
</script>