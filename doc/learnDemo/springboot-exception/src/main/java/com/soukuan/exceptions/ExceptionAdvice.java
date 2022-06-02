package com.soukuan.exceptions;

import com.soukuan.web.ApiResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiebiao
 * @date  2018-07-11  10:23:24
 * @version v1.0
 * Description 控制器异常通知类
 */
@ControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice {

    /**
     * logback new instance
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 处理业务逻辑异常
     *
     * @param e 业务逻辑异常对象实例
     * @return 逻辑异常消息内容
     */
    @ExceptionHandler(LogicException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public ApiResponseEntity<String> logicException(LogicException e) {
        logger.error("遇到业务逻辑异常：【{}】", e.getErrCode());
        // 返回响应实体内容
        return ApiResponseEntity.<String>builder().errorMsg(e.getErrMsg()).build();
    }
}
