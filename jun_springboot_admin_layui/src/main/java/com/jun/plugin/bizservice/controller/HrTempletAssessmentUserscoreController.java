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

import com.jun.plugin.bizservice.entity.HrTempletAssessmentUserscoreEntity;
import com.jun.plugin.bizservice.mapper.HrTempletAssessmentUserscoreMapper;
import com.jun.plugin.bizservice.service.HrTempletAssessmentUserscoreService;



/**
 * 用户考核记录
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Controller
@RequestMapping("/")
@Slf4j
public class HrTempletAssessmentUserscoreController {

    @Autowired
    private HrTempletAssessmentUserscoreService hrTempletAssessmentUserscoreService;

    @Autowired
    private HrTempletAssessmentUserscoreMapper hrTempletAssessmentUserscoreMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/hrTempletAssessmentUserscore")
    public String hrTempletAssessmentUserscore() {
        return "hrtempletassessmentuserscore/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("hrTempletAssessmentUserscore/add")
    @RequiresPermissions("hrTempletAssessmentUserscore:add")
    @ResponseBody
    public DataResult add(@RequestBody HrTempletAssessmentUserscoreEntity hrTempletAssessmentUserscore){
        hrTempletAssessmentUserscoreService.save(hrTempletAssessmentUserscore);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("hrTempletAssessmentUserscore/delete")
    @RequiresPermissions("hrTempletAssessmentUserscore:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        hrTempletAssessmentUserscoreService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("hrTempletAssessmentUserscore/update")
    @RequiresPermissions("hrTempletAssessmentUserscore:update")
    @ResponseBody
    public DataResult update(@RequestBody HrTempletAssessmentUserscoreEntity hrTempletAssessmentUserscore){
        hrTempletAssessmentUserscoreService.updateById(hrTempletAssessmentUserscore);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("hrTempletAssessmentUserscore/listByPage")
    @RequiresPermissions("hrTempletAssessmentUserscore:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody HrTempletAssessmentUserscoreEntity hrTempletAssessmentUserscore){
        Page page = new Page(hrTempletAssessmentUserscore.getPage(), hrTempletAssessmentUserscore.getLimit());
        LambdaQueryWrapper<HrTempletAssessmentUserscoreEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletAssessmentUserscoreEntity::getId, hrTempletAssessmentUserscore.getId());
        IPage<HrTempletAssessmentUserscoreEntity> iPage = hrTempletAssessmentUserscoreService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("hrTempletAssessmentUserscore/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody HrTempletAssessmentUserscoreEntity hrTempletAssessmentUserscore){
    	LambdaQueryWrapper<HrTempletAssessmentUserscoreEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(HrTempletAssessmentUserscoreEntity::getId, hrTempletAssessmentUserscore.getId());
    	//HrTempletAssessmentUserscoreEntity one = hrTempletAssessmentUserscoreService.getOne(queryWrapper);
    	HrTempletAssessmentUserscoreEntity one = hrTempletAssessmentUserscoreService.getById(hrTempletAssessmentUserscore.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("hrTempletAssessmentUserscore/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody HrTempletAssessmentUserscoreEntity hrTempletAssessmentUserscore){
        Page page = new Page(hrTempletAssessmentUserscore.getPage(), hrTempletAssessmentUserscore.getLimit());
        LambdaQueryWrapper<HrTempletAssessmentUserscoreEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(HrTempletAssessmentUserscoreEntity::getId, hrTempletAssessmentUserscore.getId());
        IPage<HrTempletAssessmentUserscoreEntity> iPage = hrTempletAssessmentUserscoreService.page(page, queryWrapper);
        log.info("\n this.hrTempletAssessmentUserscoreMapper.selectCountUser()="+this.hrTempletAssessmentUserscoreMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
