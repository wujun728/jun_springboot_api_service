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

import com.jun.plugin.bizservice.entity.PjProjectReportnumberEntity;
import com.jun.plugin.bizservice.service.PjProjectReportnumberService;



/**
 * 项目报告文号
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 17:26:13
 */
@Controller
@RequestMapping("/")
public class PjProjectReportnumberController {
    @Autowired
    private PjProjectReportnumberService pjProjectReportnumberService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectReportnumber")
    public String pjProjectReportnumber() {
        return "pjprojectreportnumber/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectReportnumber/add")
    @RequiresPermissions("pjProjectReportnumber:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectReportnumberEntity pjProjectReportnumber){
        pjProjectReportnumberService.save(pjProjectReportnumber);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectReportnumber/delete")
    @RequiresPermissions("pjProjectReportnumber:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectReportnumberService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectReportnumber/update")
    @RequiresPermissions("pjProjectReportnumber:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectReportnumberEntity pjProjectReportnumber){
        pjProjectReportnumberService.updateById(pjProjectReportnumber);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectReportnumber/listByPage")
    @RequiresPermissions("pjProjectReportnumber:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjProjectReportnumberEntity pjProjectReportnumber){
        Page page = new Page(pjProjectReportnumber.getPage(), pjProjectReportnumber.getLimit());
        LambdaQueryWrapper<PjProjectReportnumberEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectReportnumberEntity::getId, pjProjectReportnumber.getId());
        IPage<PjProjectReportnumberEntity> iPage = pjProjectReportnumberService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
