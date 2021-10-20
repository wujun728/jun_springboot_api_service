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

import com.jun.plugin.bizservice.entity.PjProjectProdessRiskEntity;
import com.jun.plugin.bizservice.mapper.PjProjectProdessRiskMapper;
import com.jun.plugin.bizservice.service.PjProjectProdessRiskService;



/**
 * 项目进度与风险
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectProdessRiskController {

    @Autowired
    private PjProjectProdessRiskService pjProjectProdessRiskService;

    @Autowired
    private PjProjectProdessRiskMapper pjProjectProdessRiskMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectProdessRisk")
    public String pjProjectProdessRisk() {
        return "pjprojectprodessrisk/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectProdessRisk/add")
    @RequiresPermissions("pjProjectProdessRisk:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectProdessRiskEntity pjProjectProdessRisk){
        pjProjectProdessRiskService.save(pjProjectProdessRisk);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectProdessRisk/delete")
    @RequiresPermissions("pjProjectProdessRisk:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectProdessRiskService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectProdessRisk/update")
    @RequiresPermissions("pjProjectProdessRisk:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectProdessRiskEntity pjProjectProdessRisk){
        pjProjectProdessRiskService.updateById(pjProjectProdessRisk);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectProdessRisk/listByPage")
    @RequiresPermissions("pjProjectProdessRisk:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectProdessRiskEntity pjProjectProdessRisk){
        Page page = new Page(pjProjectProdessRisk.getPage(), pjProjectProdessRisk.getLimit());
        LambdaQueryWrapper<PjProjectProdessRiskEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectProdessRiskEntity::getId, pjProjectProdessRisk.getId());
        IPage<PjProjectProdessRiskEntity> iPage = pjProjectProdessRiskService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectProdessRisk/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectProdessRiskEntity pjProjectProdessRisk){
    	LambdaQueryWrapper<PjProjectProdessRiskEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectProdessRiskEntity::getId, pjProjectProdessRisk.getId());
    	//PjProjectProdessRiskEntity one = pjProjectProdessRiskService.getOne(queryWrapper);
    	PjProjectProdessRiskEntity one = pjProjectProdessRiskService.getById(pjProjectProdessRisk.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectProdessRisk/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectProdessRiskEntity pjProjectProdessRisk){
        Page page = new Page(pjProjectProdessRisk.getPage(), pjProjectProdessRisk.getLimit());
        LambdaQueryWrapper<PjProjectProdessRiskEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectProdessRiskEntity::getId, pjProjectProdessRisk.getId());
        IPage<PjProjectProdessRiskEntity> iPage = pjProjectProdessRiskService.page(page, queryWrapper);
        log.info("\n this.pjProjectProdessRiskMapper.selectCountUser()="+this.pjProjectProdessRiskMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
