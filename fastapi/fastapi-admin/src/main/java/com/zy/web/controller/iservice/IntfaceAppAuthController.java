package com.zy.web.controller.iservice;

import com.zy.common.annotation.Log;
import com.zy.common.core.controller.BaseController;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.core.page.TableDataInfo;
import com.zy.common.enums.BusinessType;
import com.zy.iservice.domain.bo.InsertUpdateIntfaceAppAuthBO;
import com.zy.iservice.domain.bo.SelectIntfaceAppAuthListBO;
import com.zy.iservice.service.IntfaceAppAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接口应用授权Controller
 *
 * @author javachen
 * @date 2022-11-19
 */
@RestController
@RequestMapping("/iservice/intfaceAppAuth")
public class IntfaceAppAuthController extends BaseController
{
    @Autowired
    private IntfaceAppAuthService intfaceAppAuthService;

    /**
     * 查询接口应用授权列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SelectIntfaceAppAuthListBO selectIntfaceAppAuthListBO)
    {
        startPage();
        List<SelectIntfaceAppAuthListBO> list = intfaceAppAuthService.selectIntfaceAppAuthList(selectIntfaceAppAuthListBO);
        return getDataTable(list);
    }

    /**
     * 添加授权
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:addAuth')")
    @Log(title = "接口应用授权", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InsertUpdateIntfaceAppAuthBO insertUpdateIntfaceAppAuthBO)
    {
        intfaceAppAuthService.insertIntfaceAppAuth(insertUpdateIntfaceAppAuthBO);
        return  AjaxResult.success("授权成功");
    }

    /**
     * 取消授权
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceAppApplyFor:cancelAuth')")
    @Log(title = "接口应用授权", businessType = BusinessType.DELETE)
    @PostMapping("/cancleAuth")
    public AjaxResult cancleAuth(@RequestBody InsertUpdateIntfaceAppAuthBO insertIntfaceAppAuth)
    {
        intfaceAppAuthService.cancleAuth(insertIntfaceAppAuth);
        return  AjaxResult.success("已取消授权");
    }

}
