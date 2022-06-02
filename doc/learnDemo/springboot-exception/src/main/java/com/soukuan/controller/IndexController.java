package com.soukuan.controller;

import com.soukuan.enums.ErrorCodeEnum;
import com.soukuan.exceptions.LogicException;
import com.soukuan.web.ApiResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiebiao
 * @date  2018-07-11  10:13:06
 * @version v1.0
 * Description
 */
@RestController
public class IndexController {

    /**
     * 首页方法
     * @return xx
     * @throws LogicException 系统通用异常
     */
    @GetMapping(value = "/index")
    public ApiResponseEntity<String> index() throws LogicException {
        /**
         * 模拟用户不存在
         * 抛出业务逻辑异常
         */
        if (true) {
            throw new LogicException(ErrorCodeEnum.USER_STATUS_FAILD);
        }
        return ApiResponseEntity.<String>builder().data("this is index mapping").build();
    }
}
