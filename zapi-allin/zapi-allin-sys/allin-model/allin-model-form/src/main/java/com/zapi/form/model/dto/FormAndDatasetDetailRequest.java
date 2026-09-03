package com.zapi.form.model.dto;

import lombok.Data;

@Data
public class FormAndDatasetDetailRequest {
    private String json;
    private Long datasetId;
    private Long formId;
} 