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

import com.jun.plugin.bizservice.entity.HrTempletQuotaDetailUserscoreDetailEntity;
import com.jun.plugin.bizservice.mapper.HrTempletQuotaDetailUserscoreDetailMapper;
import com.jun.plugin.bizservice.service.HrTempletQuotaDetailUserscoreDetailService;



/**
 * 用户考核明细
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class HrTempletQuotaDetailUserscoreDetailController {

    @Autowired
    private HrTempletQuotaDetailUserscoreDetailService hrTempletQuotaDetailUserscoreDetailService;

    @Autowired
    private HrTempletQuotaDetailUserscoreDetailMapper hrTempletQuotaDetailUserscoreDetailMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/hrTempletQuotaDetailUserscoreDetail")
    public String hrTempletQuotaDetailUserscoreDetail() {
        return "hrtempletquotadetailuserscoredetail/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("hrTempletQuotaDetailUserscoreDetail/add")
    @RequiresPermissions("hrTempletQuotaDetailUserscoreDetail:add")
    @ResponseBody
    public DataResult add(@RequestBody HrTempletQuotaDetailUserscoreDetailEntity hrTempletQuotaDetailUserscoreDetail){
        hrTempletQuotaDetailUserscoreDetailService.save(hrTempletQuotaDetailUserscoreDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("hrTempletQuotaDetailUserscoreDetail/delete")
    @RequiresPermissions("hrTempletQuotaDetailUserscoreDetail:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        hrTempletQuotaDetailUserscoreDetailService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("hrTempletQuotaDetailUserscoreDetail/update")
    @RequiresPermissions("hrTempletQuotaDetailUserscoreDetail:update")
    @ResponseBody
    public DataResult update(@RequestBody HrTempletQuotaDetailUserscoreDetailEntity hrTempletQuotaDetailUserscoreDetail){
        hrTempletQuotaDetailUserscoreDetailService.updateById(hrTempletQuotaDetailUserscoreDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("hrTempletQuotaDetailUserscoreDetail/listByPage")
    @RequiresPermissions("hrTempletQuotaDetailUserscoreDetail:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody HrTempletQuotaDetailUserscoreDetailEntity hrTempletQuotaDetailUserscoreDetail){
        Page page = new Page(hrTempletQuotaDetailUserscoreDetail.getPage(), hrTempletQuotaDetailUserscoreDetail.getLimit());
        LambdaQueryWrapper<HrTempletQuotaDetailUserscoreDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletQuotaDetailUserscoreDetailEntity::getId, hrTempletQuotaDetailUserscoreDetail.getId());
        IPage<HrTempletQuotaDetailUserscoreDetailEntity> iPage = hrTempletQuotaDetailUserscoreDetailService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("hrTempletQuotaDetailUserscoreDetail/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody HrTempletQuotaDetailUserscoreDetailEntity hrTempletQuotaDetailUserscoreDetail){
    	LambdaQueryWrapper<HrTempletQuotaDetailUserscoreDetailEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(HrTempletQuotaDetailUserscoreDetailEntity::getId, hrTempletQuotaDetailUserscoreDetail.getId());
    	//HrTempletQuotaDetailUserscoreDetailEntity one = hrTempletQuotaDetailUserscoreDetailService.getOne(queryWrapper);
    	HrTempletQuotaDetailUserscoreDetailEntity one = hrTempletQuotaDetailUserscoreDetailService.getById(hrTempletQuotaDetailUserscoreDetail.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("hrTempletQuotaDetailUserscoreDetail/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody HrTempletQuotaDetailUserscoreDetailEntity hrTempletQuotaDetailUserscoreDetail){
        Page page = new Page(hrTempletQuotaDetailUserscoreDetail.getPage(), hrTempletQuotaDetailUserscoreDetail.getLimit());
        LambdaQueryWrapper<HrTempletQuotaDetailUserscoreDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletQuotaDetailUserscoreDetailEntity::getId, hrTempletQuotaDetailUserscoreDetail.getId());
        IPage<HrTempletQuotaDetailUserscoreDetailEntity> iPage = hrTempletQuotaDetailUserscoreDetailService.page(page, queryWrapper);
        log.info("\n this.hrTempletQuotaDetailUserscoreDetailMapper.selectCountUser()="+this.hrTempletQuotaDetailUserscoreDetailMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
