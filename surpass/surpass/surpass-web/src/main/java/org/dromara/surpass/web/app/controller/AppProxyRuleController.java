package org.dromara.surpass.web.app.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.app.AppProxyRules;
import org.dromara.surpass.entity.app.dto.AppProxyRulesChangeDto;
import org.dromara.surpass.entity.app.dto.AppProxyRulesPageDto;
import org.dromara.surpass.persistence.service.AppProxyRulesService;
import org.dromara.surpass.validate.AddGroup;
import org.dromara.surpass.validate.EditGroup;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app-proxy")
@RequiredArgsConstructor
public class AppProxyRuleController {

    private final AppProxyRulesService appProxyRulesService;

    @PostMapping("/add")
    public Message<String> add(@Validated(value = AddGroup.class) @RequestBody AppProxyRulesChangeDto dto) {
        return appProxyRulesService.create(dto);
    }

    @GetMapping("/page")
    public Message<JpaPageResults<AppProxyRules>> page(@ParameterObject AppProxyRulesPageDto dto) {
        return appProxyRulesService.pageList(dto);
    }

    @GetMapping("/all")
    public Message<List<AppProxyRules>> all(@ParameterObject AppProxyRules dto) {
        return appProxyRulesService.getAll(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody AppProxyRulesChangeDto dto) {
        return appProxyRulesService.updateProxyRules(dto);
    }

    @DeleteMapping(value = {"/delete"})
    public Message<String> delete(@RequestParam("ids") List<String> ids) {
        return appProxyRulesService.deleteProxyRules(ids);
    }

    @GetMapping("/get/{id}")
    public Message<AppProxyRules> get(@PathVariable String id) {
        AppProxyRules appProxyRules = appProxyRulesService.get(id);
        return Message.ok(appProxyRules);
    }
}
