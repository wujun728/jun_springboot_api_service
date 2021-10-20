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

import com.jun.plugin.bizservice.entity.OaOfficeCountEntity;
import com.jun.plugin.bizservice.mapper.OaOfficeCountMapper;
import com.jun.plugin.bizservice.service.OaOfficeCountService;



/**
 * 项目总结及评价
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaOfficeCountController {

    @Autowired
    private OaOfficeCountService oaOfficeCountService;

    @Autowired
    private OaOfficeCountMapper oaOfficeCountMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaOfficeCount")
    public String oaOfficeCount() {
        return "oaofficecount/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaOfficeCount/add")
    @RequiresPermissions("oaOfficeCount:add")
    @ResponseBody
    public DataResult add(@RequestBody OaOfficeCountEntity oaOfficeCount){
        oaOfficeCountService.save(oaOfficeCount);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaOfficeCount/delete")
    @RequiresPermissions("oaOfficeCount:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaOfficeCountService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaOfficeCount/update")
    @RequiresPermissions("oaOfficeCount:update")
    @ResponseBody
    public DataResult update(@RequestBody OaOfficeCountEntity oaOfficeCount){
        oaOfficeCountService.updateById(oaOfficeCount);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaOfficeCount/listByPage")
    @RequiresPermissions("oaOfficeCount:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaOfficeCountEntity oaOfficeCount){
        Page page = new Page(oaOfficeCount.getPage(), oaOfficeCount.getLimit());
        LambdaQueryWrapper<OaOfficeCountEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaOfficeCountEntity::getId, oaOfficeCount.getId());
        IPage<OaOfficeCountEntity> iPage = oaOfficeCountService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaOfficeCount/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaOfficeCountEntity oaOfficeCount){
    	LambdaQueryWrapper<OaOfficeCountEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaOfficeCountEntity::getId, oaOfficeCount.getId());
    	//OaOfficeCountEntity one = oaOfficeCountService.getOne(queryWrapper);
    	OaOfficeCountEntity one = oaOfficeCountService.getById(oaOfficeCount.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaOfficeCount/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaOfficeCountEntity oaOfficeCount){
        Page page = new Page(oaOfficeCount.getPage(), oaOfficeCount.getLimit());
        LambdaQueryWrapper<OaOfficeCountEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaOfficeCountEntity::getId, oaOfficeCount.getId());
        IPage<OaOfficeCountEntity> iPage = oaOfficeCountService.page(page, queryWrapper);
        log.info("\n this.oaOfficeCountMapper.selectCountUser()="+this.oaOfficeCountMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
