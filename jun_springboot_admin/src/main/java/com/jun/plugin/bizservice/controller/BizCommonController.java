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

import com.jun.plugin.bizservice.entity.BizCommonEntity;
import com.jun.plugin.bizservice.service.BizCommonService;



/**
 * 公共信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 15:25:29
 */
@Controller
@RequestMapping("/")
public class BizCommonController {
    @Autowired
    private BizCommonService bizCommonService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/bizCommon")
    public String bizCommon() {
        return "bizcommon/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("bizCommon/add")
    @RequiresPermissions("bizCommon:add")
    @ResponseBody
    public DataResult add(@RequestBody BizCommonEntity bizCommon){
        bizCommonService.save(bizCommon);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("bizCommon/delete")
    @RequiresPermissions("bizCommon:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        bizCommonService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("bizCommon/update")
    @RequiresPermissions("bizCommon:update")
    @ResponseBody
    public DataResult update(@RequestBody BizCommonEntity bizCommon){
        bizCommonService.updateById(bizCommon);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("bizCommon/listByPage")
    @RequiresPermissions("bizCommon:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody BizCommonEntity bizCommon){
        Page page = new Page(bizCommon.getPage(), bizCommon.getLimit());
        LambdaQueryWrapper<BizCommonEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(BizCommonEntity::getId, bizCommon.getId());
        IPage<BizCommonEntity> iPage = bizCommonService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
