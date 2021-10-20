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

import com.jun.plugin.bizservice.entity.PjContractEntity;
import com.jun.plugin.bizservice.mapper.PjContractMapper;
import com.jun.plugin.bizservice.service.PjContractService;



/**
 * 业务约定书
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjContractController {

    @Autowired
    private PjContractService pjContractService;

    @Autowired
    private PjContractMapper pjContractMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjContract")
    public String pjContract() {
        return "pjcontract/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjContract/add")
    @RequiresPermissions("pjContract:add")
    @ResponseBody
    public DataResult add(@RequestBody PjContractEntity pjContract){
        pjContractService.save(pjContract);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjContract/delete")
    @RequiresPermissions("pjContract:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjContractService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjContract/update")
    @RequiresPermissions("pjContract:update")
    @ResponseBody
    public DataResult update(@RequestBody PjContractEntity pjContract){
        pjContractService.updateById(pjContract);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjContract/listByPage")
    @RequiresPermissions("pjContract:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjContractEntity pjContract){
        Page page = new Page(pjContract.getPage(), pjContract.getLimit());
        LambdaQueryWrapper<PjContractEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjContractEntity::getId, pjContract.getId());
        IPage<PjContractEntity> iPage = pjContractService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjContract/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjContractEntity pjContract){
    	LambdaQueryWrapper<PjContractEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjContractEntity::getId, pjContract.getId());
    	//PjContractEntity one = pjContractService.getOne(queryWrapper);
    	PjContractEntity one = pjContractService.getById(pjContract.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjContract/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjContractEntity pjContract){
        Page page = new Page(pjContract.getPage(), pjContract.getLimit());
        LambdaQueryWrapper<PjContractEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjContractEntity::getId, pjContract.getId());
        IPage<PjContractEntity> iPage = pjContractService.page(page, queryWrapper);
        log.info("\n this.pjContractMapper.selectCountUser()="+this.pjContractMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
