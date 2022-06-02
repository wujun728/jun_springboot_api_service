package com.soukuan.exceptions;


import com.soukuan.enums.BaseEnum;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * @author xiebiao
 * @date  2018-07-11  10:10:47
 * @version v1.0
 * Description
 */
@Getter
public class LogicException extends RuntimeException {

    /**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(LogicException.class);

    /**
     * 错误消息内容
     */
    private String errMsg;
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 格式化错误码时所需参数列表
     */
    private String[] params;

    /**
     * 构造函数设置错误码以及错误参数列表
     * @param baseEnum 错误枚举
     * @param params 参数
     * @param <T>
     */
    public <T extends BaseEnum> LogicException(T baseEnum, String... params) {
        this.errCode = baseEnum.getCode();
        this.params = params;
        //获取格式化后的异常消息内容
        String desc = baseEnum.getDesc();
        this.errMsg = ObjectUtils.isEmpty(params) ? desc : String.format(desc, params);
        //错误信息
        logger.error("系统遇到如下异常，异常码：{}>>>异常信息：{}", errCode, errMsg);
    }

}
