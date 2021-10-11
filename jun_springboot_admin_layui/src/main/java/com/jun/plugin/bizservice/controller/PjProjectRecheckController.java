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

import com.jun.plugin.bizservice.entity.PjProjectRecheckEntity;
import com.jun.plugin.bizservice.service.PjProjectRecheckService;



/**
 * 项目复核
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 13:36:30
 */
@Controller
@RequestMapping("/")
public class PjProjectRecheckController {
    @Autowired
    private PjProjectRecheckService pjProjectRecheckService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectRecheck")
    public String pjProjectRecheck() {
        return "pjprojectrecheck/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectRecheck/add")
    @RequiresPermissions("pjProjectRecheck:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectRecheckEntity pjProjectRecheck){
        pjProjectRecheckService.save(pjProjectRecheck);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectRecheck/delete")
    @RequiresPermissions("pjProjectRecheck:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectRecheckService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectRecheck/update")
    @RequiresPermissions("pjProjectRecheck:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectRecheckEntity pjProjectRecheck){
        pjProjectRecheckService.updateById(pjProjectRecheck);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectRecheck/listByPage")
    @RequiresPermissions("pjProjectRecheck:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjProjectRecheckEntity pjProjectRecheck){
        Page page = new Page(pjProjectRecheck.getPage(), pjProjectRecheck.getLimit());
        LambdaQueryWrapper<PjProjectRecheckEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectRecheckEntity::getId, pjProjectRecheck.getId());
        IPage<PjProjectRecheckEntity> iPage = pjProjectRecheckService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
