import { ref } from 'vue'
import { dataSourceApi } from '@/api/index.js'

export default function useDataSource() {
  const dataSourceList = ref([])
  const loading = ref(false)
  const total = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)

  // 获取数据源列表
  const fetchData = async () => {
    loading.value = true
    try {
      const response = await dataSourceApi.getAllDataSources()
      if (response.code === 200) {
        dataSourceList.value = response.data
        total.value = Number(response.data.length) || 0
      } else {
        dataSourceList.value = []
        total.value = 0
      }
    } catch (error) {

      dataSourceList.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  // 创建数据源
  const createDataSource = async (data) => {
    try {
      const response = await dataSourceApi.createDataSource(data)
      if (response.code === 200) {
        return Promise.resolve(response.data)
      } else {
        return Promise.reject(new Error(response.message || '创建失败'))
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 更新数据源
  const updateDataSource = async (data) => {
    try {
      const response = await dataSourceApi.updateDataSource(data)
      if (response.code === 200) {
        return Promise.resolve(response.data)
      } else {
        return Promise.reject(new Error(response.message || '更新失败'))
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 删除数据源
  const deleteDataSource = async (id) => {
    try {
      const response = await dataSourceApi.deleteDataSource(id)
      if (response.code === 200) {
        return Promise.resolve(response.data)
      } else {
        return Promise.reject(new Error(response.message || '删除失败'))
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }

  return {
    dataSourceList,
    loading,
    total,
    currentPage,
    pageSize,
    fetchData,
    createDataSource,
    updateDataSource,
    deleteDataSource
  }
} 