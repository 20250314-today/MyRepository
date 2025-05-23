import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Admin.vue'
//import VueRouter from 'vue-router';

//Vue.use(VueRouter);
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/',redirect:'/manager/home'},
    { path: '/manager',component:()=>import('../views/Manager.vue'),
        children:[
        {path: 'home',meta:{name:'主页'} ,component:()=>import('../views/Home.vue'),},
        {path: 'admin', meta:{name:'管理员信息'} ,component:()=>import('../views/Admin.vue'),},
        {path: 'user', meta:{name:'用户信息'} ,component:()=>import('../views/User.vue'),},
        {path: 'teacher', meta:{name:'用户信息'} ,component:()=>import('../views/Teacher.vue'),},
        {path: 'updatePassword', meta:{name:'更新密码'} ,component:()=>import('../views/UpdatePassword.vue'),},
        {path: 'person', meta:{name:'个人信息'} ,component:()=>import('../views/Person.vue'),},
        {path: 'notice', meta:{name:'系统公告'} ,component:()=>import('../views/Notice.vue'),},
        {path: 'introduction', meta:{name:'学习笔记'} ,component:()=>import('../views/Introduction.vue'),},
        {path: 'category', meta:{name:'科目分类'} ,component:()=>import('../views/Category.vue'),},
        {path: 'apply', meta:{name:'审核'} ,component:()=>import('../views/Apply.vue'),},
        {path: 'book', meta:{name:'书籍信息'} ,component:()=>import('../views/Book.vue'),},
        {path: 'study', meta:{name:'在线学习'} ,component:()=>import('../views/TeacherOp/OnlineStudy.vue'),},
        {path: 'play', meta:{name: '视频播放'}, component: () => import('../views/TeacherOp/VideoPlay.vue'),},
        {path: 'upload', meta:{name: '上传视频'}, component: () => import('../views/TeacherOp/VideoUpload.vue'),},
        {path: 'playall', meta:{name: '播放视频'}, component: () => import('../views/TeacherOp/VideoPlayAll.vue'),},
        {path: 'playmerge', meta:{name: 'VideoPlayMerge'}, component: () => import('../views/TeacherOp/VideoPlayMerge.vue'),},
        {path: 'uploadsplit', meta:{name: 'VideoUploadSplit'}, component: () => import('../views/TeacherOp/VideoUploadSplit.vue'),},
        {path: 'classadmin', meta:{name: '管理员班级'}, component: () => import('../views/AdminOp/ClassAmin.vue'),},
        {path: 'teachervideo', meta:{name: '视频管理'}, component: () => import('../views/TeacherOp/DetailOnlineWeb.vue'),},
        {path: 'teacherupvideo', meta:{name: '视频管理'}, component: () => import('../views/TeacherOp/Video.vue'),},
        {path: 'scoremanagement', meta:{name: '练习管理'}, component: () => import('../views/TeacherOp/ScoreManagerment.vue'),},
        {path: 'class', meta:{name: '班级管理'}, component: () => import('../views/AdminOp/ClassAmin.vue'),},
        {path: 'teacherapplicant', meta:{name: '班级审核'}, component: () => import('../views/TeacherOp/TeacherApplicant.vue'),},
        {path: 'studenthomework', meta:{name: '作业管理'}, component: () => import('../views/ClassManagement/StudentHomework.vue'),},

        {path: 'questionmanagement', meta:{name: '问答社区'}, component: () => import('../views/CourseManagement/questionmanagement.vue'),},
        // {path: 'aichat', meta:{name: 'AI聊天'}, component: () => import('../views/ChatMain.vue'),},
        {path: 'deepseekchat', meta:{name: 'DeepSeek聊天'}, component: () => import('../views/ChatMainWindow.vue'),},
        {path: 'examine', meta:{name: '试卷管理'}, component: () => import('../views/TeacherOp/ExamPublish.vue'),},
        {path: 'chat', meta:{name: '聊天'}, component: () => import('../views/xfChat.vue'),},
        // {path: 'show', meta:{name: 'show'}, component: () => import('../views/TeacherOp/show.vue'),}
      //     子组件一起访问，嵌套组件
      ]
    },
      {
          path: '/studentweb',
          component:()=>import('../views/StudentHome.vue'),
          children:[
              {path: 'playall', meta:{name: '播放视频'}, component: () => import('../views/TeacherOp/VideoPlayAll.vue'),},
              {path: 'checkhomework', component:()=>import('../views/StudentOp/Homework/CheckHomework.vue'),},
              {path: 'nohomework', component:()=>import('../views/StudentOp/Homework/NoHomework.vue'),},
              {path: 'askandanswer', component:()=>import('../views/StudentOp/Homework/AskAndAnswer.vue'),},
              {path: 'studentpractice', component:()=>import('../views/StudentOp/Practice/StudentPratice.vue'),},
              // {path: '/studenthome/studentmanagement', component:()=>import('../views/StudentOp/Homework/CheckHomework.vue'),},
              {path: 'practicedetail/:data2', name: "pDetail", component:()=>import('../views/StudentOp/Practice/PraDetail.vue'),
                  // props: (route) => ({
                  //     // 明确映射 query 参数到组件的 props
                  //     data2: route.params.data2
                  // })

              },
              {path: 'apply', meta:{name:'审核'} ,component:()=>import('../views/Apply.vue'),},
              {path: 'home', component:()=>import('../views/Front.vue'),},
              {path: 'studentaddclass', component:()=>import('../views/StudentOp/StudentAddClass.vue'),},
              {path: 'onlinelearn', component:()=>import('../views/TeacherOp/OnlineLearn.vue'),},
              {path: 'markdownnothomework', component:()=>import('../views/StudentOp/Homework/MarkDownNotHomework.vue'),},
              {path: 'examine', component:()=>import('../views/StudentOp/StudentDoExam.vue'),},
              {path: 'homeworkdetail', component:()=>import('../views/StudentOp/Practice/HomeworkDetails.vue'),},
              {path: 'markdown/:homework?/:type?', name:'MarkDown',component:()=>import('../views/StudentOp/MarkDown.vue'),},
          ]
      },
      {path: '/front/introductionDetail', component:()=>import('../views/introductionDetail.vue'),},
      {path: '/markdownnothomework', name:'MarkDownNotHomeWork', component:()=>import('../views/StudentOp/Homework/MarkDownNotHomework.vue'),
          props: (route) => ({
              id: route.query.id?.toString() || '', // 确保字符串类型
              content: route.query.content
                  ? decodeURIComponent(route.query.content)
                  : '',
              data2: route.query.data2 || '',
              data1:route.query.data1||''
          }),
          beforeEnter: (to, from, next) => {
              if (!to.query.id || !to.query.content) {
                  next('/error?code=4001') // 自定义错误码
                  return
              }
              next()
          }
      },

       {path: '/detailonlineweb', component:()=>import('../views/TeacherOp/DetailOnlineWeb.vue'), },
      {path: '/pradetailmark', component:()=>import('../views/TeacherOp/PracticeDetailMark.vue'), name:'DetailMark' },
      {path: '/detailmark', component:()=>import('../views/TeacherOp/DetailOnlineWeb.vue'),},
      {path: '/scoredetail',name:'Detail',  component:()=>import('../views/TeacherOp/ScoreDetail.vue'),
          props: (route) => ({
              // 明确映射 query 参数到组件的 props
              type: route.query.type,
              tag: route.query.tag,
              data2: route.query.data2
          })},
      {path: '/notFound', component:()=>import('../views/404.vue'),},
      {path: '/register', component:()=>import('../views/Register.vue'),},
      {path: '/login', component:()=>import('../views/Login.vue'),},
      {path: '/:pageMatch(.*)', redirect:'/notFound',},//配置404页面
  ],
})

export default router

