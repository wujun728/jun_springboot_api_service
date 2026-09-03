package com.zapi.form.service.impl;

import com.zapi.constants.DataBaseConstans;
import com.zapi.enums.FormFieldTypeEnum;
import com.zapi.form.mapper.DataBaseMapper;
import com.zapi.form.model.dto.FormColumnInfoDTO;
import com.zapi.form.model.entity.FormColumnInfo;
import com.zapi.form.service.DataBaseService;
import com.zapi.form.service.FormColumnInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    private DataBaseMapper dataBaseMapper;

    @Autowired
    private FormColumnInfoService columnInfoService;

    @Override
    public void createMainTable(String formId,String tableName, String tableComment) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        dataBaseMapper.createMainTable(tableName, tableComment);
        addSystemColumns(formId);

    }

    /**
     * 添加默认系统字段
     * @param formId 表单ID
     */
    @Override
    public void addSystemColumns(String formId) {
        List<FormColumnInfo> systemColumns = createSystemColumns(formId);
        columnInfoService.batchSave(systemColumns);
        log.info("成功添加 {} 个系统字段到表单ID: {}", systemColumns.size(),formId);
    }

    /**
     * 创建默认系统字段列表
     * @param formId 表单ID
     * @return 系统字段列表
     */
    private List<FormColumnInfo> createSystemColumns(String formId) {
        List<FormColumnInfo> columns = new ArrayList<>();
        // 主键ID
        FormColumnInfo id = new FormColumnInfo();
        id.setFormId(formId);
        id.setColumnName("id");
        id.setColumnType("bigint");
        id.setFormType("number");
        id.setIsSysField(1);
        id.setColumnComment("主键id");
        id.setIsNullable(false);
        id.setDefaultValue(null);
        id.setIsPrimaryKey(true);
        id.setIsAutoIncrement(true);
        columns.add(id);

        // 创建时间
        FormColumnInfo createTime = new FormColumnInfo();
        createTime.setFormId(formId);
        createTime.setColumnName("create_time");
        createTime.setColumnType(FormFieldTypeEnum.DATETIME.getDbType());
        createTime.setFormType(FormFieldTypeEnum.DATETIME.getFormType());
        createTime.setIsSysField(1);
        createTime.setColumnComment("创建时间");
        createTime.setIsNullable(true);
        createTime.setDefaultValue(null);
        createTime.setIsPrimaryKey(false);
        createTime.setIsAutoIncrement(false);
        columns.add(createTime);

        // 创建人
        FormColumnInfo createUser = new FormColumnInfo();
        createUser.setFormId(formId);
        createUser.setColumnName("create_user");
        createUser.setColumnType(FormFieldTypeEnum.INPUT.getDbType());
        createUser.setFormType(FormFieldTypeEnum.INPUT.getFormType());
        createUser.setIsSysField(1);
        createUser.setColumnComment("创建人");
        createUser.setColumnLength(64);
        createUser.setIsNullable(true);
        createUser.setDefaultValue(null);
        createUser.setIsPrimaryKey(false);
        createUser.setIsAutoIncrement(false);
        columns.add(createUser);

        // 修改时间
        FormColumnInfo updateTime = new FormColumnInfo();
        updateTime.setFormId(formId);
        updateTime.setColumnName("update_time");
        updateTime.setColumnType(FormFieldTypeEnum.DATETIME.getDbType());
        updateTime.setFormType(FormFieldTypeEnum.DATETIME.getFormType());
        updateTime.setIsSysField(1);
        updateTime.setColumnComment("修改时间");
        updateTime.setIsNullable(true);
        updateTime.setDefaultValue(null);
        updateTime.setIsPrimaryKey(false);
        updateTime.setIsAutoIncrement(false);
        columns.add(updateTime);

        // 修改人
        FormColumnInfo updateUser = new FormColumnInfo();
        updateUser.setFormId(formId);
        updateUser.setColumnName("update_user");
        updateUser.setColumnType(FormFieldTypeEnum.INPUT.getDbType());
        updateUser.setFormType(FormFieldTypeEnum.INPUT.getFormType());
        updateUser.setIsSysField(1);
        updateUser.setColumnComment("修改人");
        updateUser.setColumnLength(64);
        updateUser.setIsNullable(true);
        updateUser.setDefaultValue(null);
        updateUser.setIsPrimaryKey(false);
        updateUser.setIsAutoIncrement(false);
        columns.add(updateUser);

        // 删除标识
        FormColumnInfo isDeleted = new FormColumnInfo();
        isDeleted.setFormId(formId);
        isDeleted.setColumnName("is_deleted");
        isDeleted.setColumnType(FormFieldTypeEnum.NUMBER.getDbType());
        isDeleted.setFormType(FormFieldTypeEnum.NUMBER.getFormType());
        isDeleted.setIsSysField(1);
        isDeleted.setColumnComment("删除标识");
        isDeleted.setIsNullable(true);
        isDeleted.setDefaultValue("0");
        isDeleted.setIsPrimaryKey(false);
        isDeleted.setIsAutoIncrement(false);
        columns.add(isDeleted);

        return columns;
    }

    @Override
    public void alterTableName(String tableName, String newTableName) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        newTableName = DataBaseConstans.BUSINESS_PREFIX + newTableName;
        dataBaseMapper.alterTableName(tableName, newTableName);
    }

    @Override
    public void dropTable(String tableName) {
        /// todo 物理表还是保持逻辑删除即可，非必要不做物理删除
       // dataBaseMapper.dropTable(tableName);
    }

    @Override
    public void executeDDL(String ddlSql) {
        dataBaseMapper.executeDDL(ddlSql);
    }

    @Override
    public void batchExecuteDDL(List<String> ddlSqlList) {
        dataBaseMapper.batchExecuteDDL(ddlSqlList);
    }

    @Override
    public void batchAddColumnsWithDTO(String tableName, List<FormColumnInfoDTO> columns) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        List<Map<String, Object>> columnMaps = columns.stream()
                .map(FormColumnInfoDTO::toMap)
                .collect(Collectors.toList());
        dataBaseMapper.batchAddColumns(tableName, columnMaps);
    }

    @Override
    public void batchModifyColumnsWithDTO(String tableName, List<FormColumnInfoDTO> columns) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        List<Map<String, Object>> columnMaps = columns.stream()
                .map(FormColumnInfoDTO::toMap)
                .collect(Collectors.toList());
        dataBaseMapper.batchModifyColumns(tableName, columnMaps);
    }

    @Override
    public void addColumnsIfNotExistsWithDTO(String tableName, List<FormColumnInfoDTO> columns) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        for (FormColumnInfoDTO column : columns) {
            // 检查字段是否存在
            Integer exists = dataBaseMapper.checkColumnNameIsExisted(
                dataBaseMapper.selectCurrentDatabase(), 
                tableName, 
                column.getColumnName()
            );
            // 如果字段不存在，则新增
            if (exists == null || exists == 0) {
                dataBaseMapper.addColumn(
                    tableName,
                    column.getColumnName(),
                    column.getColumnType(),
                    column.getColumnLength(),
                    column.getIsNullable(),
                    column.getDefaultValue(),
                    column.getColumnComment()
                );
            }
        }
    }

    @Override
    public void addColumnWithDTO(String tableName, FormColumnInfoDTO column) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        dataBaseMapper.addColumn(
            tableName,
            column.getColumnName(),
            column.getColumnType(),
            column.getColumnLength(),
            column.getIsNullable(),
            column.getDefaultValue(),
            column.getColumnComment()
        );
    }

    @Override
    public void modifyColumnWithDTO(String tableName, FormColumnInfoDTO column) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        dataBaseMapper.modifyColumn(
            tableName,
            column.getColumnName(),
            column.getColumnType(),
            column.getColumnLength(),
            column.getIsNullable(),
            column.getDefaultValue(),
            column.getColumnComment()
        );
    }

    @Override
    public String getCurrentDatabase() {
        return dataBaseMapper.selectCurrentDatabase();
    }

    @Override
    public Integer checkTableExists(String databaseName, String tableName) {
        /// 所有业务表需要添加前缀
        tableName = DataBaseConstans.BUSINESS_PREFIX + tableName;
        return dataBaseMapper.checkTableNameIsExisted(databaseName, tableName);
    }
}
