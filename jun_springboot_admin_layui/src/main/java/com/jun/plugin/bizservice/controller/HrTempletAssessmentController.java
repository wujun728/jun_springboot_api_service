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

import com.jun.plugin.bizservice.entity.HrTempletAssessmentEntity;
import com.jun.plugin.bizservice.mapper.HrTempletAssessmentMapper;
import com.jun.plugin.bizservice.service.HrTempletAssessmentService;



/**
 * 考核模板
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class HrTempletAssessmentController {

    @Autowired
    private HrTempletAssessmentService hrTempletAssessmentService;

    @Autowired
    private HrTempletAssessmentMapper hrTempletAssessmentMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/hrTempletAssessment")
    public String hrTempletAssessment() {
        return "hrtempletassessment/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("hrTempletAssessment/add")
    @RequiresPermissions("hrTempletAssessment:add")
    @ResponseBody
    public DataResult add(@RequestBody HrTempletAssessmentEntity hrTempletAssessment){
        hrTempletAssessmentService.save(hrTempletAssessment);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("hrTempletAssessment/delete")
    @RequiresPermissions("hrTempletAssessment:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        hrTempletAssessmentService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("hrTempletAssessment/update")
    @RequiresPermissions("hrTempletAssessment:update")
    @ResponseBody
    public DataResult update(@RequestBody HrTempletAssessmentEntity hrTempletAssessment){
        hrTempletAssessmentService.updateById(hrTempletAssessment);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("hrTempletAssessment/listByPage")
    @RequiresPermissions("hrTempletAssessment:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody HrTempletAssessmentEntity hrTempletAssessment){
        Page page = new Page(hrTempletAssessment.getPage(), hrTempletAssessment.getLimit());
        LambdaQueryWrapper<HrTempletAssessmentEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletAssessmentEntity::getId, hrTempletAssessment.getId());
        IPage<HrTempletAssessmentEntity> iPage = hrTempletAssessmentService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("hrTempletAssessment/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody HrTempletAssessmentEntity hrTempletAssessment){
    	LambdaQueryWrapper<HrTempletAssessmentEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(HrTempletAssessmentEntity::getId, hrTempletAssessment.getId());
    	//HrTempletAssessmentEntity one = hrTempletAssessmentService.getOne(queryWrapper);
    	HrTempletAssessmentEntity one = hrTempletAssessmentService.getById(hrTempletAssessment.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("hrTempletAssessment/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody HrTempletAssessmentEntity hrTempletAssessment){
        Page page = new Page(hrTempletAssessment.getPage(), hrTempletAssessment.getLimit());
        LambdaQueryWrapper<HrTempletAssessmentEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletAssessmentEntity::getId, hrTempletAssessment.getId());
        IPage<HrTempletAssessmentEntity> iPage = hrTempletAssessmentService.page(page, queryWrapper);
        log.info("\n this.hrTempletAssessmentMapper.selectCountUser()="+this.hrTempletAssessmentMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
