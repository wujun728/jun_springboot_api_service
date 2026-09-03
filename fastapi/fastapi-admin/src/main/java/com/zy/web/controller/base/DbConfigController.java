package com.zy.web.controller.base;

import com.zy.base.domain.DbConfig;
import com.zy.base.service.IDbConfigService;
import com.zy.common.annotation.Log;
import com.zy.common.constant.DbConfigConstants;
import com.zy.common.core.controller.BaseController;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.core.page.TableDataInfo;
import com.zy.common.enums.BusinessType;
import com.zy.common.utils.CollectionUtils;
import com.zy.common.utils.poi.ExcelUtil;
import com.zy.iservice.domain.IntfaceReverseHistory;
import com.zy.utils.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 数据源配置Controller
 *
 * @author javachen
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/base/dbConfig")
public class DbConfigController extends BaseController
{
    @Autowired
    private IDbConfigService dbConfigService;

    /**
     * 查询数据源配置列表
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(DbConfig dbConfig)
    {
        startPage();
        List<DbConfig> list = dbConfigService.selectDbConfigList(dbConfig);
        return getDataTable(list);
    }

    /**
     * 查询所有数据源配置
     */
    @GetMapping("/findAll")
    public AjaxResult findAll()
    {
        List<DbConfig> list = dbConfigService.findAll();
        return AjaxResult.success(list);
    }

    /**
     * 导出数据源配置列表
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:export')")
    @Log(title = "数据源配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DbConfig dbConfig)
    {
        List<DbConfig> list = dbConfigService.selectDbConfigList(dbConfig);
        ExcelUtil<DbConfig> util = new ExcelUtil<DbConfig>(DbConfig.class);
        util.exportExcel(response, list, "数据源配置数据");
    }

    /**
     * 获取数据源配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(dbConfigService.selectDbConfigById(id));
    }

    /**
     * 新增数据源配置
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:add')")
    @Log(title = "数据源配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DbConfig dbConfig)
    {
        dbConfig.setCreateBy(getUsername());
        dbConfig.setUpdateBy(getUsername());
        return toAjax(dbConfigService.insertDbConfig(dbConfig));
    }

    /**
     * 修改数据源配置
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:edit')")
    @Log(title = "数据源配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DbConfig dbConfig)
    {
        dbConfig.setUpdateBy(getUsername());
        return toAjax(dbConfigService.updateDbConfig(dbConfig));
    }

    /**
     * 删除数据源配置
     */
    @PreAuthorize("@ss.hasPermi('base:dbConfig:remove')")
    @Log(title = "数据源配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dbConfigService.deleteDbConfigByIds(ids));
    }

    /**
     * 检查数据库连接状态
     */
    @GetMapping("/checkStatus")
    public AjaxResult checkStatus()
    {
        List<DbConfig> dbConfigAll = dbConfigService.findAll();

        List<List<DbConfig>> list = CollectionUtils.averageAssign(dbConfigAll, 5);
        for (int i = 0; i < list.size(); i++) {
            int index = i;
            CompletableFuture.runAsync(() -> {
                checkStatusByList(list.get(index));
            });
        }
        return AjaxResult.success("自检已完成");
    }

    public void checkStatusByList(List<DbConfig> list){
        for (int i = 0; i < list.size(); i++) {
            DbConfig dbConfig = list.get(i);
            DBHelper dbHelper = new DBHelper();

            String sql = "";

            if (DbConfigConstants.MYSQL_DRIVER.equals(dbConfig.getConnectDriver())||
                    DbConfigConstants.POSTGRESQL_DRIVER.equals(dbConfig.getConnectDriver())||
                    DbConfigConstants.CLICKHOUSE_DRIVER.equals(dbConfig.getConnectDriver())) {
                sql = "select NOW()";
            }

            if (DbConfigConstants.ORACLE_DRIVER.equals(dbConfig.getConnectDriver())) {
                sql = "SELECT SYSDATE FROM DUAL";
            }

            if (DbConfigConstants.SQLSERVER_DRIVER.equals(dbConfig.getConnectDriver())) {
                sql = "select GETDATE()";
            }

            IntfaceReverseHistory intfaceReverseHistory = new IntfaceReverseHistory();

            List<Map<String,Object>> result = (List<Map<String,Object>>)dbHelper.executeSql(dbConfig, sql,intfaceReverseHistory);

            //如果有数据返回,检查状态为健康,否则为异常
            Long checkStatus = DbConfigConstants.CHECK_STATUS_OK;

            if (result.size()==0) {
                checkStatus = DbConfigConstants.CHECK_STATUS_ERROR;
            }

            dbConfig.setCheckStatus(checkStatus);
            dbConfig.setCheckTime(new Date());
            dbConfigService.updateCheckStatus(dbConfig);
        }
    }

}
