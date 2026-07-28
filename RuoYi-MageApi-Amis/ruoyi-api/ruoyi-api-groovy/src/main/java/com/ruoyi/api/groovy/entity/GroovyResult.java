package com.ruoyi.api.groovy.entity;


import java.util.HashMap;

/**
 * 返回对象
 *
 * @author wujun
 */
public class GroovyResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** amis兼容状态码（status=0表示成功，与code同步） */
    public static final String STATUS_TAG = "status";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    private Boolean success = true;

    public Boolean getSuccess() {
        return success;
    }

    public GroovyResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }



    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public GroovyResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public GroovyResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(STATUS_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public GroovyResult(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(STATUS_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(DATA_TAG, data);
    }

    public static GroovyResult getResult(int i, String s) {
        return GroovyResult.error(i,s);
    }

    /**
     * 方便链式调用
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public GroovyResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static GroovyResult success()
    {
        return GroovyResult.success("success");
    }
    public static GroovyResult ok()
    {
        return GroovyResult.success("success");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static GroovyResult ok(Object data)
    {
        return GroovyResult.success("success", data);
    }
    public static GroovyResult success(Object data)
    {
        return GroovyResult.success("success", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static GroovyResult success(String msg, Object data)
    {
        return new GroovyResult(0, msg, data);
    }
    public static GroovyResult success(int code, String msg, Object data)
    {
        return new GroovyResult(code, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return
     */
    public static GroovyResult fail()
    {
        return GroovyResult.error("fail").setSuccess(false);
    }
    public static GroovyResult error()
    {
        return GroovyResult.error("fail").setSuccess(false);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static GroovyResult fail(String msg)
    {
        return GroovyResult.error(msg, null).setSuccess(false);
    }
    public static GroovyResult error(String msg)
    {
        return GroovyResult.error(msg, null).setSuccess(false);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static GroovyResult error(String msg, Object data)
    {
        return new GroovyResult(500, msg, data).setSuccess(false);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static GroovyResult error(int code, String msg)
    {
        return new GroovyResult(code, msg, null).setSuccess(false);
    }
}