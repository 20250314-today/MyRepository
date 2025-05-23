<template>
  <div>
    <div>
      <div class="cls">
        <div v-if="tag === 'homework'">
          <p>班级:</p>
          <div>
            <el-select v-model="paramData.classId" filterable placeholder="请选择">
              <el-option v-for="item in queryCla" :key="item.classId" :label="item.className"
                         :value="item.classId">
              </el-option>
            </el-select>
          </div>
        </div>
        <div>
          <p>{{ type }}标题：</p>
          <div>
            <el-input style="width: 80%" placeholder="请输入标题" v-model="paramData.title" clearable>
            </el-input>
          </div>
        </div>

        <div v-if="tag !== 'test'">
          <p class="demonstration">完成时间</p>
          <div>
            <el-date-picker  v-model="paramData.commitTime"
                             :value-format="'YYYY-MM-DD HH:mm:ss'"
                             format="YYYY-MM-DD HH:mm:ss"
                             @input="$forceUpdate()"
                             @blur="changeDate"
                             type="datetime"
                             placeholder="选择日期时间"
                             @change="handleDateChange"
            >
            </el-date-picker>
          </div>
        </div>
      </div>
      <div class="ce">


        <div style="width: 35%;">
          <p>正题</p>
          <div>
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRefContent"
                :mode="mode"
            />
            <Editor
                style="height:300px;overflow-y:hidden;"
                v-model="paramData.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="(editor) => handleCreated(editor, 'content')"
            />
          </div>
        </div>

        <div style="width: 35%">
          <p> 参考答案</p>
          <div>
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRefAnswer"
                :mode="mode"
            />
            <Editor
                style="height:300px;overflow-y:hidden;"
                v-model="this.paramData.answer"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="(editor) => handleCreated(editor, 'answer')"
            />
          </div>
        </div>
      </div>
      <div class="last" v-if="check">

        评分
        <el-input :disabled="mode === 0" placeholder="请输入内容" v-model="paramData.score">

        </el-input>
        <span style="margin-top: 10px;display: block;"> 备注</span>

        <el-input :disabled="mode === 1" type="textarea" :rows="3" style="" placeholder="请输入" v-model="paramData.remark">
        </el-input>
      </div>
      <div style="float: right;margin-right: 3%;margin-top: 10px;">
        <el-button type="success" @click="out()">返回</el-button>
        <el-button :disabled="mode === '1'" type="success" @click="addTest(paramData)">提交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import request from "@/utils/request.js"
import { Editor, Toolbar } from "@wangeditor/editor-for-vue"
import '@wangeditor/editor/dist/css/style.css'
import { reactive, onBeforeUnmount, ref, shallowRef, onMounted } from "vue"
import moment from 'moment';
export default {
  name: "Detail",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('code_user') || "{}"),
      check: false,
      mode: 0,
      queryCla: [],
      paramData: {
        id: '',
        classId: '',
        title: '',
        content: '',
        commitTime: '',
        answer: '',
        creator: '',
        userId: '',
        score: 0,
        remark: ""
      },
      param: {
        userId: '',
      },
      tag: '',
      studenttag: '',
      type: "试题",
      viewVisible: false // 新增视图可见性状态
    }
  },
  props: ['type', 'tag', 'data2'], // 声明接收的 props
  components: { Toolbar, Editor },
  setup() {

    // 编辑器实例
    const editorRefContent = shallowRef()  // 正题编辑器实例
    const editorRefAnswer = shallowRef()   // 参考答案编辑器实例
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
      if (editorRefContent.value) {
        editorRefContent.value.destroy()
      }
      // 销毁参考答案编辑器
      if (editorRefAnswer.value) {
        editorRefAnswer.value.destroy()
      }
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
      editorRefContent ,
      editorRefAnswer ,
      mode,
      editorConfig,
      handleCreated
    }
  },
  created() {
    this.paramData.creator = parseInt(this.user.id)
    this.param.userId =parseInt(this.user.id)
    this.queryClass(this.param)
    this.type = this.$route.query.type
    this.tag = this.$route.query.tag
    this.check = this.$route.query.check
    console.log("check:",this.check)
    console.log("tag:",this.tag)
    console.log("data2:",this.data2)
    if (this.data2) {
      const data2 = JSON.parse(this.$route.query.data2); // 关键解析步骤
      console.log("解析后的 data2:", data2);
      this.paramData.userId = parseInt(data2.userId)
      this.paramData.id = data2.id
      console.log("dat2.id:",this.paramData.id)
      this.paramData.title = data2.title
      console.log("dat2.title:",this.paramData.title)
      this.paramData.content = data2.content
      console.log("dat2.content:",this.paramData.content)
      this.paramData.answer = data2.answer
      console.log("dat2.answer:",this.paramData.answer)
      this.paramData.commitTime = data2.commitTime || data2.completionTime
      console.log("dat2.commitetime:",this.paramData.commitTime)
      this.paramData.classId = data2.classId
      console.log("dat2.classId:",this.paramData.classId)
      this.mode = data2.mode
      console.log("dat2.mode:",this.paramData.mode)
      this.paramData.remark = data2.remark
      console.log("dat2.remark:",this.paramData.remark)
      this.paramData.score = data2.score
      console.log("dat2.score:",this.paramData.score)
    }
  },
  methods: {
    changeDate(val) {
      this.$forceUpdate();
      this.data.value1 = val;
    },
    handleDateChange(value) {
      console.log('原始值:', value, '类型:', typeof value);
      if (!value) {
        console.log('日期已清空');
        this.paramData.commitTime = '';
        return;
      }

      try {
        // 先检查是否是字符串类型
        if (typeof value === 'string') {
          // 尝试解析特殊格式
          const formattedDate = this.fixInvalidDateFormat(value);
          if (!formattedDate) {
            throw new Error('无法解析的日期格式');
          }
          this.paramData.commitTime = formattedDate;
        } else {
          // 如果是 Date 对象，直接格式化
          this.paramData.commitTime = moment(value).format('YYYY-MM-DD HH:mm:ss');
        }
        console.log('修正后的日期:', this.paramData.commitTime);
      } catch (error) {
        console.error('日期解析失败:', error.message);
        this.paramData.commitTime = '';
      }
    },

    // 修复无效日期格式
    fixInvalidDateFormat(dateString) {
      // 正则表达式匹配 yyyy-MM-Th 00:00:00 格式
      const regex = /^(\d{4})-(\d{2})-(\w{2})\s(\d{2}:\d{2}:\d{2})$/;
      const match = dateString.match(regex);

      if (match) {
        const [, year, month, dayAbbr, time] = match;

        // 将星期缩写转换为日期（假设每个月的第一个星期四为04，第二个为11，依此类推）
        // 注意：这只是一个示例，实际逻辑需要根据你的数据来源调整
        const day = this.getDayFromAbbr(dayAbbr, year, month);

        if (day) {
          // 构建有效的日期字符串
          const validDateStr = `${year}-${month}-${day} ${time}`;

          // 使用 moment 验证并格式化
          const m = moment(validDateStr, 'YYYY-MM-DD HH:mm:ss');
          if (m.isValid()) {
            return m.format('YYYY-MM-DD HH:mm:ss');
          }
        }
      }

      // 尝试直接解析其他标准格式
      const m = moment(dateString, ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD']);
      return m.isValid() ? m.format('YYYY-MM-DD HH:mm:ss') : null;
    },

    // 根据星期缩写和年月计算日期
    getDayFromAbbr(dayAbbr, year, month) {
      // 星期缩写映射
      const dayMap = {
        'Mo': 1, 'Tu': 2, 'We': 3, 'Th': 4,
        'Fr': 5, 'Sa': 6, 'Su': 0
      };

      const dayOfWeek = dayMap[dayAbbr];
      if (dayOfWeek === undefined) return null;

      // 获取当月第一天是星期几
      const firstDayOfMonth = moment(`${year}-${month}-01`).day();

      // 计算当月第一个指定星期几的日期
      let day = 1 + ((dayOfWeek - firstDayOfMonth + 7) % 7);

      // 如果计算出的日期不在当月范围内，则返回 null
      const daysInMonth = moment(`${year}-${month}`, 'YYYY-MM').daysInMonth();
      return day <= daysInMonth ? day.toString().padStart(2, '0') : null;
    },
    // 新增辅助方法处理特殊格式
    parseDate(dateString) {
      // 尝试解析 'yyyy-05-Th 00:00:00' 这种格式
      const regex = /^(\d{4})-(\d{2})-(\w{2})\s(\d{2}:\d{2}:\d{2})$/;
      const match = dateString.match(regex);

      if (match) {
        const year = match[1];
        const month = match[2];
        // 将英文缩写转换为数字（这里假设 'Th' 是错误的，应该是日期）
        // 实际项目中需要根据具体需求调整
        const day = this.convertDayToNumber(match[3]);
        const time = match[4];

        if (day) {
          return moment(`${year}-${month}-${day} ${time}`, 'YYYY-MM-DD HH:mm:ss').toDate();
        }
      }

      // 尝试其他可能的格式
      return moment(dateString, ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD']).toDate();
    },

    // 将星期缩写转换为日期（示例）
    convertDayToNumber(dayAbbr) {
      // 注意：这个方法是为了处理示例中的 'Th'，实际项目中需要根据具体数据调整
      const dayMap = {
        'Mo': '01', 'Tu': '02', 'We': '03', 'Th': '04',
        'Fr': '05', 'Sa': '06', 'Su': '07'
      };
      return dayMap[dayAbbr] || null;
    },
    out() {
      this.tag = this.$route.query.tag
      console.log("tag1:",this.tag)
      const routePath = this.tag === 'homework'
          ? '/manager/classmanagement'
          : '/manager/scoremanagment'
      this.$router.push(routePath)
    },
    queryClass(pa) {
      request.post('/study/userClass/findList', pa).then(resp => {
        console.log("classlist:", resp)
        this.queryCla = resp.data || []
      }).catch(error => {
        console.error("获取班级失败:", error)
        this.queryCla = []
      })
    },
    addTest(paramData) {
      const tag = this.$route.query.tag
      const apiUrl = tag === 'homework'
          ? '/study/homework/save'
          : '/study/exercises/save'
      const successRoute = tag === 'homework'
          ? '/manager/studenthomework'
          : '/manager/scoremanagement'
      console.log("url:",apiUrl)
      console.log("time:",paramData)
      request.post(apiUrl, paramData).then(resp => {
        if (resp.code ===  '200') {
          this.$router.push(successRoute)
          this.$message.success('发布成功')
        } else {
          this.$message.error(resp.msg || '发布失败')
        }
      }).catch(error => {
        console.error("请求失败:", error)
        this.$message.error('网络错误')
      })
    }
  }
}
</script>

<style scoped>
/deep/ .el-input__inner {
  margin-top: 10px;
}

/deep/ .el-textarea__inner {
  margin-top: 10px;
}

.last {
  width: 95%;
  height: 200px;
  margin: 20px auto;

}

.ce {
  display: flex;
  justify-content: space-around;


}

.cls {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}
</style>