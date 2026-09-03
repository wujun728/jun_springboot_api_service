/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package org.dromara.surpass.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.app.AppProxyRules;
import org.dromara.surpass.entity.app.dto.AppProxyRulesChangeDto;
import org.dromara.surpass.entity.app.dto.AppProxyRulesPageDto;
import org.dromara.surpass.persistence.mapper.AppProxyRulesMapper;
import org.dromara.surpass.persistence.service.AppProxyRulesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppProxyRulesServiceImpl extends JpaServiceImpl<AppProxyRulesMapper, AppProxyRules> implements AppProxyRulesService {

    @Override
    public Message<String> create(AppProxyRulesChangeDto dto) {
        AppProxyRules appProxyRules = BeanUtil.copyProperties(dto, AppProxyRules.class);
        boolean result = super.insert(appProxyRules);
        return result ? Message.ok("创建成功") : Message.failed("创建失败");
    }

    @Override
    public Message<String> updateProxyRules(AppProxyRulesChangeDto dto) {
        AppProxyRules appProxyRules = BeanUtil.copyProperties(dto, AppProxyRules.class);
        boolean result = super.update(appProxyRules);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    @Transactional
    public Message<String> deleteProxyRules(List<String> ids) {
        boolean result = super.softDelete(ids);
        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }

    @Override
    public Message<JpaPageResults<AppProxyRules>> pageList(AppProxyRulesPageDto dto) {
        dto.build();
        JpaPageResults<AppProxyRules> jpaPageResults = (JpaPageResults<AppProxyRules>) this.buildPageResults(dto, getMapper().pageList(dto));
        return Message.ok(jpaPageResults);
    }

    @Override
    public Message<List<AppProxyRules>> getAll(AppProxyRules dto) {
        List<AppProxyRules> all = getMapper().getAll(dto);
        return Message.ok(all);
    }
}