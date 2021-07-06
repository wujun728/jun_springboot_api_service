package com.jun.plugin.api.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jun.plugin.api.core.Result;
import com.jun.plugin.api.core.ResultGenerator;
import com.jun.plugin.api.model.Company;
import com.jun.plugin.api.service.CompanyService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import javax.annotation.Resource;
import java.util.List;

/**
* localhost:8080/company/list
*/
@RestController
@RequestMapping(value = "/company")
public class CompanyController {
    @Resource
    private CompanyService companyService;

    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(name = "company", value = "", required = true, dataType = "String")})
    @PostMapping(value = "/add", produces = "application/json", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Result add(@RequestParam(value = "company") Company company) {
        companyService.save(company);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "", required = true, dataType = "int")})
    @DeleteMapping(value = "/delete", produces = "application/json")
    public Result delete(@RequestParam(value = "id") int id) {
        companyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "company", value = "", required = true, dataType = "String")})
    @PutMapping(value = "/update", produces = "application/json", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Result update(@RequestParam(value = "company") Company company) {
        companyService.update(company);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "查询详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "", required = true, dataType = "int")})
    @GetMapping(value = "/detail",produces = "application/json")
    public Result detail(@RequestParam(value = "id") int id) {
        Company company = companyService.findById(id);
        return ResultGenerator.genSuccessResult(company);
    }

    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
                        @ApiImplicitParam(name = "size", value = "页面大小", required = false, dataType = "int")})
    @GetMapping(value = "/list",produces = "application/json")
    public Result list( @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<Company> list = companyService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}