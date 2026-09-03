package com.zapi.form.support.strategy.service.impl;

import com.zapi.form.model.dto.FormColumnInfoDTO;
import com.zapi.form.model.dto.FormTableColumnDTO;
import com.zapi.form.model.entity.FormTableInfo;
import com.zapi.form.service.DataBaseService;
import com.zapi.form.service.FormColumnInfoService;
import com.zapi.form.service.FormTableInfoService;
import com.zapi.form.support.request.FormOperationRequest;
import com.zapi.form.support.strategy.service.FormOperateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/****
 * 修改业务数据表
 */
@Slf4j
@Service
public class UpdateTableOperateServicelmpl implements FormOperateService {

    @Autowired
    private DataBaseService service;

    @Autowired
    private FormTableInfoService tableInfoService;

    @Autowired
    private FormColumnInfoService columnInfoService;

    @Override
    public void executeDataBaseOperate(FormOperationRequest dataBaseRequest) {
        log.info("==========修改业务数据表 开始===========");
        FormTableColumnDTO tableColumnDTO = dataBaseRequest.getTableColumnDTO();
        String tableName = tableColumnDTO.getTableName();
        String tableComment = tableColumnDTO.getTableComment();
        
        ///  业务上表单编号可能会更改，所以只需要根据表单编号查找到对应的表即可
        if (!ObjectUtils.isEmpty(dataBaseRequest.getTableColumnDTO().getFormId())){
            FormTableInfo tableInfo = tableInfoService.getByFormId(dataBaseRequest.getTableColumnDTO().getFormId());
            if (!ObjectUtils.isEmpty(tableInfo)){
                tableName = tableInfo.getTableName();
                tableComment = tableInfo.getTableComment();
            } else {
                // 如果不存在FormTableInfo记录，需要新增一条数据，将物理表与表单ID关联起来
                log.info("FormTableInfo不存在，创建表单与物理表的关联记录");
                FormTableInfo info = new FormTableInfo();
                info.setFormId(dataBaseRequest.getTableColumnDTO().getFormId());
                info.setTableName(dataBaseRequest.getTableColumnDTO().getTableName());
                info.setTableComment(dataBaseRequest.getTableColumnDTO().getTableComment());
                tableInfoService.save(info);
            }
        }
        
        List<FormColumnInfoDTO> columns = tableColumnDTO.getColumnInfoDTOList();
        // 检查物理表是否存在
        String currentDatabase = service.getCurrentDatabase();
        Integer tableExists = service.checkTableExists(currentDatabase, tableName);
        if (tableExists == null || tableExists == 0) {
            // 表不存在，先创建表
            log.info("表 {} 不存在，先创建表", tableName);
            service.createMainTable(dataBaseRequest.getTableColumnDTO().getFormId(),tableName, tableComment);
        }
        // 处理字段变更
        if (columns != null && !columns.isEmpty()) {
            // 先将业务表字段中所有非系统字段都删除，然后重新添加字段信息
            columnInfoService.addColumnsToDeleteAllWithDTO(dataBaseRequest.getTableColumnDTO().getFormId(), columns);
            // 如果字段不存在则新增，如果存在则忽略
            service.addColumnsIfNotExistsWithDTO(tableName, columns);
            log.info("成功处理 {} 个字段", columns.size());
        }
        
        log.info("==========修改业务数据表 结束===========");
    }
}
