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

import com.jun.plugin.bizservice.entity.PjProjectBorrowEntity;
import com.jun.plugin.bizservice.mapper.PjProjectBorrowMapper;
import com.jun.plugin.bizservice.service.PjProjectBorrowService;



/**
 * 项目借阅
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Controller
@RequestMapping("/")
@Slf4j
public class PjProjectBorrowController {

    @Autowired
    private PjProjectBorrowService pjProjectBorrowService;

    @Autowired
    private PjProjectBorrowMapper pjProjectBorrowMapper;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/pjProjectBorrow")
    public String pjProjectBorrow() {
        return "pjprojectborrow/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("pjProjectBorrow/add")
    @RequiresPermissions("pjProjectBorrow:add")
    @ResponseBody
    public DataResult add(@RequestBody PjProjectBorrowEntity pjProjectBorrow){
        pjProjectBorrowService.save(pjProjectBorrow);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("pjProjectBorrow/delete")
    @RequiresPermissions("pjProjectBorrow:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        pjProjectBorrowService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("pjProjectBorrow/update")
    @RequiresPermissions("pjProjectBorrow:update")
    @ResponseBody
    public DataResult update(@RequestBody PjProjectBorrowEntity pjProjectBorrow){
        pjProjectBorrowService.updateById(pjProjectBorrow);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("pjProjectBorrow/listByPage")
    @RequiresPermissions("pjProjectBorrow:list")
    @ResponseBody
    @DataScope
    public DataResult findListByPage(@RequestBody PjProjectBorrowEntity pjProjectBorrow){
        Page page = new Page(pjProjectBorrow.getPage(), pjProjectBorrow.getLimit());
        LambdaQueryWrapper<PjProjectBorrowEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectBorrowEntity::getId, pjProjectBorrow.getId());
        IPage<PjProjectBorrowEntity> iPage = pjProjectBorrowService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    
    @ApiOperation(value = "查询单条数据")
    @PostMapping("pjProjectBorrow/findOne")
    @ResponseBody
    public DataResult findOne(@RequestBody PjProjectBorrowEntity pjProjectBorrow){
    	LambdaQueryWrapper<PjProjectBorrowEntity> queryWrapper = Wrappers.lambdaQuery();
    	//查询条件示例
    	//queryWrapper.eq(PjProjectBorrowEntity::getId, pjProjectBorrow.getId());
    	//PjProjectBorrowEntity one = pjProjectBorrowService.getOne(queryWrapper);
    	PjProjectBorrowEntity one = pjProjectBorrowService.getById(pjProjectBorrow.getId());
    	return DataResult.success(one);
    }

    @ApiOperation(value = "查询下拉框数据")
    @PostMapping("pjProjectBorrow/listBySelect")
    @ResponseBody
    public DataResult findListBySelect(@RequestBody PjProjectBorrowEntity pjProjectBorrow){
        Page page = new Page(pjProjectBorrow.getPage(), pjProjectBorrow.getLimit());
        LambdaQueryWrapper<PjProjectBorrowEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(PjProjectBorrowEntity::getId, pjProjectBorrow.getId());
        IPage<PjProjectBorrowEntity> iPage = pjProjectBorrowService.page(page, queryWrapper);
        log.info("\n this.pjProjectBorrowMapper.selectCountUser()="+this.pjProjectBorrowMapper.selectCountUser());
        return DataResult.success(iPage);
    }

}
