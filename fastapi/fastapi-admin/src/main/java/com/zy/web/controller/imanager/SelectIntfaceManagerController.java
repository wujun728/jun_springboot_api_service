package com.zy.web.controller.imanager;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.zy.base.domain.AppManager;
import com.zy.base.service.IAppManagerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zy.common.annotation.Log;
import com.zy.common.core.controller.BaseController;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.enums.BusinessType;
import com.zy.imanager.domain.SelectIntfaceManager;
import com.zy.imanager.service.ISelectIntfaceManagerService;
import com.zy.common.utils.poi.ExcelUtil;
import com.zy.common.core.page.TableDataInfo;

/**
 * 查询接口管理Controller
 *
 * @author javachen
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/imanager/selectIntfaceManager")
public class SelectIntfaceManagerController extends BaseController
{
    @Autowired
    private ISelectIntfaceManagerService selectIntfaceManagerService;

    @Autowired
    private IAppManagerService appManagerService;

    /**
     * 查询查询接口管理列表
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:list')")
    @GetMapping("/list")
    public TableDataInfo list(SelectIntfaceManager selectIntfaceManager)
    {
        startPage();
        List<SelectIntfaceManager> list = selectIntfaceManagerService.selectSelectIntfaceManagerList(selectIntfaceManager);
        return getDataTable(list);
    }

    /**
     * 导出查询接口管理列表
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:export')")
    @Log(title = "查询接口管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SelectIntfaceManager selectIntfaceManager)
    {
        List<SelectIntfaceManager> list = selectIntfaceManagerService.selectSelectIntfaceManagerList(selectIntfaceManager);
        ExcelUtil<SelectIntfaceManager> util = new ExcelUtil<SelectIntfaceManager>(SelectIntfaceManager.class);
        util.exportExcel(response, list, "查询接口管理数据");
    }

    /**
     * 获取查询接口管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(selectIntfaceManagerService.selectSelectIntfaceManagerById(id));
    }

    /**
     * 新增查询接口管理
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:add')")
    @Log(title = "查询接口管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SelectIntfaceManager selectIntfaceManager)
    {
        Long appManagerId = selectIntfaceManager.getAppManagerId();
        AppManager appManager = appManagerService.selectAppManagerById(appManagerId);
        selectIntfaceManager.setAppManagerName(appManager.getName());
        selectIntfaceManager.setAppManagerPrefix(appManager.getPrefix());
        selectIntfaceManager.setCreateBy(getUsername());
        selectIntfaceManager.setUpdateBy(getUsername());

        String sqlStr = selectIntfaceManager.getSqlStr();
        sqlStr = sqlStr.trim();
        if(sqlStr.endsWith(";")){
            sqlStr = sqlStr.substring(0,sqlStr.length()-1);
        }
        selectIntfaceManager.setSqlStr(sqlStr);

        return toAjax(selectIntfaceManagerService.insertSelectIntfaceManager(selectIntfaceManager));
    }

    /**
     * 修改查询接口管理
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:edit')")
    @Log(title = "查询接口管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SelectIntfaceManager selectIntfaceManager)
    {
        Long appManagerId = selectIntfaceManager.getAppManagerId();
        AppManager appManager = appManagerService.selectAppManagerById(appManagerId);
        selectIntfaceManager.setAppManagerName(appManager.getName());
        selectIntfaceManager.setAppManagerPrefix(appManager.getPrefix());
        selectIntfaceManager.setUpdateBy(getUsername());

        String sqlStr = selectIntfaceManager.getSqlStr();
        sqlStr = sqlStr.trim();
        if(sqlStr.endsWith(";")){
            sqlStr = sqlStr.substring(0,sqlStr.length()-1);
        }
        selectIntfaceManager.setSqlStr(sqlStr);

        return toAjax(selectIntfaceManagerService.updateSelectIntfaceManager(selectIntfaceManager));
    }

    /**
     * 修改转换信息
     */
    @Log(title = "查询接口管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateConverInfo")
    public AjaxResult updateConverInfo(@RequestBody SelectIntfaceManager selectIntfaceManager)
    {
        selectIntfaceManagerService.updateConverInfo(selectIntfaceManager);
        return AjaxResult.success("修改成功");
    }


    /**
     * 删除查询接口管理
     */
    @PreAuthorize("@ss.hasPermi('imanager:selectIntfaceManager:remove')")
    @Log(title = "查询接口管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(selectIntfaceManagerService.deleteSelectIntfaceManagerByIds(ids));
    }
}
