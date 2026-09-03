package com.zy.web.controller.iservice;

import com.zy.common.core.controller.BaseController;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.core.page.TableDataInfo;
import com.zy.iservice.domain.IntfaceReverseHistory;
import com.zy.iservice.service.IntfaceReverseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接口调用历史Controller
 *
 * @author javachen
 * @date 2022-11-22
 */
@RestController
@RequestMapping("/iservice/intfaceReverseHistory")
public class IntfaceReverseHistoryController extends BaseController
{
    @Autowired
    private IntfaceReverseHistoryService intfaceReverseHistoryService;

    /**
     * 查询接口调用历史列表
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceReverseHistory:list')")
    @GetMapping("/list")
    public TableDataInfo list(IntfaceReverseHistory intfaceReverseHistory)
    {
        startPage();
        List<IntfaceReverseHistory> list = intfaceReverseHistoryService.selectIntfaceReverseHistoryList(intfaceReverseHistory);
        return getDataTable(list);
    }

    /**
     * 获取接口调用历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('iservice:intfaceReverseHistory:list')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(intfaceReverseHistoryService.selectIntfaceReverseHistoryById(id));
    }

}
