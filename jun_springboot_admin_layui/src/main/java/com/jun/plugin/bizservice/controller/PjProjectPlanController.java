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

import com.jun.plugin.bizservice.entity.PjProjectPlanEntity;
import com.jun.plugin.bizservice.service.PjProjectPlanService;



/**
 * 项目计划
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 13:34:51
 */
@Controller
@RequestMapping("/")
public class PjProjectPlanController {
    @Autowired
    private PjProjectPlanService pjProjectPlanService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectPlan")
    public String pjProjectPlan() {
        return "pjprojectplan/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectPlan/add")
    @RequiresPermissions("pjProjectPlan:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectPlanEntity pjProjectPlan){
        pjProjectPlanService.save(pjProjectPlan);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectPlan/delete")
    @RequiresPermissions("pjProjectPlan:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectPlanService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectPlan/update")
    @RequiresPermissions("pjProjectPlan:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectPlanEntity pjProjectPlan){
        pjProjectPlanService.updateById(pjProjectPlan);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectPlan/listByPage")
    @RequiresPermissions("pjProjectPlan:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjProjectPlanEntity pjProjectPlan){
        Page page = new Page(pjProjectPlan.getPage(), pjProjectPlan.getLimit());
        LambdaQueryWrapper<PjProjectPlanEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectPlanEntity::getId, pjProjectPlan.getId());
        IPage<PjProjectPlanEntity> iPage = pjProjectPlanService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
