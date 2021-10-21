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

import com.jun.plugin.bizservice.entity.PjProjectInvoiceEntity;
import com.jun.plugin.bizservice.mapper.PjProjectInvoiceMapper;
import com.jun.plugin.bizservice.service.PjProjectInvoiceService;



/**
 * 项目开票
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectInvoiceController {

    @Autowired
    private PjProjectInvoiceService pjProjectInvoiceService;

    @Autowired
    private PjProjectInvoiceMapper pjProjectInvoiceMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectInvoice")
    public String pjProjectInvoice() {
        return "pjprojectinvoice/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectInvoice/add")
    @RequiresPermissions("pjProjectInvoice:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectInvoiceEntity pjProjectInvoice){
        pjProjectInvoiceService.save(pjProjectInvoice);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectInvoice/delete")
    @RequiresPermissions("pjProjectInvoice:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectInvoiceService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectInvoice/update")
    @RequiresPermissions("pjProjectInvoice:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectInvoiceEntity pjProjectInvoice){
        pjProjectInvoiceService.updateById(pjProjectInvoice);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectInvoice/listByPage")
    @RequiresPermissions("pjProjectInvoice:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectInvoiceEntity pjProjectInvoice){
        Page page = new Page(pjProjectInvoice.getPage(), pjProjectInvoice.getLimit());
        LambdaQueryWrapper<PjProjectInvoiceEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectInvoiceEntity::getId, pjProjectInvoice.getId());
        IPage<PjProjectInvoiceEntity> iPage = pjProjectInvoiceService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectInvoice/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectInvoiceEntity pjProjectInvoice){
    	LambdaQueryWrapper<PjProjectInvoiceEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectInvoiceEntity::getId, pjProjectInvoice.getId());
    	//PjProjectInvoiceEntity one = pjProjectInvoiceService.getOne(queryWrapper);
    	PjProjectInvoiceEntity one = pjProjectInvoiceService.getById(pjProjectInvoice.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectInvoice/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectInvoiceEntity pjProjectInvoice){
        Page page = new Page(pjProjectInvoice.getPage(), pjProjectInvoice.getLimit());
        LambdaQueryWrapper<PjProjectInvoiceEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectInvoiceEntity::getId, pjProjectInvoice.getId());
        IPage<PjProjectInvoiceEntity> iPage = pjProjectInvoiceService.page(page, queryWrapper);
        log.info("\n this.pjProjectInvoiceMapper.selectCountUser()="+this.pjProjectInvoiceMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
