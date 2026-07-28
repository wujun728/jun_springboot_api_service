package com.ruoyi.api.core.spi;

import com.ruoyi.api.core.model.ApiRequest;
import com.ruoyi.api.core.model.ApiResponse;

public interface TransformProvider {

    String getName();

    Object transform(Object data, ApiRequest request, ApiResponse response);

}