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

import com.jun.plugin.bizservice.entity.PjProjectEntity;
import com.jun.plugin.bizservice.service.PjProjectService;



/**
 * 项目信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 15:45:46
 */
@Controller
@RequestMapping("/")
public class PjProjectController {
    @Autowired
    private PjProjectService pjProjectService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProject")
    public String pjProject() {
        return "pjproject/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProject/add")
    @RequiresPermissions("pjProject:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectEntity pjProject){
        pjProjectService.save(pjProject);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProject/delete")
    @RequiresPermissions("pjProject:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProject/update")
    @RequiresPermissions("pjProject:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectEntity pjProject){
        pjProjectService.updateById(pjProject);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProject/listByPage")
    @RequiresPermissions("pjProject:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjProjectEntity pjProject){
        Page page = new Page(pjProject.getPage(), pjProject.getLimit());
        LambdaQueryWrapper<PjProjectEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectEntity::getId, pjProject.getId());
        IPage<PjProjectEntity> iPage = pjProjectService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
