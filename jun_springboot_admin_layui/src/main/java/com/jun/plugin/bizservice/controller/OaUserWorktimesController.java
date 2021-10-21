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

import com.jun.plugin.bizservice.entity.OaUserWorktimesEntity;
import com.jun.plugin.bizservice.mapper.OaUserWorktimesMapper;
import com.jun.plugin.bizservice.service.OaUserWorktimesService;



/**
 * 考勤
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaUserWorktimesController {

    @Autowired
    private OaUserWorktimesService oaUserWorktimesService;

    @Autowired
    private OaUserWorktimesMapper oaUserWorktimesMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaUserWorktimes")
    public String oaUserWorktimes() {
        return "oauserworktimes/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaUserWorktimes/add")
    @RequiresPermissions("oaUserWorktimes:add")
    @ResponseBody
    public DataResult add(@RequestBody OaUserWorktimesEntity oaUserWorktimes){
        oaUserWorktimesService.save(oaUserWorktimes);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaUserWorktimes/delete")
    @RequiresPermissions("oaUserWorktimes:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaUserWorktimesService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaUserWorktimes/update")
    @RequiresPermissions("oaUserWorktimes:update")
    @ResponseBody
    public DataResult update(@RequestBody OaUserWorktimesEntity oaUserWorktimes){
        oaUserWorktimesService.updateById(oaUserWorktimes);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaUserWorktimes/listByPage")
    @RequiresPermissions("oaUserWorktimes:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaUserWorktimesEntity oaUserWorktimes){
        Page page = new Page(oaUserWorktimes.getPage(), oaUserWorktimes.getLimit());
        LambdaQueryWrapper<OaUserWorktimesEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaUserWorktimesEntity::getId, oaUserWorktimes.getId());
        IPage<OaUserWorktimesEntity> iPage = oaUserWorktimesService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaUserWorktimes/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaUserWorktimesEntity oaUserWorktimes){
    	LambdaQueryWrapper<OaUserWorktimesEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaUserWorktimesEntity::getId, oaUserWorktimes.getId());
    	//OaUserWorktimesEntity one = oaUserWorktimesService.getOne(queryWrapper);
    	OaUserWorktimesEntity one = oaUserWorktimesService.getById(oaUserWorktimes.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaUserWorktimes/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaUserWorktimesEntity oaUserWorktimes){
        Page page = new Page(oaUserWorktimes.getPage(), oaUserWorktimes.getLimit());
        LambdaQueryWrapper<OaUserWorktimesEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaUserWorktimesEntity::getId, oaUserWorktimes.getId());
        IPage<OaUserWorktimesEntity> iPage = oaUserWorktimesService.page(page, queryWrapper);
        log.info("\n this.oaUserWorktimesMapper.selectCountUser()="+this.oaUserWorktimesMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
