package org.dromara.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.entity.app.AppProxyRules;
import org.dromara.surpass.entity.app.dto.AppProxyRulesPageDto;

import java.util.List;

@Mapper
public interface AppProxyRulesMapper extends IJpaMapper<AppProxyRules> {
    List<AppProxyRules> pageList(AppProxyRulesPageDto dto);
    List<AppProxyRules> getAll(AppProxyRules dto);
}
