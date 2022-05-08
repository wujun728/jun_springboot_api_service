package com.itboy.controller;

import com.alibaba.fastjson.JSON;
import com.itboy.db.DataSourceFactory;
import com.itboy.db.DbSourceFactory;
import com.itboy.db.JdbcUtils;
import com.itboy.db.SqlDruidParser;
import com.itboy.model.*;
import com.itboy.service.DbSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName 控制层
 * @Description 操作数据源及执行sql
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 17:03
 **/
@Controller
@Slf4j
public class DbSourceController {

    @Resource
    private DbSourceService dbSourceService;

    @Resource
    private DbSourceFactory dbSourceFactory;

    @RequestMapping("dbSource/sourceController/page")
    public String dbsourcePage() {
        return "dbsourcePage";
    }

    @RequestMapping("dbSource/sqlController/sqlTextPage")
    public String sqlTextPage() {
        return "sqlTextPage";
    }

    @RequestMapping("dbSource/sqlExecute/sqlPage")
    public String sqlPage() {
        return "sqlPage";
    }

    @RequestMapping("dbSource/log/logPage")
    public String logPage() {
        return "logPage";
    }

    @RequestMapping("dbSource/sqlController/tablePage")
    @ResponseBody
    public String tablePage() {
        //可自行实现
        return "请通过SQL编写查询表";
    }

    @RequestMapping("dbSource/sourceController/addSourcePage")
    public String addSourcePage() {
        return "addSourcePage";
    }

    @RequestMapping("dbSource/dbsourceList")
    @ResponseBody
    public Map dbsourceList(DbSourceModel model) {
        Result<DbSourceModel> p = dbSourceService.selectDbSourceList(model);
        Map result = new HashMap();
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }

    /*测试数据源*/
    @RequestMapping("dbSource/testUrl")
    @ResponseBody
    public String testUrl(DbSourceModel model) {
        Connection conn = null;
        Statement pre = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(model.getDburl().trim(),
                    model.getDbaccount().trim(), model.getDbpass().trim());
            pre = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pre.executeQuery(model.getDbtestUrl());
            if (rs.first()) {
                return "连接成功！";
            } else {
                return "连接失败,返回结果集为空！";
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return "连接失败！error:" + e.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    /*新增数据源*/
    @RequestMapping("dbSource/sourceController/addDbSource")
    @ResponseBody
    public String addDbSource(DbSourceModel model) {
        try {
            DataSourceFactory.saveDataSource(model);
            dbSourceService.addDbSource(model);
            log.info("Successful Add  DbSources ");
        } catch (Exception e) {
            return "新增失败！error：" + e.getMessage();
        }
        return "新增成功！";
    }

    /*删除数据源*/
    @RequestMapping("dbSource/sourceController/delDbSource/{id}")
    @ResponseBody
    public String delDbSource(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        try {
            DbSourceModel model = dbSourceService.delDbSource(id);
            DataSourceFactory.removeDataSource(model.getDbname());
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败！error：" + e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    /*加载数据源*/
    @RequestMapping("dbSource/dbsourceSqlList")
    @ResponseBody
    public Map dbsourceSqlList(DbSourceModel model) {
        List<DbSourceModel> resultData = dbSourceService.dbsourceSqlList(model);
        Map result = new HashMap();
        result.put("code", 0);
        result.put("data", resultData);
        return result;
    }

    /*执行器*/
    @RequestMapping("dbSource/sqlExecute/executeSql")
    @ResponseBody
    public Map executeSql(ExecuteSql sql) {
        Map result = new HashMap();
        SysLog logs = new SysLog();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUserId() + ":" + user.getUserName();
        try {
            result.put("code", 1);
            Map<String, Object> sqlParser = SqlDruidParser.sqlParser(sql.getDataType(), sql.getSqlText());
            if (sqlParser.get("executeType") == null) {
                throw new NullPointerException("SQL解析异常");
            }
            result.put("sqlExecuteType", sqlParser.get("executeType"));
            List<String> executeSqlList = (List<String>) sqlParser.get("executeSql");
            List dataList = new ArrayList();
            if (sqlParser.get("executeType").equals("SELECT")) {
                for (String executeSql : executeSqlList
                ) {
                    Map<String, Object> resultData = JdbcUtils.findMoreResult(sql.getDataType(), executeSql, new ArrayList<>());
                    dataList.add(resultData);
                }
            } else {
                for (String executeSql : executeSqlList
                ) {
                    Map<String, Object> resultData = JdbcUtils.updateByPreparedStatement(sql.getDataType(), executeSql, new ArrayList<>());
                    dataList.add(resultData);
                }
            }
            //未改造日志记录
            logs.setLogContent(sql.getSqlText());
            logs.setLogResult(dataList.toString());
            logs.setLogType("1");
            logs.setLogName("SQL执行");
            logs.setLogDbSource(sql.getDataType());
            logs.setUserid(userName);
            result.put("dataList", dataList);
        } catch (Exception e) {
            logs.setLogContent(sql.getSqlText());
            logs.setLogResult(e.getMessage());
            logs.setLogType("1");
            logs.setLogName("sql执行记录");
            logs.setLogDbSource(sql.getDataType());
            logs.setUserid(userName);
            log.error(e.getMessage());
            result.put("code", 2);
            result.put("msg", e.getMessage());
        } finally {
            SysSetup sysSetup = dbSourceFactory.getSysSetUp();
            if (sysSetup.getCol3() == 1) {
                dbSourceService.insertLog(logs);
            }
        }
        return result;
    }


    /*暂未实现获取表功能*/
    @RequestMapping("dbSource/getTableList")
    @ResponseBody
    public Map getTableList(ExecuteSql sql) {
        Map result = new HashMap();
        return result;
    }

    /*sql文本列表*/
    @RequestMapping("dbSource/getSqlTextList")
    @ResponseBody
    public Map getSqlTextList(DbSqlText model) {
        Result<DbSqlText> p = dbSourceService.getDbSqlText(model);
        Map result = new HashMap();
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }

    /*保存sql文本*/
    @RequestMapping("dbSource/sqlController/saveSqlText")
    @ResponseBody
    public String saveSqlText(DbSqlText model) {
        try {
            dbSourceService.saveSqlText(model);
        } catch (Exception e) {
            return "新增失败！error：" + e.getMessage();
        }
        return "新增成功！";
    }

    /*查询sql文本*/
    @RequestMapping("dbSource/sqlController/sqlTextList")
    @ResponseBody
    public Map sqlTextList(DbSourceModel model) {
        List<DbSourceModel> resultData = dbSourceService.sqlTextList(model);
        Map result = new HashMap();
        result.put("code", 0);
        result.put("data", resultData);
        return result;
    }

    //删除sql
    @RequestMapping("dbSource/sqlController/delsqlText/{id}")
    @ResponseBody
    public String delsqlText(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        try {
            dbSourceService.delsqlText(id);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败！error：" + e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("dbSource/getLogList")
    @ResponseBody
    public Map getLogList(SysLog model) {
        Result<SysLog> p = dbSourceService.getLogList(model);
        Map result = new HashMap();
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }

    //删除日志
    @RequestMapping("dbSource/sqlController/delLog/{type}")
    @ResponseBody
    public String delLog(@PathVariable int type) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        try {
            if (type == 1) {
                dbSourceService.delSysLog();
            } else if (type == 2) {
                dbSourceService.delUserLog();
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败！error：" + e.getMessage());
        }
        return JSON.toJSONString(result);
    }

}
