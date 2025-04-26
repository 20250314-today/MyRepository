import {post,get} from "../request";


export function konwpoint(data) {
    // alert('ppppp')
    return post('/api/study/knowledgePoint/list', data)
}
export function deletekonwpoint(data) {
    // alert('ppppp')
    return post('/api/study/knowledgePoint/delete', data)
}

export function stuPointList(data) {
    // alert('ppppp')
    return get('/api/study/knowledgePoint/stuPointList', data)
}
