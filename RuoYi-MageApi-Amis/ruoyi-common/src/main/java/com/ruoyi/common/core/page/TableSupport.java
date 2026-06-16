package com.ruoyi.common.core.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ServletUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * 表格数据处理
 * 
 * @author ruoyi
 */
public class TableSupport
{
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        JSONObject bodyJson = getBodyJson();
        pageDomain.setPageNum(Convert.toInt(Optional.ofNullable(ServletUtils.getParameter(PAGE_NUM)).orElse(bodyJson.getString(PAGE_NUM)), 1));
        pageDomain.setPageSize(Convert.toInt(Optional.ofNullable(ServletUtils.getParameter(PAGE_SIZE)).orElse(bodyJson.getString(PAGE_SIZE)), 10));
        pageDomain.setOrderByColumn(Optional.ofNullable(ServletUtils.getParameter(ORDER_BY_COLUMN)).orElse(bodyJson.getString(ORDER_BY_COLUMN)));
        pageDomain.setIsAsc(Optional.ofNullable(ServletUtils.getParameter(IS_ASC)).orElse(bodyJson.getString(IS_ASC)));
        pageDomain.setReasonable(Optional.ofNullable(ServletUtils.getParameterToBool(REASONABLE)).orElse(bodyJson.getBoolean(REASONABLE)));
        return pageDomain;
    }

    public static JSONObject getBodyJson(){
        try {
            return Optional.ofNullable(JSON.parseObject(IOUtils.toString(ServletUtils.getRequest().getReader()))).orElse(new JSONObject());
        } catch (IOException e) {
            return new JSONObject();
        }
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
