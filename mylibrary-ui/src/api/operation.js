import request from '@/utils/request'
import {cxt} from '@/utils/constant'

export function doCacheImage(filepath,number){
  return request({
    url:cxt+'/op/server/pdf/image/cache/'+filepath+'/'+number ,
    method:'get'
  })
}

export function doSaveBook(data){
  return request({
    url:cxt+'/book',
    method:'post',
    data
  })
}

export function doRepublish(id){
  return request({
    url:cxt+"/op/ghost/republish/"+id,
    method:'get'
  })
}

export function doDeletePost(postid){
  return request({
    url:cxt+"/op/ghost/deletePost/"+postid,
    method:'delete'
  })
}
