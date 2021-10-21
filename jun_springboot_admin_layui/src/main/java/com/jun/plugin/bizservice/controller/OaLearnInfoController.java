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

import com.jun.plugin.bizservice.entity.OaLearnInfoEntity;
import com.jun.plugin.bizservice.mapper.OaLearnInfoMapper;
import com.jun.plugin.bizservice.service.OaLearnInfoService;



/**
 * 培训学习
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaLearnInfoController {

    @Autowired
    private OaLearnInfoService oaLearnInfoService;

    @Autowired
    private OaLearnInfoMapper oaLearnInfoMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaLearnInfo")
    public String oaLearnInfo() {
        return "oalearninfo/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaLearnInfo/add")
    @RequiresPermissions("oaLearnInfo:add")
    @ResponseBody
    public DataResult add(@RequestBody OaLearnInfoEntity oaLearnInfo){
        oaLearnInfoService.save(oaLearnInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaLearnInfo/delete")
    @RequiresPermissions("oaLearnInfo:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaLearnInfoService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaLearnInfo/update")
    @RequiresPermissions("oaLearnInfo:update")
    @ResponseBody
    public DataResult update(@RequestBody OaLearnInfoEntity oaLearnInfo){
        oaLearnInfoService.updateById(oaLearnInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaLearnInfo/listByPage")
    @RequiresPermissions("oaLearnInfo:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaLearnInfoEntity oaLearnInfo){
        Page page = new Page(oaLearnInfo.getPage(), oaLearnInfo.getLimit());
        LambdaQueryWrapper<OaLearnInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaLearnInfoEntity::getId, oaLearnInfo.getId());
        IPage<OaLearnInfoEntity> iPage = oaLearnInfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaLearnInfo/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaLearnInfoEntity oaLearnInfo){
    	LambdaQueryWrapper<OaLearnInfoEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaLearnInfoEntity::getId, oaLearnInfo.getId());
    	//OaLearnInfoEntity one = oaLearnInfoService.getOne(queryWrapper);
    	OaLearnInfoEntity one = oaLearnInfoService.getById(oaLearnInfo.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaLearnInfo/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaLearnInfoEntity oaLearnInfo){
        Page page = new Page(oaLearnInfo.getPage(), oaLearnInfo.getLimit());
        LambdaQueryWrapper<OaLearnInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaLearnInfoEntity::getId, oaLearnInfo.getId());
        IPage<OaLearnInfoEntity> iPage = oaLearnInfoService.page(page, queryWrapper);
        log.info("\n this.oaLearnInfoMapper.selectCountUser()="+this.oaLearnInfoMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
