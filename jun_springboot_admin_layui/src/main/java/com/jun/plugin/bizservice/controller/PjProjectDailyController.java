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

import com.jun.plugin.bizservice.entity.PjProjectDailyEntity;
import com.jun.plugin.bizservice.mapper.PjProjectDailyMapper;
import com.jun.plugin.bizservice.service.PjProjectDailyService;



/**
 * 项目日报周报
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectDailyController {

    @Autowired
    private PjProjectDailyService pjProjectDailyService;

    @Autowired
    private PjProjectDailyMapper pjProjectDailyMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectDaily")
    public String pjProjectDaily() {
        return "pjprojectdaily/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectDaily/add")
    @RequiresPermissions("pjProjectDaily:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectDailyEntity pjProjectDaily){
        pjProjectDailyService.save(pjProjectDaily);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectDaily/delete")
    @RequiresPermissions("pjProjectDaily:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectDailyService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectDaily/update")
    @RequiresPermissions("pjProjectDaily:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectDailyEntity pjProjectDaily){
        pjProjectDailyService.updateById(pjProjectDaily);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectDaily/listByPage")
    @RequiresPermissions("pjProjectDaily:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectDailyEntity pjProjectDaily){
        Page page = new Page(pjProjectDaily.getPage(), pjProjectDaily.getLimit());
        LambdaQueryWrapper<PjProjectDailyEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectDailyEntity::getId, pjProjectDaily.getId());
        IPage<PjProjectDailyEntity> iPage = pjProjectDailyService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectDaily/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectDailyEntity pjProjectDaily){
    	LambdaQueryWrapper<PjProjectDailyEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectDailyEntity::getId, pjProjectDaily.getId());
    	//PjProjectDailyEntity one = pjProjectDailyService.getOne(queryWrapper);
    	PjProjectDailyEntity one = pjProjectDailyService.getById(pjProjectDaily.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectDaily/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectDailyEntity pjProjectDaily){
        Page page = new Page(pjProjectDaily.getPage(), pjProjectDaily.getLimit());
        LambdaQueryWrapper<PjProjectDailyEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectDailyEntity::getId, pjProjectDaily.getId());
        IPage<PjProjectDailyEntity> iPage = pjProjectDailyService.page(page, queryWrapper);
        log.info("\n this.pjProjectDailyMapper.selectCountUser()="+this.pjProjectDailyMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
