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

import com.jun.plugin.bizservice.entity.OaPomsWorkmarksTimesEntity;
import com.jun.plugin.bizservice.service.OaPomsWorkmarksTimesService;



/**
 * 考勤记录
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 16:19:28
 */
@Controller
@RequestMapping("/")
public class OaPomsWorkmarksTimesController {
    @Autowired
    private OaPomsWorkmarksTimesService oaPomsWorkmarksTimesService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/oaPomsWorkmarksTimes")
    public String oaPomsWorkmarksTimes() {
        return "oapomsworkmarkstimes/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("oaPomsWorkmarksTimes/add")
    @RequiresPermissions("oaPomsWorkmarksTimes:add")
    @ResponseBody
    public DataResult add(@RequestBody OaPomsWorkmarksTimesEntity oaPomsWorkmarksTimes){
        oaPomsWorkmarksTimesService.save(oaPomsWorkmarksTimes);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("oaPomsWorkmarksTimes/delete")
    @RequiresPermissions("oaPomsWorkmarksTimes:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        oaPomsWorkmarksTimesService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("oaPomsWorkmarksTimes/update")
    @RequiresPermissions("oaPomsWorkmarksTimes:update")
    @ResponseBody
    public DataResult update(@RequestBody OaPomsWorkmarksTimesEntity oaPomsWorkmarksTimes){
        oaPomsWorkmarksTimesService.updateById(oaPomsWorkmarksTimes);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("oaPomsWorkmarksTimes/listByPage")
    @RequiresPermissions("oaPomsWorkmarksTimes:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody OaPomsWorkmarksTimesEntity oaPomsWorkmarksTimes){
        Page page = new Page(oaPomsWorkmarksTimes.getPage(), oaPomsWorkmarksTimes.getLimit());
        LambdaQueryWrapper<OaPomsWorkmarksTimesEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaPomsWorkmarksTimesEntity::getId, oaPomsWorkmarksTimes.getId());
        IPage<OaPomsWorkmarksTimesEntity> iPage = oaPomsWorkmarksTimesService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
