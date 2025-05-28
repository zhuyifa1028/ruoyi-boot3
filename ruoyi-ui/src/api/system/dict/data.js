import request from '@/utils/request'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: '/system/dict/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(dictId) {
  return request({
    url: '/system/dict/' + dictId,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: '/system/dict/type/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: '/system/dict',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: '/system/dict',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delData(dictId) {
  return request({
    url: '/system/dict/' + dictId,
    method: 'delete'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/system/dict/refreshCache',
    method: 'delete'
  })
}
