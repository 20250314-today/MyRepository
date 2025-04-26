
import {post,deletes} from "../request";


export function astudentquery(data) {
    // alert('ppppp')
    return post('/api/study/user/list', data)
}
export function addStudent(data) {
    // alert('ppppp')
    return post('/api/study/user/save', data)
}

export function deleteStudent(data) {
    // alert('ppppp')
    return deletes('/api/study/user/delete', data)
}


export function updateStudent(data) {
    // alert('ppppp')
    return post('/api/study/user/update', data)
}

