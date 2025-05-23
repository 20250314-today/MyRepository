import axios from 'axios'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request=axios.create({
     baseURL:'http://localhost:9999',
     timeout:30000
 })
request.interceptors.request.use(config =>{

    config.headers['Content-Type']='application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem('code_user')||'{}')
     config.headers['token'] = user.token
    return config
}, error =>{
    return Promise.reject(error)
    });
 request.interceptors.response.use(
    response =>{
        let res = response.data;
        if(typeof res == 'string'){
            res=res?JSON.parse(res):res
        }
        if(res.code === '401'){
            ElMessage.error(res.msg)
            router.push('/login')
        }
        else{
            return res;
        }
    },
    error =>{
        if (error.response.status === 404){
            ElMessage.error('未找到请求接口')
        }
        else if ( error.response.status === 500)
        {
            ElMessage.error('系统异常,请查看后端控制台报错')
        }
        else{
            console.error(error.message)
        }
        return Promise.reject(error)

    }
)

export function get(url,params) {
    //params.t = new Date().getTime(); //get方法加一个时间参数,解决ie下可能缓存问题.
    return request({
        url:url,
        method: 'get',
        headers: {
        },
        params
    })
}


//封装post请求
export function post(url, data) {
    //默认配置
    let sendObject={
        url:url,
        method: 'post',
        headers: {
            'Content-Type':'application/json;charset=UTF-8'
        },
        data:data
    };
    sendObject.data=JSON.stringify(data);
    return request(sendObject)
}


//封装put方法 (resfulAPI常用)
export function put(url,data){
    return request({
        url: url,
        method: 'put',
        headers: {
            'Content-Type':'application/json;charset=UTF-8'
        },
        data:JSON.stringify(data)
    })
}
//删除方法(resfulAPI常用)
export function deletes(url,data){
    return request({
        url: url,
        method: 'delete',
        headers: {
            'Content-Type':'application/json;charset=UTF-8'
        },
        data:JSON.stringify(data)
    })
}

//不要忘记export
export default request