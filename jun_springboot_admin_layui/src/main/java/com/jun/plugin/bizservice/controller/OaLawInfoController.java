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

import com.jun.plugin.bizservice.entity.OaLawInfoEntity;
import com.jun.plugin.bizservice.mapper.OaLawInfoMapper;
import com.jun.plugin.bizservice.service.OaLawInfoService;



/**
 * 政策法规
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OaLawInfoController {

    @Autowired
    private OaLawInfoService oaLawInfoService;

    @Autowired
    private OaLawInfoMapper oaLawInfoMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaLawInfo")
    public String oaLawInfo() {
        return "oalawinfo/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaLawInfo/add")
    @RequiresPermissions("oaLawInfo:add")
    @ResponseBody
    public DataResult add(@RequestBody OaLawInfoEntity oaLawInfo){
        oaLawInfoService.save(oaLawInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaLawInfo/delete")
    @RequiresPermissions("oaLawInfo:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaLawInfoService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaLawInfo/update")
    @RequiresPermissions("oaLawInfo:update")
    @ResponseBody
    public DataResult update(@RequestBody OaLawInfoEntity oaLawInfo){
        oaLawInfoService.updateById(oaLawInfo);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaLawInfo/listByPage")
    @RequiresPermissions("oaLawInfo:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody OaLawInfoEntity oaLawInfo){
        Page page = new Page(oaLawInfo.getPage(), oaLawInfo.getLimit());
        LambdaQueryWrapper<OaLawInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaLawInfoEntity::getId, oaLawInfo.getId());
        IPage<OaLawInfoEntity> iPage = oaLawInfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("oaLawInfo/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody OaLawInfoEntity oaLawInfo){
    	LambdaQueryWrapper<OaLawInfoEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(OaLawInfoEntity::getId, oaLawInfo.getId());
    	//OaLawInfoEntity one = oaLawInfoService.getOne(queryWrapper);
    	OaLawInfoEntity one = oaLawInfoService.getById(oaLawInfo.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("oaLawInfo/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody OaLawInfoEntity oaLawInfo){
        Page page = new Page(oaLawInfo.getPage(), oaLawInfo.getLimit());
        LambdaQueryWrapper<OaLawInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaLawInfoEntity::getId, oaLawInfo.getId());
        IPage<OaLawInfoEntity> iPage = oaLawInfoService.page(page, queryWrapper);
        log.info("\n this.oaLawInfoMapper.selectCountUser()="+this.oaLawInfoMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
