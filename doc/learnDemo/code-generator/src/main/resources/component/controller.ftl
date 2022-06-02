package ${basePackage}.controller;

import ${basePackage}.api.${modelNameUpperCamel}Api;
import ${basePackage}.domains.po.${modelNameUpperCamel};
import ${basePackage}.domains.condition.${modelNameUpperCamel}Condition;
import ${basePackage}.web.page.SimplePageInfo;
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Title
* Author ${author}
* DateTime  ${date}.
* Version V1.0.0
*/
@RestController
@RequestMapping("${modelNameLowerCamel}")
public class ${modelNameUpperCamel}Controller implements ${modelNameUpperCamel}Api {

    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping("/add")
    public void add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
    }

    @PostMapping("/delete")
    public int delete(@RequestParam Long id) {
        return  ${modelNameLowerCamel}Service.deleteById(id);
    }

    @PostMapping("/update")
    public int update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
    }

    @PostMapping("/detail")
    public ${modelNameUpperCamel} detail(@RequestParam Long id) {
        return  ${modelNameLowerCamel}Service.findById(id);
    }

    @PostMapping("/list")
    public SimplePageInfo<List<${modelNameUpperCamel}>> list(@RequestBody ${modelNameUpperCamel}Condition ${modelNameLowerCamel}Condition) {
        PageHelper.startPage(${modelNameLowerCamel}Condition.getPageNum(), ${modelNameLowerCamel}Condition.getPageSize());
        List<${modelNameUpperCamel}> result = ${modelNameLowerCamel}Service.findAll();
        return new SimplePageInfo(result);
    }
}
