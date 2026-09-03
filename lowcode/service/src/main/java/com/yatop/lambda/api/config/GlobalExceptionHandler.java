package com.yatop.lambda.api.config;

import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonRpcResponse handleException(Exception e) {
        log.error("系统内部错误：", e);
        return new JsonRpcResponse(EngineErrorEnum.ModelException, e);
    }


}