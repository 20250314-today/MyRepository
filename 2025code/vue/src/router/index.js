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
        {path: 'updatePassword', meta:{name:'更新密码'} ,component:()=>import('../views/UpdatePassword.vue'),},
        {path: 'person', meta:{name:'个人信息'} ,component:()=>import('../views/Person.vue'),},
        {path: 'notice', meta:{name:'系统公告'} ,component:()=>import('../views/Notice.vue'),},
        {path: 'introduction', meta:{name:'旅游攻略'} ,component:()=>import('../views/Introduction.vue'),},
        {path: 'category', meta:{name:'旅游分类'} ,component:()=>import('../views/Category.vue'),},
        {path: 'apply', meta:{name:'审核'} ,component:()=>import('../views/Apply.vue'),},
        {path: 'book', meta:{name:'书籍信息'} ,component:()=>import('../views/Book.vue'),},
        {path: 'study', meta:{name:'在线学习'} ,component:()=>import('../views/TeacherOp/OnlineStudy.vue'),},
        {path: 'play', meta:{name: 'VideoPlay'}, component: () => import('../views/TeacherOp/VideoPlay.vue'),},
        {path: 'upload', meta:{name: 'VideoUpload'}, component: () => import('../views/TeacherOp/VideoUpload.vue'),},
        {path: 'playall', meta:{name: 'VideoPlayAll'}, component: () => import('../views/TeacherOp/VideoPlayAll.vue'),},
        {path: 'playmerge', meta:{name: 'VideoPlayMerge'}, component: () => import('../views/TeacherOp/VideoPlayMerge.vue'),},
        {path: 'uploadsplit', meta:{name: 'VideoUploadSplit'}, component: () => import('../views/TeacherOp/VideoUploadSplit.vue'),},
        // {path: 'show', meta:{name: 'show'}, component: () => import('../views/TeacherOp/show.vue'),}
      //     子组件一起访问，嵌套组件
      ]
    },
      {
          path: '/studentweb',
          component:()=>import('../views/StudentHome.vue'),
          children:[
              {path: 'playall', meta:{name: 'VideoPlayAll'}, component: () => import('../views/TeacherOp/VideoPlayAll.vue'),},
              {path: 'checkhomework', component:()=>import('../views/StudentOp/Homework/CheckHomework.vue'),},
              {path: 'nohomework', component:()=>import('../views/StudentOp/Homework/NoHomework.vue'),},
              {path: 'askandanswer', component:()=>import('../views/StudentOp/Homework/AskAndAnswer.vue'),},
              {path: 'studentpractice', component:()=>import('../views/StudentOp/Practice/StudentPratice.vue'),},
              // {path: '/studenthome/studentmanagement', component:()=>import('../views/StudentOp/Homework/CheckHomework.vue'),},
              {path: 'practicedetail', component:()=>import('../views/StudentOp/Practice/PraDetail.vue'),},
              {path: 'apply', meta:{name:'审核'} ,component:()=>import('../views/Apply.vue'),},
              {path: 'home', component:()=>import('../views/Front.vue'),},
          ]
      },
      {path: '/front/introductionDetail', component:()=>import('../views/introductionDetail.vue'),},
      {path: '/notFound', component:()=>import('../views/404.vue'),},
      {path: '/register', component:()=>import('../views/Register.vue'),},
      {path: '/login', component:()=>import('../views/Login.vue'),},
      {path: '/:pageMatch(.*)', redirect:'/notFound',},//配置404页面
  ],
})

export default router
