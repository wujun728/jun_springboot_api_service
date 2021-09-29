package com.jun.plugin.controller;

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
import com.jun.plugin.common.utils.DataResult;

import com.jun.plugin.entity.BizCustomerEntity;
import com.jun.plugin.service.BizCustomerService;



/**
 * 客户信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-09-25 20:46:48
 */
@Controller
@RequestMapping("/")
public class BizCustomerController {
    @Autowired
    private BizCustomerService bizCustomerService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/bizCustomer")
    public String bizCustomer() {
        return "bizcustomer/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("bizCustomer/add")
    @RequiresPermissions("bizCustomer:add")
    @ResponseBody
    public DataResult add(@RequestBody BizCustomerEntity bizCustomer){
        bizCustomerService.save(bizCustomer);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("bizCustomer/delete")
    @RequiresPermissions("bizCustomer:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        bizCustomerService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("bizCustomer/update")
    @RequiresPermissions("bizCustomer:update")
    @ResponseBody
    public DataResult update(@RequestBody BizCustomerEntity bizCustomer){
        bizCustomerService.updateById(bizCustomer);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("bizCustomer/listByPage")
    @RequiresPermissions("bizCustomer:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody BizCustomerEntity bizCustomer){
        Page page = new Page(bizCustomer.getPage(), bizCustomer.getLimit());
        LambdaQueryWrapper<BizCustomerEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(BizCustomerEntity::getId, bizCustomer.getId());
        IPage<BizCustomerEntity> iPage = bizCustomerService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
