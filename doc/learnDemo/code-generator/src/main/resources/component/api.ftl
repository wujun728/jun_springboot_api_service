package ${basePackage}.api;

import ${basePackage}.domains.po.${modelNameUpperCamel};
import ${basePackage}.domains.condition.${modelNameUpperCamel}Condition;
import ${basePackage}.web.page.SimplePageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* Title
* Author ${author}
* DateTime  ${date}.
* Version V1.0.0
*/
@FeignClient(name = "${projectName}",path = "/${modelNameLowerCamel}")
public interface ${modelNameUpperCamel}Api {

    @PostMapping("/add")
    void add(${modelNameUpperCamel} ${modelNameLowerCamel});

    @PostMapping("/delete")
    int delete(@RequestParam("id") Long id);

    @PostMapping("/update")
    int update(${modelNameUpperCamel} ${modelNameLowerCamel});

    @PostMapping("/detail")
    ${modelNameUpperCamel} detail(@RequestParam("id") Long id);

    @PostMapping("/list")
    SimplePageInfo<List<${modelNameUpperCamel}>> list(${modelNameUpperCamel}Condition ${modelNameLowerCamel}Condition);
}
