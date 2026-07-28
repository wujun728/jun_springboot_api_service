package com.ruoyi.api.mcp.config;

import com.ruoyi.api.mcp.annotation.EnableMcpServer;
import com.ruoyi.api.mcp.server.McpServer;
import com.ruoyi.api.mcp.transport.WebMvcSseServerTransport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(annotation = EnableMcpServer.class)
public class McpAutoConfiguration {

    @Bean
    public McpServer mcpServer() {
        return new McpServer("RuoYi-MCP", "1.0");
    }

    @Bean
    public WebMvcSseServerTransport mcpTransport(McpServer mcpServer) {
        return new WebMvcSseServerTransport(mcpServer);
    }
}