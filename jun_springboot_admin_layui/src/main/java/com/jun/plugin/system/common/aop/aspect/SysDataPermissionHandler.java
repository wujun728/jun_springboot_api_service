package com.jun.plugin.system.common.aop.aspect;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.jun.plugin.module.ext.entity.SysDataPower;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;

@Slf4j
public class SysDataPermissionHandler implements DataPermissionHandler {
	
	@Autowired
	SysDataPermissionTools sysDataPermissionTools;

    /**
     * @param where             原SQL Where 条件表达式
     * @param mappedStatementId Mapper接口方法ID
     * @return
     */
    @SneakyThrows
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        List<String> split = StrUtil.split(mappedStatementId, '.');
        int index = split.size();
        String method = split.get(index - 1);
        String mapper = split.get(index - 2);
        UserInfoAndPowers userInfoAndPowers = sysDataPermissionTools.getCurrentUserInfo();
        if (userInfoAndPowers == null) {
            return where;
        }
        if (userInfoAndPowers.isSuperAdmin()) {
            return where;
        }
        List<SysDataPower> userDataPowers = userInfoAndPowers.getUserDataPowers();
        if (CollUtil.isEmpty(userDataPowers)) {
            return where;
        }
        Optional<SysDataPower> dataPower = userDataPowers.stream()
                .filter(sysDataPower -> StrUtil.equalsIgnoreCase(mapper, sysDataPower.getMapper())
                        && StrUtil.equalsIgnoreCase(method, sysDataPower.getMethod()))
                .findFirst();
        if (!dataPower.isPresent()) {
            return where;
        }
        try {
            // 1. 获取权限过滤相关信息
            log.info("进行权限过滤,dataPowerFilterType:{} , where: {},mappedStatementId: {}", dataPower.get().getFilterType(), where, mappedStatementId);
            Expression expression = new HexValue(" 1 = 1 ");
            if (where == null) {
                where = expression;
            }
            switch (dataPower.get().getFilterType()) {
                // 查看全部
                case ALL:
                    return where;
                // 查看本人所在组织机构以及下属机构
                case DEPT_SETS:
                    // 创建IN 表达式
                    // 创建IN范围的元素集合
                    Set<Long> deptIds = userInfoAndPowers.getDeptIds();
                    // 把集合转变为JSQLParser需要的元素列表
                    ItemsList itemsList = new ExpressionList(deptIds.stream().map(LongValue::new).collect(Collectors.toList()));
                    InExpression inExpression = new InExpression(new Column("create_dept_id"), itemsList);
                    AndExpression andExpression = new AndExpression(where, inExpression);
                    log.info(" where {}", andExpression);
                    return andExpression;
                // 查看当前部门的数据
                case DEPT:
                    //  = 表达式
                    // dept_id = deptId
                    EqualsTo equalsTo = new EqualsTo();
                    equalsTo.setLeftExpression(new Column("create_dept_id"));
                    equalsTo.setRightExpression(new LongValue(userInfoAndPowers.getUser().getId()));
                    // 创建 AND 表达式 拼接Where 和 = 表达式
                    // WHERE xxx AND dept_id = 3
                    AndExpression deptAndExpression = new AndExpression(where, equalsTo);
                    log.info(" where {}", deptAndExpression);
                    return deptAndExpression;
                // 查看自己的数据
                case SELF:
                    // create_by = userId
                    EqualsTo selfEqualsTo = new EqualsTo();
                    selfEqualsTo.setLeftExpression(new Column("create_by"));
                    selfEqualsTo.setRightExpression(new LongValue(userInfoAndPowers.getUser().getId()));
                    AndExpression selfAndExpression = new AndExpression(where, selfEqualsTo);
                    log.info(" where {}", selfAndExpression);
                    return selfAndExpression;
                case DIY:
                    return new AndExpression(where, new StringValue(userInfoAndPowers.getSql()));
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("LakerDataPermissionHandler.err", e);
        } finally {
        }
        return where;
    }
}
