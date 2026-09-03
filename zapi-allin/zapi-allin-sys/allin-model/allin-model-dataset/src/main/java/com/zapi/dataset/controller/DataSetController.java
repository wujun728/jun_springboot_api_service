package com.zapi.dataset.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.dataset.model.entity.DataSet;
import com.zapi.dataset.model.vo.DataSetVO;
import com.zapi.dataset.model.vo.FieldInfoVO;
import com.zapi.dataset.service.DataSetService;
import com.zapi.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dataset/dataConnection")
public class DataSetController {

    @Autowired
    private DataSetService datasetService;

    @GetMapping("/list")
    public Result<Page<DataSet>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String name) {
        return Result.success(datasetService.pageList(pageNum, pageSize, name));
    }

    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody DataSet dataset) {
        dataset.setCreateTime(new Date());
        dataset.setUpdateTime(new Date());
        return Result.success(datasetService.save(dataset));
    }

    /***
     * 根据数据集id查询出数据集相对应的相关信息
     * @param dataset
     * @return
     */
    @PostMapping("/getDataSetInfoById")
    public Result<DataSetVO> getDataSetInfoById(@RequestBody DataSet dataset) throws Exception{
        DataSetVO info = datasetService.getDataSetInfoById(dataset);
        return Result.success(info);
    }


    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody DataSet dataset) {
        dataset.setUpdateTime(new Date());
        return Result.success(datasetService.updateById(dataset));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(datasetService.removeById(id));
    }

    @GetMapping("/fields")
    public Result<List<FieldInfoVO>> getFields(@RequestParam Long datasetId) throws Exception {
        DataSet dataset=new DataSet();
        dataset.setId(datasetId);
            // 模拟返回字段列表数据
        DataSetVO info = datasetService.getDataSetInfoById(dataset);


        // 返回字段列表
        return Result.success(info.getFields());

    }
}
