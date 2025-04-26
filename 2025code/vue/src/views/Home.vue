<template>
<div style="width:1000px">
  <div class="card">你好!欢迎登录</div>
  <div class="card" style="margin-top:10px;width:70%"
    v-if="data.user.role === 'USER'"
  >
    <div style="font-size:18px;margin-bottom: 10px">系统公告</div>
    <el-timeline style="max-width: 600px"

    >
      <el-timeline-item :timestamp="item.time" placement="top" v-for="item in data.noticeData"
        color="#ba5253"
      >
        <el-card style="min-width: 300px;">
          <h4 >{{item.title}}}</h4>
          <p>{{ item.content }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
  <div v-else
    style="margin-top:10px">
    <div style="display: flex;grid-gap: 10px">
      <div class="card" style="height:400px;width: 50%;min-width: 300px;" id="pie"></div>
      <div class="card" style="height:400px;width: 50%;min-width: 300px;" id="bar"></div>
    </div>
<div class="card" style="height:400px;width: 100%;margin-top:10px;min-width: 300px;" id="line"></div>
  </div>
</div>
</template>
<script setup>
import {reactive,onMounted} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import * as echarts from "echarts";
const data =reactive({
  user:JSON.parse( localStorage.getItem("code_user")|| '{}'),
  noticeData:[],
})
const loadNotice=()=>{
  request.get('/notice/selectAll').then(res=>{
    if(res.code === '200')
    {
      data.noticeData = res.data
      if(data.noticeData.length > 3 ){
        data.noticeData = data.noticeData.slice(0,3)
      }
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
const loadPie=()=>{
  request.get('/echarts/pie').then(res=>{
    if(res.code === '200'){
      let  chartDom = document.getElementById('pie');
      let  myChart = echarts.init(chartDom);
      pieOptions.series[0].data = res.data
      myChart.setOption(pieOptions);
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}
const loadBar=()=>{
  request.get('/echarts/bar').then(res=>{
    if(res.code === '200'){
      let  chartDom = document.getElementById('bar');
      let  myChart = echarts.init(chartDom);
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions);
    }
  })
}
const loadLine=()=>{
  request.get('/echarts/line').then(res=>{
    if(res.code === '200'){
      let  chartDom = document.getElementById('line');
      let  myChart = echarts.init(chartDom);
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions);
    }
  })
}
onMounted (()=>{
  loadPie()
  loadBar()
  loadLine()
})

let pieOptions = {
  title:{
    text:"不同分类下用户发布旅游攻略帖子的数量",
    subtext:'统计维度：用户昵称',
    left:'center'
  },
  tooltip:{
    trigger:'item',
    formatter:'{a} <br/>{b}:{c} ({d}%)'
  },
  legend: {
    orient:'vertical',
    left:'left'
  },
  series: [
    {
      name: '数量占比',
      type: 'pie',
      radius: '50%',
      center: ['50%', '55%'],
      roseType: 'area',
      data: [
        { value: 40, name: '风景名胜' },
        { value: 38, name: '人文景观' },
        { value: 32, name: '历史古迹' }
      ]
    }
  ]
}
let barOptions = {
  title:{
    text:"不同分类下用户发布帖子数量Top5",
    subtext:'统计维度：用户昵称',
    left:'center'
  },
  grid:{
    bottom:'15%'
  },
  legend: {
    orient:'vertical',
    left:'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    name:'用户昵称',
    axisLabel:{
      show:true,
      interval:0,
      rotate:-60,
      inside:false,
      margin:6
    }
  },
  yAxis: {
    type: 'value',
    name:'攻略数量'
  },
  tooltip:{
    trigger:'item',
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130],
      type: 'bar',
      itemStyle:{
        normal:{
          color:function(){
            return "#"+Math.floor(Math.random()*(256*256*256 - 1)).toString(16);
          }
        },
      },
    }
  ]
};
let lineOptions = {
  title:{
    text:'不同类型旅游攻略发布量折线图',
    subtext:'统计维度：最近一周',
    left:'center'

  },
  legend:{
    data:[],
    template:""
  },
  grind:{
    left:'3%',
    right:'4%',
    bottom:'3%',
    containLabel:true
  },
  tooltip:{
    trigger:'item'
  },
  xAxis: {
    name:'日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    name:'发布攻略数量',
    type: 'value'
  },
  series: [
    {
      name:'攻略数量',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true,
      markLine:{
        data:[{type:'average',name:'最近一周内发布数量平均值'}]
      },
      markPoint:{
        data:[
          {type:'max',name:'最大值'},
          {type:'min',name:'最小值'},

        ]
      }
    }
  ]
};
loadNotice()
</script>
