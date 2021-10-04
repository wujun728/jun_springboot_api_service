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

import com.jun.plugin.bizservice.entity.BizCustomerTestEntity;
import com.jun.plugin.bizservice.service.BizCustomerTestService;



/**
 * 客户信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-04 15:38:01
 */
@Controller
@RequestMapping("/")
public class BizCustomerTestController {
    @Autowired
    private BizCustomerTestService bizCustomerTestService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/bizCustomerTest")
    public String bizCustomerTest() {
        return "bizcustomertest/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("bizCustomerTest/add")
    @RequiresPermissions("bizCustomerTest:add")
    @ResponseBody
    public DataResult add(@RequestBody BizCustomerTestEntity bizCustomerTest){
        bizCustomerTestService.save(bizCustomerTest);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("bizCustomerTest/delete")
    @RequiresPermissions("bizCustomerTest:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        bizCustomerTestService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("bizCustomerTest/update")
    @RequiresPermissions("bizCustomerTest:update")
    @ResponseBody
    public DataResult update(@RequestBody BizCustomerTestEntity bizCustomerTest){
        bizCustomerTestService.updateById(bizCustomerTest);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("bizCustomerTest/listByPage")
    @RequiresPermissions("bizCustomerTest:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody BizCustomerTestEntity bizCustomerTest){
        Page page = new Page(bizCustomerTest.getPage(), bizCustomerTest.getLimit());
        LambdaQueryWrapper<BizCustomerTestEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(BizCustomerTestEntity::getId, bizCustomerTest.getId());
        IPage<BizCustomerTestEntity> iPage = bizCustomerTestService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
