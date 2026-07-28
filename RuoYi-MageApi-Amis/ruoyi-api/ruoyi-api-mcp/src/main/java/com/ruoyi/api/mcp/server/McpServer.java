package com.ruoyi.api.mcp.server;

import com.ruoyi.api.mcp.core.McpSchema;
import com.ruoyi.api.mcp.core.McpSchema.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class McpServer {

    private static final Logger log = LoggerFactory.getLogger(McpServer.class);

    private final String name;
    private final String version;
    private final Map<String, ToolRegistration> tools = new ConcurrentHashMap<>();

    public McpServer(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public void addTool(String name, String description, Object inputSchema,
                        Function<Map<String, Object>, Object> handler) {
        tools.put(name, new ToolRegistration(
                new Tool(name, description, inputSchema), handler));
        log.info("MCP tool registered: {}", name);
    }

    public List<Tool> listTools() {
        List<Tool> result = new ArrayList<>();
        for (ToolRegistration reg : tools.values()) {
            result.add(reg.tool);
        }
        return result;
    }

    public Object callTool(String name, Map<String, Object> arguments) {
        ToolRegistration reg = tools.get(name);
        if (reg == null) {
            throw new IllegalArgumentException("Tool not found: " + name);
        }
        return reg.handler.apply(arguments);
    }

    public String getName() { return name; }
    public String getVersion() { return version; }

    private static class ToolRegistration {
        final Tool tool;
        final Function<Map<String, Object>, Object> handler;

        ToolRegistration(Tool tool, Function<Map<String, Object>, Object> handler) {
            this.tool = tool;
            this.handler = handler;
        }
    }
}