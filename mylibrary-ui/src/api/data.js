import request from '@/utils/request'
import {cxt} from '@/utils/constant'



export function getBookList(params) {
  return request({
    url: cxt+'/v2/book/'+params.pageSize+'/'+params.currentPage,
    method: 'get',
    params
  })
}

export function getFileList(params) {
  return request({
    url: cxt+'/file/'+params.pageSize+'/'+params.currentPage,
    method: 'get',
    params
  })
}

export function getFileBaseurl(){
  return request({
    url:cxt+'/file/baseurl',
    method:'get'
  })
}

export function getFileBasepath(){
  return request({
    url:cxt+'/file/basepath',
    method:'get'
  })
}

export function getPdfViewerUrl(){
  return request({
    url:cxt+'/file/pdfviewer',
    method:'get'
  })
}


export function getPublishHouse(){
  return request({
    url:cxt+'/v2/book/publishhouse'
  })
}

export function getClassification(){
  return request({
    url:cxt+'/v2/book/classification'
  })
}

export function getAuthor(){
  return request({
    url:cxt+'/v2/book/author'
  })
}
