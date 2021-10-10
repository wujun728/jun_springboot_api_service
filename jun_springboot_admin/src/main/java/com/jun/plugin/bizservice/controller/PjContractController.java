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

import com.jun.plugin.bizservice.entity.PjContractEntity;
import com.jun.plugin.bizservice.service.PjContractService;



/**
 * 业务约定书
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 17:00:12
 */
@Controller
@RequestMapping("/")
public class PjContractController {
    @Autowired
    private PjContractService pjContractService;


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
    public DataResult findListByPage(@RequestBody PjContractEntity pjContract){
        Page page = new Page(pjContract.getPage(), pjContract.getLimit());
        LambdaQueryWrapper<PjContractEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjContractEntity::getId, pjContract.getId());
        IPage<PjContractEntity> iPage = pjContractService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
