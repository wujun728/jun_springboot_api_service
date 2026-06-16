package com.ruoyi.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.business.domain.TestData;
import com.ruoyi.business.service.ITestDataService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseRestController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 测试数据Controller
 * 
 * @author ruoyi
 * @date 2024-06-15
 */
@RestController
@RequestMapping("/business/data")
public class TestDataController extends BaseRestController {

    @Resource
    private ITestDataService testDataService;

    /**
     * 查询测试数据列表
     */
    @PreAuthorize("@ss.hasPermi('business:data:list')")
    @PostMapping("/list")
    public TableDataInfo<TestData> list(@RequestBody TestData testData)
    {
        startPage();
        LambdaQueryWrapper<TestData> wrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotEmpty(testData.getName())){
            wrapper.like(TestData::getName,testData.getName());
        }
        List<TestData> list = testDataService.list(wrapper);
        return getDataTable(list);
    }

    /**
     * 导出测试数据列表
     */
    @PreAuthorize("@ss.hasPermi('business:data:export')")
    @Log(title = "测试数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TestData testData)
    {
        LambdaQueryWrapper<TestData> wrapper = Wrappers.lambdaQuery();
        List<TestData> list = testDataService.list(wrapper);
        ExcelUtil<TestData> util = new ExcelUtil<TestData>(TestData.class);
        util.exportExcel(response, list, "测试数据数据");
    }

    /**
     * 获取测试数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:data:query')")
    @GetMapping(value = "/{id}")
    public R<TestData> getInfo(@PathVariable("id") Long id)
    {
        return success(testDataService.getById(id));
    }

    /**
     * 新增测试数据
     */
    @PreAuthorize("@ss.hasPermi('business:data:add')")
    @Log(title = "测试数据", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody TestData testData)
    {
        return toAjax(testDataService.save(testData));
    }

    /**
     * 修改测试数据
     */
    @PreAuthorize("@ss.hasPermi('business:data:edit')")
    @Log(title = "测试数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody TestData testData)
    {
        return toAjax(testDataService.updateById(testData));
    }

    /**
     * 删除测试数据
     */
    @PreAuthorize("@ss.hasPermi('business:data:remove')")
    @Log(title = "测试数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids)
    {
        return toAjax(testDataService.removeByIds(Arrays.asList(ids)));
    }
}
