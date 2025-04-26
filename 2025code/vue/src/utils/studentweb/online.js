import {post} from "../request";

export function onlineweb(data) {
    return post('api/study/videoTotal/pageVideo',data)

}
export function onlinecourse(data) {
    return post('api/study/subject/queryList',data)

}