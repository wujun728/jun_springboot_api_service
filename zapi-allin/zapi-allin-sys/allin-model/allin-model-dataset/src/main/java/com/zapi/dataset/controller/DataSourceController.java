package com.zapi.dataset.controller;
import com.zapi.dataset.model.entity.DataSourceEntity;
import com.zapi.dataset.service.DataSourceService;
import com.zapi.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dataset/dataSource") // 设置统一的基础路径
public class DataSourceController {

    @Resource
    private DataSourceService dataSourceService;

    // 查询所有数据源
    @GetMapping("/getAll")
    public Result<List<DataSourceEntity>> getAll() {
        return Result.success(dataSourceService.getAllDataSources());
    }

    // 根据ID查询单个数据源
    @GetMapping("/getInfoById/{id}")
    public Result<DataSourceEntity> getById(@PathVariable Integer id) {
        return Result.success(dataSourceService.getDataSourceById(id));
    }

    // 新建数据源
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody DataSourceEntity dataSource) {
        return Result.success(dataSourceService.saveDataSource(dataSource));
    }

    // 更新数据源
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody DataSourceEntity dataSource) {
        return Result.success(dataSourceService.updateDataSource(dataSource));
    }

    // 删除数据源
    @DeleteMapping("delete/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return Result.success(dataSourceService.deleteDataSource(id));
    }
}