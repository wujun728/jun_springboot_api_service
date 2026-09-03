/**
 * API服务统一导出
 * 统一导出所有API模块
 */

// 导入所有API模块
export { userApi, userRoleApi, userOrgApi } from './user'
export { roleApi } from './role'
export { departmentApi } from './department'
export { sysLogApi } from './sysLog'
export { menuApi } from './menu'
export { fileApi } from './file'
export { messageApi, messageTemplateApi } from './notification'
export { datasetApi, dataTableApi, dataSourceApi, dataSourceExportApi } from './dataset'
export { workflowApi, workflowInstanceApi, workflowTemplateApi } from './workflow'
export { flowTestOrderApi } from './flowTestOrder'
export { onlyofficeApi } from './onlyoffice'
export { formApi } from './form'
export { statisticsApi } from './statistics'
export { sealApi } from './seal'
export { certApi } from './cert'
export { contractApi } from './contract'
export { dictTypeApi, dictDataApi } from './dict'
export { wbApplicationApi } from './wbApplication'
export { wbAppEntryApi } from './wbAppEntry'
export { apiConfigApi, apiLogApi } from './openapiConfig'
export { sysConfigApi } from './sysConfig'

// 默认导出axios实例
export { default as api } from './request'

// 也可以按需导入具体模块
export { default as userApiModule } from './user'
export { default as roleApiModule } from './role'
export { default as departmentApiModule } from './department'
export { default as sysLogApiModule } from './sysLog'
export { default as menuApiModule } from './menu'
export { default as fileApiModule } from './file'
export { default as datasetApiModule } from './dataset'
export { default as workflowApiModule } from './workflow'
export { default as onlyofficeApiModule } from './onlyoffice'
export { default as formApiModule } from './form'
export { default as statisticsApiModule } from './statistics'
export { default as sealApiModule } from './seal'
export { default as certApiModule } from './cert'
export { default as contractApiModule } from './contract'
export { default as dictApiModule } from './dict'
export { default as wbApplicationModule } from './wbApplication'
export { default as wbAppEntryModule } from './wbAppEntry'
export { default as sysConfigApiModule } from './sysConfig'
