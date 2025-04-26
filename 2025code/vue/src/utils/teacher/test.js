import {post,get} from "../request";

export function addt(data) {
    return post('api/study/exercises/save',data)

}

export function addKnow(data) {
    return post('api/study/knowledgePoint/save',data)

}
export function saveTest(data) {
    // alert('ppppp')
    return post('/api/study/knowledgePoint/update ', data)
}

export function saveHomework(data) {
    console.log(data);
    return post('/api/study/homework/save', data)
}