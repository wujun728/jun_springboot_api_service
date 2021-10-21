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

import com.jun.plugin.bizservice.entity.OaPomsWorkmarksOutsiteEntity;
import com.jun.plugin.bizservice.mapper.OaPomsWorkmarksOutsiteMapper;
import com.jun.plugin.bizservice.service.OaPomsWorkmarksOutsiteService;



/**
 * 外出信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaPomsWorkmarksOutsiteController {

    @Autowired
    private OaPomsWorkmarksOutsiteService oaPomsWorkmarksOutsiteService;

    @Autowired
    private OaPomsWorkmarksOutsiteMapper oaPomsWorkmarksOutsiteMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaPomsWorkmarksOutsite")
    public String oaPomsWorkmarksOutsite() {
        return "oapomsworkmarksoutsite/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaPomsWorkmarksOutsite/add")
    @RequiresPermissions("oaPomsWorkmarksOutsite:add")
    @ResponseBody
    public DataResult add(@RequestBody OaPomsWorkmarksOutsiteEntity oaPomsWorkmarksOutsite){
        oaPomsWorkmarksOutsiteService.save(oaPomsWorkmarksOutsite);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaPomsWorkmarksOutsite/delete")
    @RequiresPermissions("oaPomsWorkmarksOutsite:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaPomsWorkmarksOutsiteService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaPomsWorkmarksOutsite/update")
    @RequiresPermissions("oaPomsWorkmarksOutsite:update")
    @ResponseBody
    public DataResult update(@RequestBody OaPomsWorkmarksOutsiteEntity oaPomsWorkmarksOutsite){
        oaPomsWorkmarksOutsiteService.updateById(oaPomsWorkmarksOutsite);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaPomsWorkmarksOutsite/listByPage")
    @RequiresPermissions("oaPomsWorkmarksOutsite:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaPomsWorkmarksOutsiteEntity oaPomsWorkmarksOutsite){
        Page page = new Page(oaPomsWorkmarksOutsite.getPage(), oaPomsWorkmarksOutsite.getLimit());
        LambdaQueryWrapper<OaPomsWorkmarksOutsiteEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaPomsWorkmarksOutsiteEntity::getId, oaPomsWorkmarksOutsite.getId());
        IPage<OaPomsWorkmarksOutsiteEntity> iPage = oaPomsWorkmarksOutsiteService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaPomsWorkmarksOutsite/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaPomsWorkmarksOutsiteEntity oaPomsWorkmarksOutsite){
    	LambdaQueryWrapper<OaPomsWorkmarksOutsiteEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaPomsWorkmarksOutsiteEntity::getId, oaPomsWorkmarksOutsite.getId());
    	//OaPomsWorkmarksOutsiteEntity one = oaPomsWorkmarksOutsiteService.getOne(queryWrapper);
    	OaPomsWorkmarksOutsiteEntity one = oaPomsWorkmarksOutsiteService.getById(oaPomsWorkmarksOutsite.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaPomsWorkmarksOutsite/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaPomsWorkmarksOutsiteEntity oaPomsWorkmarksOutsite){
        Page page = new Page(oaPomsWorkmarksOutsite.getPage(), oaPomsWorkmarksOutsite.getLimit());
        LambdaQueryWrapper<OaPomsWorkmarksOutsiteEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaPomsWorkmarksOutsiteEntity::getId, oaPomsWorkmarksOutsite.getId());
        IPage<OaPomsWorkmarksOutsiteEntity> iPage = oaPomsWorkmarksOutsiteService.page(page, queryWrapper);
        log.info("\n this.oaPomsWorkmarksOutsiteMapper.selectCountUser()="+this.oaPomsWorkmarksOutsiteMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
