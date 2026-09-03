package com.zy.web.controller.iservice;

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
import com.zy.iservice.domain.IntfaceAppApplyFor;
import com.zy.iservice.service.IntfaceAppApplyForService;
import com.zy.common.utils.poi.ExcelUtil;
import com.zy.common.core.page.TableDataInfo;

/**
 * 接口应用申请Controller
 *
 * @author javachen
 * @date 2022-11-19
 */
@RestController
@RequestMapping("/iservice/intfaceAppApplyFor")
public class IntfaceAppApplyForController extends BaseController
{
    @Autowired
    private IntfaceAppApplyForService intfaceAppApplyForService;

    /**
     * 查询接口应用申请列表
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:list')")
    @GetMapping("/list")
    public TableDataInfo list(IntfaceAppApplyFor intfaceAppApplyFor)
    {
        startPage();
        List<IntfaceAppApplyFor> list = intfaceAppApplyForService.selectIntfaceAppApplyForList(intfaceAppApplyFor);
        return getDataTable(list);
    }

    /**
     * 导出接口应用申请列表
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:export')")
    @Log(title = "接口应用申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IntfaceAppApplyFor intfaceAppApplyFor)
    {
        List<IntfaceAppApplyFor> list = intfaceAppApplyForService.selectIntfaceAppApplyForList(intfaceAppApplyFor);
        ExcelUtil<IntfaceAppApplyFor> util = new ExcelUtil<IntfaceAppApplyFor>(IntfaceAppApplyFor.class);
        util.exportExcel(response, list, "接口应用申请数据");
    }

    /**
     * 获取接口应用申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(intfaceAppApplyForService.selectIntfaceAppApplyForById(id));
    }

    /**
     * 新增接口应用申请
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:add')")
    @Log(title = "接口应用申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IntfaceAppApplyFor intfaceAppApplyFor)
    {
        return toAjax(intfaceAppApplyForService.insertIntfaceAppApplyFor(intfaceAppApplyFor));
    }

    /**
     * 修改接口应用申请
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:edit')")
    @Log(title = "接口应用申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IntfaceAppApplyFor intfaceAppApplyFor)
    {
        return toAjax(intfaceAppApplyForService.updateIntfaceAppApplyFor(intfaceAppApplyFor));
    }

    /**
     * 查询密钥
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:query')")
    @Log(title = "查询密钥", businessType = BusinessType.OTHER)
	@GetMapping("/selectSecret/{id}")
    public AjaxResult selectSecret(@PathVariable Long id)
    {
        return AjaxResult.success(intfaceAppApplyForService.selectSecret(id));
    }

    /**
     * 重置密钥
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:edit')")
    @Log(title = "查询密钥", businessType = BusinessType.UPDATE)
    @GetMapping("/resetSecret/{id}")
    public AjaxResult resetSecret(@PathVariable Long id)
    {
        intfaceAppApplyForService.resetSecret(id);
        return AjaxResult.success("重置成功");
    }

    /**
     * 删除接口应用申请
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:remove')")
    @Log(title = "接口应用申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(intfaceAppApplyForService.deleteIntfaceAppApplyForByIds(ids));
    }

}
