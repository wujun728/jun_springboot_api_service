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

import com.jun.plugin.bizservice.entity.PjProjectDraftEntity;
import com.jun.plugin.bizservice.mapper.PjProjectDraftMapper;
import com.jun.plugin.bizservice.service.PjProjectDraftService;



/**
 * 项目底稿
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectDraftController {

    @Autowired
    private PjProjectDraftService pjProjectDraftService;

    @Autowired
    private PjProjectDraftMapper pjProjectDraftMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectDraft")
    public String pjProjectDraft() {
        return "pjprojectdraft/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectDraft/add")
    @RequiresPermissions("pjProjectDraft:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectDraftEntity pjProjectDraft){
        pjProjectDraftService.save(pjProjectDraft);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectDraft/delete")
    @RequiresPermissions("pjProjectDraft:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectDraftService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectDraft/update")
    @RequiresPermissions("pjProjectDraft:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectDraftEntity pjProjectDraft){
        pjProjectDraftService.updateById(pjProjectDraft);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectDraft/listByPage")
    @RequiresPermissions("pjProjectDraft:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectDraftEntity pjProjectDraft){
        Page page = new Page(pjProjectDraft.getPage(), pjProjectDraft.getLimit());
        LambdaQueryWrapper<PjProjectDraftEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectDraftEntity::getId, pjProjectDraft.getId());
        IPage<PjProjectDraftEntity> iPage = pjProjectDraftService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectDraft/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectDraftEntity pjProjectDraft){
    	LambdaQueryWrapper<PjProjectDraftEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectDraftEntity::getId, pjProjectDraft.getId());
    	//PjProjectDraftEntity one = pjProjectDraftService.getOne(queryWrapper);
    	PjProjectDraftEntity one = pjProjectDraftService.getById(pjProjectDraft.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectDraft/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectDraftEntity pjProjectDraft){
        Page page = new Page(pjProjectDraft.getPage(), pjProjectDraft.getLimit());
        LambdaQueryWrapper<PjProjectDraftEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectDraftEntity::getId, pjProjectDraft.getId());
        IPage<PjProjectDraftEntity> iPage = pjProjectDraftService.page(page, queryWrapper);
        log.info("\n this.pjProjectDraftMapper.selectCountUser()="+this.pjProjectDraftMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
