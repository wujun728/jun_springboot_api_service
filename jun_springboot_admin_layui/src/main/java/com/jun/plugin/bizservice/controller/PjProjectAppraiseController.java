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

import com.jun.plugin.bizservice.entity.PjProjectAppraiseEntity;
import com.jun.plugin.bizservice.mapper.PjProjectAppraiseMapper;
import com.jun.plugin.bizservice.service.PjProjectAppraiseService;



/**
 * 项目总结及评价
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectAppraiseController {

    @Autowired
    private PjProjectAppraiseService pjProjectAppraiseService;

    @Autowired
    private PjProjectAppraiseMapper pjProjectAppraiseMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectAppraise")
    public String pjProjectAppraise() {
        return "pjprojectappraise/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectAppraise/add")
    @RequiresPermissions("pjProjectAppraise:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectAppraiseEntity pjProjectAppraise){
        pjProjectAppraiseService.save(pjProjectAppraise);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectAppraise/delete")
    @RequiresPermissions("pjProjectAppraise:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectAppraiseService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectAppraise/update")
    @RequiresPermissions("pjProjectAppraise:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectAppraiseEntity pjProjectAppraise){
        pjProjectAppraiseService.updateById(pjProjectAppraise);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectAppraise/listByPage")
    @RequiresPermissions("pjProjectAppraise:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectAppraiseEntity pjProjectAppraise){
        Page page = new Page(pjProjectAppraise.getPage(), pjProjectAppraise.getLimit());
        LambdaQueryWrapper<PjProjectAppraiseEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectAppraiseEntity::getId, pjProjectAppraise.getId());
        IPage<PjProjectAppraiseEntity> iPage = pjProjectAppraiseService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectAppraise/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectAppraiseEntity pjProjectAppraise){
    	LambdaQueryWrapper<PjProjectAppraiseEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectAppraiseEntity::getId, pjProjectAppraise.getId());
    	//PjProjectAppraiseEntity one = pjProjectAppraiseService.getOne(queryWrapper);
    	PjProjectAppraiseEntity one = pjProjectAppraiseService.getById(pjProjectAppraise.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectAppraise/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectAppraiseEntity pjProjectAppraise){
        Page page = new Page(pjProjectAppraise.getPage(), pjProjectAppraise.getLimit());
        LambdaQueryWrapper<PjProjectAppraiseEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectAppraiseEntity::getId, pjProjectAppraise.getId());
        IPage<PjProjectAppraiseEntity> iPage = pjProjectAppraiseService.page(page, queryWrapper);
        log.info("\n this.pjProjectAppraiseMapper.selectCountUser()="+this.pjProjectAppraiseMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
