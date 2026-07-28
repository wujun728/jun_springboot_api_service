package com.ruoyi.api.mcp.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.api.mcp.core.McpSchema.Tool;
import com.ruoyi.api.mcp.server.McpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebMvcSseServerTransport {

    private static final Logger log = LoggerFactory.getLogger(WebMvcSseServerTransport.class);

    private final McpServer server;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, javax.servlet.AsyncContext> sessions = new ConcurrentHashMap<>();

    public WebMvcSseServerTransport(McpServer server) {
        this.server = server;
    }

    public void handleSse(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        String sessionId = UUID.randomUUID().toString();
        javax.servlet.AsyncContext asyncCtx = request.startAsync();
        asyncCtx.setTimeout(0);
        sessions.put(sessionId, asyncCtx);

        try {
            sendSseEvent(asyncCtx, "endpoint", "/mcp/message?sessionId=" + sessionId);
        } catch (Exception e) {
            log.error("Failed to send SSE endpoint", e);
        }
    }

    public void handleMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> message = mapper.readValue(request.getInputStream(), Map.class);

        String method = (String) message.get("method");
        Object id = message.get("id");
        Map<String, Object> result = new HashMap<>();

        try {
            switch (method) {
                case "initialize":
                    Map<String, Object> caps = new HashMap<>();
                    caps.put("tools", true);
                    result.put("protocolVersion", "2024-11-05");
                    result.put("capabilities", caps);
                    result.put("serverInfo", server.getName() + "/" + server.getVersion());
                    break;
                case "tools/list":
                    List<Map<String, Object>> tools = new ArrayList<>();
                    for (Tool tool : server.listTools()) {
                        Map<String, Object> t = new HashMap<>();
                        t.put("name", tool.getName());
                        t.put("description", tool.getDescription());
                        t.put("inputSchema", tool.getInputSchema());
                        tools.add(t);
                    }
                    Map<String, Object> toolsResult = new HashMap<>();
                    toolsResult.put("tools", tools);
                    result.put("tools", tools);
                    break;
                case "tools/call":
                    Map<String, Object> params = (Map<String, Object>) message.get("params");
                    String toolName = (String) params.get("name");
                    Map<String, Object> args = (Map<String, Object>) params.get("arguments");
                    Object callResult = server.callTool(toolName, args);
                    Map<String, Object> content = new HashMap<>();
                    content.put("content", callResult);
                    content.put("isError", false);
                    result.put("content", java.util.Collections.singletonList(content));
                    break;
                case "notifications/initialized":
                    result.put("_notification", true);
                    break;
                default:
                    result.put("error", "Unknown method: " + method);
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("jsonrpc", "2.0");
        responseJson.put("id", id);
        responseJson.put("result", result);

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.write(objectMapper.writeValueAsString(responseJson));
            out.flush();
        }
    }

    private void sendSseEvent(javax.servlet.AsyncContext asyncCtx, String event, String data) throws IOException {
        try (PrintWriter writer = asyncCtx.getResponse().getWriter()) {
            if (event != null) {
                writer.write("event: " + event + "\n");
            }
            writer.write("data: " + data + "\n\n");
            writer.flush();
        }
    }
}