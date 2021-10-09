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

import com.jun.plugin.bizservice.entity.PjCustomerEntity;
import com.jun.plugin.bizservice.service.PjCustomerService;



/**
 * 客户信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 14:55:57
 */
@Controller
@RequestMapping("/")
public class PjCustomerController {
    @Autowired
    private PjCustomerService pjCustomerService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjCustomer")
    public String pjCustomer() {
        return "pjcustomer/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjCustomer/add")
    @RequiresPermissions("pjCustomer:add")
    @ResponseBody
    public DataResult add(@RequestBody PjCustomerEntity pjCustomer){
        pjCustomerService.save(pjCustomer);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjCustomer/delete")
    @RequiresPermissions("pjCustomer:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjCustomerService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjCustomer/update")
    @RequiresPermissions("pjCustomer:update")
    @ResponseBody
    public DataResult update(@RequestBody PjCustomerEntity pjCustomer){
        pjCustomerService.updateById(pjCustomer);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjCustomer/listByPage")
    @RequiresPermissions("pjCustomer:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody PjCustomerEntity pjCustomer){
        Page page = new Page(pjCustomer.getPage(), pjCustomer.getLimit());
        LambdaQueryWrapper<PjCustomerEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjCustomerEntity::getId, pjCustomer.getId());
        IPage<PjCustomerEntity> iPage = pjCustomerService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
