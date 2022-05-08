package com.itboy.model;


import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 分页
 */
@Data
public class Pages extends BaseRowModel {

    private int page;//你要查询第几页

    private int limit;//每页多少行

    private String token;//全局token


    public int getPage() {
        if(page<0 || page==0) {
            page = 1;
        }
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        if(limit<0 || limit==0){limit=10;}
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
