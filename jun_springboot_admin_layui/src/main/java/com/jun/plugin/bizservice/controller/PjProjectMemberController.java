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

import com.jun.plugin.bizservice.entity.PjProjectMemberEntity;
import com.jun.plugin.bizservice.mapper.PjProjectMemberMapper;
import com.jun.plugin.bizservice.service.PjProjectMemberService;



/**
 * 项目成员与结算
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectMemberController {

    @Autowired
    private PjProjectMemberService pjProjectMemberService;

    @Autowired
    private PjProjectMemberMapper pjProjectMemberMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectMember")
    public String pjProjectMember() {
        return "pjprojectmember/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectMember/add")
    @RequiresPermissions("pjProjectMember:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectMemberEntity pjProjectMember){
        pjProjectMemberService.save(pjProjectMember);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectMember/delete")
    @RequiresPermissions("pjProjectMember:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectMemberService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectMember/update")
    @RequiresPermissions("pjProjectMember:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectMemberEntity pjProjectMember){
        pjProjectMemberService.updateById(pjProjectMember);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectMember/listByPage")
    @RequiresPermissions("pjProjectMember:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectMemberEntity pjProjectMember){
        Page page = new Page(pjProjectMember.getPage(), pjProjectMember.getLimit());
        LambdaQueryWrapper<PjProjectMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectMemberEntity::getId, pjProjectMember.getId());
        IPage<PjProjectMemberEntity> iPage = pjProjectMemberService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectMember/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectMemberEntity pjProjectMember){
    	LambdaQueryWrapper<PjProjectMemberEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectMemberEntity::getId, pjProjectMember.getId());
    	//PjProjectMemberEntity one = pjProjectMemberService.getOne(queryWrapper);
    	PjProjectMemberEntity one = pjProjectMemberService.getById(pjProjectMember.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectMember/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectMemberEntity pjProjectMember){
        Page page = new Page(pjProjectMember.getPage(), pjProjectMember.getLimit());
        LambdaQueryWrapper<PjProjectMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectMemberEntity::getId, pjProjectMember.getId());
        IPage<PjProjectMemberEntity> iPage = pjProjectMemberService.page(page, queryWrapper);
        log.info("\n this.pjProjectMemberMapper.selectCountUser()="+this.pjProjectMemberMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
