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

import com.jun.plugin.bizservice.entity.HrTempletQuotaDetailEntity;
import com.jun.plugin.bizservice.mapper.HrTempletQuotaDetailMapper;
import com.jun.plugin.bizservice.service.HrTempletQuotaDetailService;



/**
 * 考核模板明细
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class HrTempletQuotaDetailController {

    @Autowired
    private HrTempletQuotaDetailService hrTempletQuotaDetailService;

    @Autowired
    private HrTempletQuotaDetailMapper hrTempletQuotaDetailMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/hrTempletQuotaDetail")
    public String hrTempletQuotaDetail() {
        return "hrtempletquotadetail/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("hrTempletQuotaDetail/add")
    @RequiresPermissions("hrTempletQuotaDetail:add")
    @ResponseBody
    public DataResult add(@RequestBody HrTempletQuotaDetailEntity hrTempletQuotaDetail){
        hrTempletQuotaDetailService.save(hrTempletQuotaDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("hrTempletQuotaDetail/delete")
    @RequiresPermissions("hrTempletQuotaDetail:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        hrTempletQuotaDetailService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("hrTempletQuotaDetail/update")
    @RequiresPermissions("hrTempletQuotaDetail:update")
    @ResponseBody
    public DataResult update(@RequestBody HrTempletQuotaDetailEntity hrTempletQuotaDetail){
        hrTempletQuotaDetailService.updateById(hrTempletQuotaDetail);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("hrTempletQuotaDetail/listByPage")
    @RequiresPermissions("hrTempletQuotaDetail:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody HrTempletQuotaDetailEntity hrTempletQuotaDetail){
        Page page = new Page(hrTempletQuotaDetail.getPage(), hrTempletQuotaDetail.getLimit());
        LambdaQueryWrapper<HrTempletQuotaDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletQuotaDetailEntity::getId, hrTempletQuotaDetail.getId());
        IPage<HrTempletQuotaDetailEntity> iPage = hrTempletQuotaDetailService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("hrTempletQuotaDetail/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody HrTempletQuotaDetailEntity hrTempletQuotaDetail){
    	LambdaQueryWrapper<HrTempletQuotaDetailEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(HrTempletQuotaDetailEntity::getId, hrTempletQuotaDetail.getId());
    	//HrTempletQuotaDetailEntity one = hrTempletQuotaDetailService.getOne(queryWrapper);
    	HrTempletQuotaDetailEntity one = hrTempletQuotaDetailService.getById(hrTempletQuotaDetail.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("hrTempletQuotaDetail/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody HrTempletQuotaDetailEntity hrTempletQuotaDetail){
        Page page = new Page(hrTempletQuotaDetail.getPage(), hrTempletQuotaDetail.getLimit());
        LambdaQueryWrapper<HrTempletQuotaDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletQuotaDetailEntity::getId, hrTempletQuotaDetail.getId());
        IPage<HrTempletQuotaDetailEntity> iPage = hrTempletQuotaDetailService.page(page, queryWrapper);
        log.info("\n this.hrTempletQuotaDetailMapper.selectCountUser()="+this.hrTempletQuotaDetailMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
