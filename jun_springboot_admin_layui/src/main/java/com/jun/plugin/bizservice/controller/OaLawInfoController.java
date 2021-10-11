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

import com.jun.plugin.bizservice.entity.OaLawInfoEntity;
import com.jun.plugin.bizservice.service.OaLawInfoService;



/**
 * 政策法规
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-03 20:39:01
 */
@Controller
@RequestMapping("/")
public class OaLawInfoController {
    @Autowired
    private OaLawInfoService oaLawInfoService;


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
    public DataResult findListByPage(@RequestBody OaLawInfoEntity oaLawInfo){
        Page page = new Page(oaLawInfo.getPage(), oaLawInfo.getLimit());
        LambdaQueryWrapper<OaLawInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(OaLawInfoEntity::getId, oaLawInfo.getId());
        IPage<OaLawInfoEntity> iPage = oaLawInfoService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
