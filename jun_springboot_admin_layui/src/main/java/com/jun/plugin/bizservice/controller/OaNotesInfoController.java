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

import com.jun.plugin.bizservice.entity.OaNotesInfoEntity;
import com.jun.plugin.bizservice.mapper.OaNotesInfoMapper;
import com.jun.plugin.bizservice.service.OaNotesInfoService;



/**
 * 公告通知
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaNotesInfoController {

    @Autowired
    private OaNotesInfoService oaNotesInfoService;

    @Autowired
    private OaNotesInfoMapper oaNotesInfoMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaNotesInfo")
    public String oaNotesInfo() {
        return "oanotesinfo/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaNotesInfo/add")
    @RequiresPermissions("oaNotesInfo:add")
    @ResponseBody
    public DataResult add(@RequestBody OaNotesInfoEntity oaNotesInfo){
        oaNotesInfoService.save(oaNotesInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaNotesInfo/delete")
    @RequiresPermissions("oaNotesInfo:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaNotesInfoService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaNotesInfo/update")
    @RequiresPermissions("oaNotesInfo:update")
    @ResponseBody
    public DataResult update(@RequestBody OaNotesInfoEntity oaNotesInfo){
        oaNotesInfoService.updateById(oaNotesInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaNotesInfo/listByPage")
    @RequiresPermissions("oaNotesInfo:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaNotesInfoEntity oaNotesInfo){
        Page page = new Page(oaNotesInfo.getPage(), oaNotesInfo.getLimit());
        LambdaQueryWrapper<OaNotesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaNotesInfoEntity::getId, oaNotesInfo.getId());
        IPage<OaNotesInfoEntity> iPage = oaNotesInfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaNotesInfo/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaNotesInfoEntity oaNotesInfo){
    	LambdaQueryWrapper<OaNotesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaNotesInfoEntity::getId, oaNotesInfo.getId());
    	//OaNotesInfoEntity one = oaNotesInfoService.getOne(queryWrapper);
    	OaNotesInfoEntity one = oaNotesInfoService.getById(oaNotesInfo.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaNotesInfo/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaNotesInfoEntity oaNotesInfo){
        Page page = new Page(oaNotesInfo.getPage(), oaNotesInfo.getLimit());
        LambdaQueryWrapper<OaNotesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaNotesInfoEntity::getId, oaNotesInfo.getId());
        IPage<OaNotesInfoEntity> iPage = oaNotesInfoService.page(page, queryWrapper);
        log.info("\n this.oaNotesInfoMapper.selectCountUser()="+this.oaNotesInfoMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
