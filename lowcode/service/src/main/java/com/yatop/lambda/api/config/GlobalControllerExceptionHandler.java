package com.yatop.lambda.api.config;


import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.jsonrpc.JsonRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerExceptionHandler extends GlobalExceptionHandler {

    public static Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(value = EngineException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handleRequestFailedLambdaException(HttpServletRequest req, EngineException lambdaException) throws Exception {
        logger.info("MLS业务请求失败：{}", lambdaException);
        return new JsonRpcResponse(EngineErrorEnum.ModelException,lambdaException);
    }

}
