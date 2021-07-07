package com.jun.plugin.api.controller;
import com.jun.plugin.api.core.Result;
import com.jun.plugin.api.core.ResultGenerator;
import com.jun.plugin.api.model.City;
import com.jun.plugin.api.service.CityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by Wujun on 2021/07/07.
*/
@RestController
@RequestMapping(value = "/api/public/city")
public class CityController {
    @Resource
    private CityService cityService;

    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(name = "city", value = "", required = true, dataType = "String")})
    @PostMapping(value = "/add", produces = "application/json", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Result add(@RequestParam(value = "city") City city) {
        cityService.save(city);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "", required = true, dataType = "int")})
    @DeleteMapping(value = "/delete", produces = "application/json")
    public Result delete(@RequestParam(value = "id") int id) {
        cityService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "city", value = "", required = true, dataType = "String")})
    @PutMapping(value = "/update", produces = "application/json", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Result update(@RequestParam(value = "city") City city) {
        cityService.update(city);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "查询详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "", required = true, dataType = "int")})
    @GetMapping(value = "/detail",produces = "application/json")
    public Result detail(@RequestParam(value = "id") int id) {
        City city = cityService.findById(id);
        return ResultGenerator.genSuccessResult(city);
    }

    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
                        @ApiImplicitParam(name = "size", value = "页面大小", required = false, dataType = "int")})
    @GetMapping(value = "/list",produces = "application/json")
    public Result list( @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<City> list = cityService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
