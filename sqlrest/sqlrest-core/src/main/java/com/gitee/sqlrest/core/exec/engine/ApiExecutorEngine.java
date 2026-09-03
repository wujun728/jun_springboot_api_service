package com.gitee.sqlrest.core.exec.engine;

import com.gitee.sqlrest.common.enums.NamingStrategyEnum;
import com.gitee.sqlrest.persistence.entity.ApiContextEntity;
import java.util.List;
import java.util.Map;

public interface ApiExecutorEngine {

  Object execute(List<ApiContextEntity> scripts, Map<String, Object> params, NamingStrategyEnum strategy);
}
