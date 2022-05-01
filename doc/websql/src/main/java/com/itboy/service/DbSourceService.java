package com.itboy.service;

import com.itboy.model.*;

import java.util.List;

/**
 * @ClassName DbSourceService
 * @Description 数据源接口
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 17:33
 **/
public interface DbSourceService {
    Result<DbSourceModel> selectDbSourceList(DbSourceModel model);

    void addDbSource(DbSourceModel model);

    List<DbSourceModel> dbsourceSqlList(DbSourceModel model);

    void saveSqlText(DbSqlText model);

    List<DbSourceModel> sqlTextList(DbSourceModel model);

    Result<DbSqlText> getDbSqlText(DbSqlText model);

    DbSourceModel delDbSource(String id);

    SysSetup initSysSetup();

    void delsqlText(String id);

    void insertLog(SysLog logs);

    Result<SysLog> getLogList(SysLog model);

    void delSysLog();

    void delUserLog();

}
