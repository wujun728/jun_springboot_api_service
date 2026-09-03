package com.zapi.workflow.service.support.handler;

import com.zapi.workflow.service.common.constant.ProcessConstants;
import com.zapi.workflow.service.model.dto.FlowDTO;
import com.zapi.workflow.service.model.dto.NodeDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;
import com.zapi.workflow.service.support.AbstractNodeHandler;
import com.zapi.workflow.service.support.NodeHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 条件分支节点处理器
 * 根据条件表达式评估结果，路由到不同的分支
 */
@Slf4j
@Component
public class ConditionalBranchNodeHandler extends AbstractNodeHandler {
    
    @Override
    public String getNodeType() {
        return ProcessConstants.NodeType.CONDITIONAL_BRANCH;
    }
    
    @Override
    protected void doHandle(NodeHandlerContext context, WfProcessInstance instance,
                            ProcessDefinitionDTO definitionDTO, NodeDTO node) {
        // 获取当前节点的所有流出连线
        List<FlowDTO> outgoingFlows = context.findOutgoingFlows(definitionDTO, node.getId());
        
        // 解析流程变量
        Map<String, Object> variables = context.parseVariables(instance);
        
        // 找到第一个满足条件的连线
        String nextNodeId = null;
        String matchedCondition = null;
        
        for (FlowDTO flow : outgoingFlows) {
            if (flow.getCondition() == null || flow.getCondition().trim().isEmpty()) {
                continue;
            }
            
            // 评估条件表达式
            if (evaluateCondition(flow.getCondition(), variables)) {
                nextNodeId = flow.getTo();
                matchedCondition = flow.getCondition();
                break;
            }
        }
        
        // 如果没有找到满足条件的，使用默认连线
        if (nextNodeId == null && node.getDefaultFlow() != null) {
            nextNodeId = outgoingFlows.stream()
                    .filter(flow -> node.getDefaultFlow().equals(flow.getId()))
                    .map(FlowDTO::getTo)
                    .findFirst()
                    .orElse(null);
            matchedCondition = "默认连线";
        }
        
        // 如果没有默认连线，使用第一条连线
        if (nextNodeId == null && !outgoingFlows.isEmpty()) {
            nextNodeId = outgoingFlows.get(0).getTo();
            matchedCondition = "第一条连线(兜底)";
        }
        
        if (nextNodeId == null) {
            throw new RuntimeException("条件分支节点[" + node.getName() + "]没有找到可用的流出连线");
        }
        
        // 记录条件分支节点的完成
        String comment = matchedCondition != null ? "条件路由: " + matchedCondition : "自动路由";
        context.getHistoryActivityService().recordCompleteActivity(
            instance.getId(),
            node.getId(),
            node.getName(),
            node.getType(),
            instance.getStartUser(),
            comment
        );
        
        // 更新当前节点并处理下一个节点
        context.updateCurrentNode(instance, nextNodeId);
        context.processNextNode(instance, definitionDTO, nextNodeId);
    }
    
    /**
     * 评估条件表达式
     */
    private boolean evaluateCondition(String condition, Map<String, Object> variables) {
        try {
            if (variables == null || variables.isEmpty()) {
                return false;
            }
            
            // 去除 ${} 包装，获取表达式内容
            String expr = condition.replace("${", "").replace("}", "").trim();
            
            // 支持多种运算符
            if (expr.contains("==")) {
                return evaluateEquality(expr, variables, true, "==");
            } else if (expr.contains("!=")) {
                return evaluateEquality(expr, variables, false, "!=");
            } else if (expr.contains(">=")) {
                return evaluateComparison(expr, variables, ">=");
            } else if (expr.contains("<=")) {
                return evaluateComparison(expr, variables, "<=");
            } else if (expr.contains(">")) {
                return evaluateComparison(expr, variables, ">");
            } else if (expr.contains("<")) {
                return evaluateComparison(expr, variables, "<");
            } else if (expr.contains("=")) {
                return evaluateEquality(expr, variables, true, "=");
            }
            
            return false;
        } catch (Exception e) {
            log.error("条件表达式计算失败: {}", condition, e);
            throw new RuntimeException("条件表达式计算失败: " + condition, e);
        }
    }
    
    /**
     * 评估等值比较表达式
     */
    private boolean evaluateEquality(String expr, Map<String, Object> variables, boolean isEqual, String operator) {
        String[] parts = expr.split(operator);
        if (parts.length != 2) {
            return false;
        }
        
        String varName = parts[0].trim();
        String expectedValue = parts[1].trim();
        
        // 去除字符串值的单引号或双引号
        if ((expectedValue.startsWith("'") && expectedValue.endsWith("'")) ||
            (expectedValue.startsWith("\"") && expectedValue.endsWith("\""))) {
            expectedValue = expectedValue.substring(1, expectedValue.length() - 1);
        }
        
        Object actualValue = variables.get(varName);
        if (actualValue == null) {
            return !isEqual;
        }
        
        boolean result = actualValue.toString().equals(expectedValue);
        return isEqual ? result : !result;
    }
    
    /**
     * 评估大小比较表达式
     */
    private boolean evaluateComparison(String expr, Map<String, Object> variables, String operator) {
        String[] parts = expr.split(operator.replace(">", "\\>").replace("<", "\\<"));
        if (parts.length != 2) {
            return false;
        }
        
        String varName = parts[0].trim();
        String valueStr = parts[1].trim();
        
        Object actualValueObj = variables.get(varName);
        if (actualValueObj == null) {
            return false;
        }
        
        try {
            double actualValue = actualValueObj instanceof Number ? 
                ((Number) actualValueObj).doubleValue() : 
                Double.parseDouble(actualValueObj.toString());
            double expectedValue = Double.parseDouble(valueStr);
            
            switch (operator) {
                case ">":
                    return actualValue > expectedValue;
                case "<":
                    return actualValue < expectedValue;
                case ">=":
                    return actualValue >= expectedValue;
                case "<=":
                    return actualValue <= expectedValue;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
