package com.zy.web.controller.base;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.zy.base.domain.AppManager;
import com.zy.base.service.IAppManagerService;
import com.zy.common.utils.poi.ExcelUtil;
import com.zy.common.core.page.TableDataInfo;

/**
 * 应用管理Controller
 *
 * @author ruoyi
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/base/appManager")
public class AppManagerController extends BaseController
{
    @Autowired
    private IAppManagerService appManagerService;

    /**
     * 查询应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppManager appManager)
    {
        startPage();
        List<AppManager> list = appManagerService.selectAppManagerList(appManager);
        return getDataTable(list);
    }

    /**
     * 导出应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:export')")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppManager appManager)
    {
        List<AppManager> list = appManagerService.selectAppManagerList(appManager);
        ExcelUtil<AppManager> util = new ExcelUtil<AppManager>(AppManager.class);
        util.exportExcel(response, list, "应用管理数据");
    }

    /**
     * 获取应用管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(appManagerService.selectAppManagerById(id));
    }

    /**
     * 新增应用管理
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:add')")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppManager appManager)
    {
        appManager.setCreateBy(getUsername());
        appManager.setUpdateBy(getUsername());
        return toAjax(appManagerService.insertAppManager(appManager));
    }

    /**
     * 修改应用管理
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:edit')")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppManager appManager)
    {
        appManager.setUpdateBy(getUsername());
        return toAjax(appManagerService.updateAppManager(appManager));
    }

    /**
     * 删除应用管理
     */
    @PreAuthorize("@ss.hasPermi('base:appManager:remove')")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appManagerService.deleteAppManagerByIds(ids));
    }

    /**
     * 查询所有应用信息
     */
    @GetMapping("/findAll")
    public AjaxResult findAll()
    {
        return AjaxResult.success(appManagerService.findAll());
    }

}
