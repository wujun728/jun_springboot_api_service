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
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import com.jun.plugin.system.common.utils.DataResult;
import com.jun.plugin.system.common.aop.annotation.DataScope;

import com.jun.plugin.bizservice.entity.PjProjectEntity;
import com.jun.plugin.bizservice.mapper.PjProjectMapper;
import com.jun.plugin.bizservice.service.PjProjectService;



/**
 * 项目信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectController {

    @Autowired
    private PjProjectService pjProjectService;

    @Autowired
    private PjProjectMapper pjProjectMapper;


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
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectEntity pjProject){
        Page page = new Page(pjProject.getPage(), pjProject.getLimit());
        LambdaQueryWrapper<PjProjectEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectEntity::getId, pjProject.getId());
        IPage<PjProjectEntity> iPage = pjProjectService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProject/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectEntity pjProject){
    	LambdaQueryWrapper<PjProjectEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectEntity::getId, pjProject.getId());
    	//PjProjectEntity one = pjProjectService.getOne(queryWrapper);
    	PjProjectEntity one = pjProjectService.getById(pjProject.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProject/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectEntity pjProject){
        Page page = new Page(pjProject.getPage(), pjProject.getLimit());
        LambdaQueryWrapper<PjProjectEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectEntity::getId, pjProject.getId());
        IPage<PjProjectEntity> iPage = pjProjectService.page(page, queryWrapper);
        log.info("\n this.pjProjectMapper.selectCountUser()="+this.pjProjectMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
