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

import com.jun.plugin.bizservice.entity.OaPomsWorkmarksPayrollEntity;
import com.jun.plugin.bizservice.service.OaPomsWorkmarksPayrollService;



/**
 * 工资审核发放
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 16:19:28
 */
@Controller
@RequestMapping("/")
public class OaPomsWorkmarksPayrollController {
    @Autowired
    private OaPomsWorkmarksPayrollService oaPomsWorkmarksPayrollService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaPomsWorkmarksPayroll")
    public String oaPomsWorkmarksPayroll() {
        return "oapomsworkmarkspayroll/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaPomsWorkmarksPayroll/add")
    @RequiresPermissions("oaPomsWorkmarksPayroll:add")
    @ResponseBody
    public DataResult add(@RequestBody OaPomsWorkmarksPayrollEntity oaPomsWorkmarksPayroll){
        oaPomsWorkmarksPayrollService.save(oaPomsWorkmarksPayroll);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaPomsWorkmarksPayroll/delete")
    @RequiresPermissions("oaPomsWorkmarksPayroll:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaPomsWorkmarksPayrollService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaPomsWorkmarksPayroll/update")
    @RequiresPermissions("oaPomsWorkmarksPayroll:update")
    @ResponseBody
    public DataResult update(@RequestBody OaPomsWorkmarksPayrollEntity oaPomsWorkmarksPayroll){
        oaPomsWorkmarksPayrollService.updateById(oaPomsWorkmarksPayroll);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaPomsWorkmarksPayroll/listByPage")
    @RequiresPermissions("oaPomsWorkmarksPayroll:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody OaPomsWorkmarksPayrollEntity oaPomsWorkmarksPayroll){
        Page page = new Page(oaPomsWorkmarksPayroll.getPage(), oaPomsWorkmarksPayroll.getLimit());
        LambdaQueryWrapper<OaPomsWorkmarksPayrollEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaPomsWorkmarksPayrollEntity::getId, oaPomsWorkmarksPayroll.getId());
        IPage<OaPomsWorkmarksPayrollEntity> iPage = oaPomsWorkmarksPayrollService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
