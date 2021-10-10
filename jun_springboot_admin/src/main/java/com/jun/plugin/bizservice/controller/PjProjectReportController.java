package com.jun.plugin.bizservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import com.jun.plugin.system.common.utils.DataResult;

import com.jun.plugin.bizservice.entity.PjProjectReportEntity;
import com.jun.plugin.bizservice.service.PjProjectReportService;



/**
 * 项目报告
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 17:14:54
 */
@Controller
@RequestMapping("/")
public class PjProjectReportController {
    @Autowired
    private PjProjectReportService pjProjectReportService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectReport")
    public String pjProjectReport() {
        return "pjprojectreport/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectReport/add")
    @RequiresPermissions("pjProjectReport:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectReportEntity pjProjectReport){
        pjProjectReportService.save(pjProjectReport);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectReport/delete")
    @RequiresPermissions("pjProjectReport:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectReportService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectReport/update")
    @RequiresPermissions("pjProjectReport:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectReportEntity pjProjectReport){
        pjProjectReportService.updateById(pjProjectReport);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectReport/listByPage")
    @RequiresPermissions("pjProjectReport:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjProjectReportEntity pjProjectReport){
        Page page = new Page(pjProjectReport.getPage(), pjProjectReport.getLimit());
        LambdaQueryWrapper<PjProjectReportEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectReportEntity::getId, pjProjectReport.getId());
        IPage<PjProjectReportEntity> iPage = pjProjectReportService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
