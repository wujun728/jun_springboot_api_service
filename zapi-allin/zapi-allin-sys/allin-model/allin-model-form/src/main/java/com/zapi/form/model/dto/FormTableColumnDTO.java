package com.zapi.form.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.enums.FormFieldTypeEnum;
import com.zapi.form.model.entity.Form;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 数据表字段
 * @author zhonglz
 * @date 2025年08月19日16:32:15
 */
@Slf4j
@Data
public class FormTableColumnDTO {

    /***
     * 表单模板ID
     */
    private String formId;
    /***
     * 表名称
     */
    private String tableName;

    /***
     * 表描述
     */
    private String tableComment;


    /****
     * 表字段集合
     */
    private List<FormColumnInfoDTO> columnInfoDTOList;

    /***
     * 表单信息
     */
    private Form formInfo;


    public FormTableColumnDTO(Form formInfo){
        this.formId = formInfo.getId();
        this.tableName = formInfo.getId();
        this.tableComment = formInfo.getFormName();
        this.formInfo = formInfo;
        this.columnInfoDTOList = parseContentToColumnInfoList(formInfo.getFormConfig());
    }

    /**
     * 无参构造函数
     */
    public FormTableColumnDTO(){
        this.columnInfoDTOList = new ArrayList<>();
    }
    

    /**
     * 解析表单模板内容，提取字段信息
     * @param content 表单模板的JSON内容
     * @return 字段信息列表
     */
    private List<FormColumnInfoDTO> parseContentToColumnInfoList(String content) {
        if (content == null || content.trim().isEmpty()) {
            log.warn("表单配置内容为空");
            return new ArrayList<>();
        }
        try {
            /// 将设计内容转换成json
            Map<String, Object> contentMap = new ObjectMapper().readValue(content, new TypeReference<Map<String, Object>>(){});
            @SuppressWarnings("unchecked")/// 去除代码告警
            List<Map<String, Object>> fields = (List<Map<String, Object>>) contentMap.get("components");
            if (fields == null) {
                log.warn("表单配置中没有components字段");
                return new ArrayList<>();
            }
            
            log.info("解析表单字段, 组件数量: {}", fields.size());
            
            // 只要有name字段就处理，label可以使用默认值
            List<FormColumnInfoDTO> result = fields.stream()
                    .filter(field -> {
                        Object name = field.get("name");
                        Object type = field.get("type");
                        boolean valid = name != null && !name.toString().trim().isEmpty();
                        if (!valid) {
                            log.debug("过滤掉无效字段: name={}, type={}", name, type);
                        }
                        return valid;
                    })
                    .map(this::buildColumnInfo)
                    .collect(java.util.stream.Collectors.toList());
            
            log.info("解析表单字段完成, 有效字段数量: {}, 字段列表: {}", 
                    result.size(), 
                    result.stream().map(FormColumnInfoDTO::getColumnName).collect(java.util.stream.Collectors.joining(", ")));
            
            return result;
                    
        } catch (Exception e) {
            log.error("解析表单配置失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    private FormColumnInfoDTO buildColumnInfo(Map<String, Object> field) {
        String fieldType = (String) field.get("type");
        String fieldName = (String) field.get("name");
        // label可能为空，使用name作为默认值
        String label = field.get("label") != null ? (String) field.get("label") : fieldName;
        Boolean required = (Boolean) field.get("required");
        Object defaultValueObj = field.get("defaultValue");
        String defaultValue = null;
        
        String dbType = FormFieldTypeEnum.getDbType(fieldType);
        Integer columnLength = FormFieldTypeEnum.getColumnLength(fieldType);
        
        // TEXT/LONGTEXT/BLOB/JSON 类型在 MySQL 中不能有默认值
        boolean isTextType = "text".equalsIgnoreCase(dbType) || 
                             "longtext".equalsIgnoreCase(dbType) || 
                             "blob".equalsIgnoreCase(dbType) || 
                             "json".equalsIgnoreCase(dbType);
        
        // 处理defaultValue的类型转换（TEXT类型不设置默认值）
        if (defaultValueObj != null && !isTextType) {
            // 对于开关组件，将布尔值或数字转换为字符串数字
            if ("switch".equals(fieldType)) {
                if (defaultValueObj instanceof Boolean) {
                    defaultValue = ((Boolean) defaultValueObj) ? "1" : "0";
                } else if (defaultValueObj instanceof Number) {
                    defaultValue = ((Number) defaultValueObj).intValue() == 0 ? "0" : "1";
                } else {
                    String strVal = String.valueOf(defaultValueObj);
                    defaultValue = "true".equalsIgnoreCase(strVal) || "1".equals(strVal) ? "1" : "0";
                }
            } else {
                defaultValue = String.valueOf(defaultValueObj);
            }
        }
        
        log.debug("构建字段信息: name={}, type={}, dbType={}, length={}, isTextType={}, defaultValue={}", 
                fieldName, fieldType, dbType, columnLength, isTextType, defaultValue);
        
        return FormColumnInfoDTO.builder()
                .columnName(fieldName)
                .columnComment(label)
                .columnType(dbType)
                .formType(fieldType)
                .columnLength(columnLength)
                .columnScale(FormFieldTypeEnum.getColumnScale(fieldType))
                .isNullable(required == null || !required)
                .defaultValue(defaultValue)
                .isPrimaryKey(false)
                .isAutoIncrement(false)
                .build();
    }







}
