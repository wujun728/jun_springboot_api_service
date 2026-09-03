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

import java.util.List;

/***
 * 新增业务数据表
 */
@Slf4j
@Service
public class AddTableOperateServiceImpl implements FormOperateService {

    @Autowired
    private DataBaseService service;

    @Autowired
    private FormTableInfoService tableInfoService;

    @Autowired
    private FormColumnInfoService columnInfoService;


    @Override
    public void executeDataBaseOperate(FormOperationRequest formOperationRequest) {

        log.info("==========新增业务数据表 开始===========");
        /// 需要先将表单以及建数据表的信息记录下来
        FormTableColumnDTO tableColumnDTO = formOperationRequest.getTableColumnDTO();
        FormTableInfo info = new FormTableInfo();
        info.setFormId(formOperationRequest.getTableColumnDTO().getFormId());
        info.setTableName(formOperationRequest.getTableColumnDTO().getTableName());
        info.setTableComment(formOperationRequest.getTableColumnDTO().getTableComment());
        tableInfoService.save(info);
        /// 创建物理表
        service.createMainTable(formOperationRequest.getTableColumnDTO().getFormId(),formOperationRequest.getTableColumnDTO().getTableName(), formOperationRequest.getTableColumnDTO().getTableComment());

        List<FormColumnInfoDTO> columns = tableColumnDTO.getColumnInfoDTOList();
        // 处理字段变更
        if (columns != null && !columns.isEmpty()) {
            // 先将业务表字段中所有非系统字段都删除，然后重新添加字段信息
            columnInfoService.addColumnsToDeleteAllWithDTO(formOperationRequest.getTableColumnDTO().getFormId(), columns);
            // 如果字段不存在则新增，如果存在则忽略
            service.addColumnsIfNotExistsWithDTO(formOperationRequest.getTableColumnDTO().getTableName(), columns);
        }
        log.info("==========新增业务数据表 结束===========");

    }
    

}
