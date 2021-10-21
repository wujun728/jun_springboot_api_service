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

import com.jun.plugin.bizservice.entity.OaUserLeaveEntity;
import com.jun.plugin.bizservice.mapper.OaUserLeaveMapper;
import com.jun.plugin.bizservice.service.OaUserLeaveService;



/**
 * 员工请假
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaUserLeaveController {

    @Autowired
    private OaUserLeaveService oaUserLeaveService;

    @Autowired
    private OaUserLeaveMapper oaUserLeaveMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaUserLeave")
    public String oaUserLeave() {
        return "oauserleave/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaUserLeave/add")
    @RequiresPermissions("oaUserLeave:add")
    @ResponseBody
    public DataResult add(@RequestBody OaUserLeaveEntity oaUserLeave){
        oaUserLeaveService.save(oaUserLeave);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaUserLeave/delete")
    @RequiresPermissions("oaUserLeave:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaUserLeaveService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaUserLeave/update")
    @RequiresPermissions("oaUserLeave:update")
    @ResponseBody
    public DataResult update(@RequestBody OaUserLeaveEntity oaUserLeave){
        oaUserLeaveService.updateById(oaUserLeave);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaUserLeave/listByPage")
    @RequiresPermissions("oaUserLeave:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaUserLeaveEntity oaUserLeave){
        Page page = new Page(oaUserLeave.getPage(), oaUserLeave.getLimit());
        LambdaQueryWrapper<OaUserLeaveEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaUserLeaveEntity::getId, oaUserLeave.getId());
        IPage<OaUserLeaveEntity> iPage = oaUserLeaveService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaUserLeave/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaUserLeaveEntity oaUserLeave){
    	LambdaQueryWrapper<OaUserLeaveEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaUserLeaveEntity::getId, oaUserLeave.getId());
    	//OaUserLeaveEntity one = oaUserLeaveService.getOne(queryWrapper);
    	OaUserLeaveEntity one = oaUserLeaveService.getById(oaUserLeave.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaUserLeave/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaUserLeaveEntity oaUserLeave){
        Page page = new Page(oaUserLeave.getPage(), oaUserLeave.getLimit());
        LambdaQueryWrapper<OaUserLeaveEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaUserLeaveEntity::getId, oaUserLeave.getId());
        IPage<OaUserLeaveEntity> iPage = oaUserLeaveService.page(page, queryWrapper);
        log.info("\n this.oaUserLeaveMapper.selectCountUser()="+this.oaUserLeaveMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
