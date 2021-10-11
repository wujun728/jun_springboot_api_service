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
import java.util.List;
import com.jun.plugin.system.common.utils.DataResult;

import com.jun.plugin.bizservice.entity.PjProjectMemberEntity;
import com.jun.plugin.bizservice.service.PjProjectMemberService;



/**
 * 项目成员与结算
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 13:33:17
 */
@Controller
@RequestMapping("/")
public class PjProjectMemberController {
    @Autowired
    private PjProjectMemberService pjProjectMemberService;


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
    public DataResult findListByPage(@RequestBody PjProjectMemberEntity pjProjectMember){
        Page page = new Page(pjProjectMember.getPage(), pjProjectMember.getLimit());
        LambdaQueryWrapper<PjProjectMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectMemberEntity::getId, pjProjectMember.getId());
        IPage<PjProjectMemberEntity> iPage = pjProjectMemberService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
