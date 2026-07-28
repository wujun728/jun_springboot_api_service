package com.ruoyi.api.mcp.core;

public class McpSchema {
    public static final String JSONRPC_VERSION = "2.0";
    public static final String PROTOCOL_VERSION = "2024-11-05";

    public static class JsonRpcMessage {
        private String jsonrpc = JSONRPC_VERSION;
        private String method;
        private Object params;

        public JsonRpcMessage() {}
        public JsonRpcMessage(String method, Object params) {
            this.method = method;
            this.params = params;
        }

        public String getJsonrpc() { return jsonrpc; }
        public void setJsonrpc(String jsonrpc) { this.jsonrpc = jsonrpc; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public Object getParams() { return params; }
        public void setParams(Object params) { this.params = params; }
    }

    public static class JsonRpcResponse {
        private String jsonrpc = JSONRPC_VERSION;
        private Object result;
        private Object error;
        private String id;

        public String getJsonrpc() { return jsonrpc; }
        public void setJsonrpc(String jsonrpc) { this.jsonrpc = jsonrpc; }
        public Object getResult() { return result; }
        public void setResult(Object result) { this.result = result; }
        public Object getError() { return error; }
        public void setError(Object error) { this.error = error; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public static class Tool {
        private String name;
        private String description;
        private Object inputSchema;

        public Tool() {}
        public Tool(String name, String description, Object inputSchema) {
            this.name = name; this.description = description; this.inputSchema = inputSchema;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Object getInputSchema() { return inputSchema; }
        public void setInputSchema(Object inputSchema) { this.inputSchema = inputSchema; }
    }

    public static class CallToolRequest {
        private String name;
        private Object arguments;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Object getArguments() { return arguments; }
        public void setArguments(Object arguments) { this.arguments = arguments; }
    }

    public static class CallToolResult {
        private Object content;
        private boolean isError;

        public CallToolResult() {}
        public CallToolResult(Object content) { this.content = content; }

        public Object getContent() { return content; }
        public void setContent(Object content) { this.content = content; }
        public boolean isError() { return isError; }
        public void setError(boolean error) { isError = error; }
    }

    public static class ListToolsResult {
        private java.util.List<Tool> tools;

        public ListToolsResult() {}
        public ListToolsResult(java.util.List<Tool> tools) { this.tools = tools; }

        public java.util.List<Tool> getTools() { return tools; }
        public void setTools(java.util.List<Tool> tools) { this.tools = tools; }
    }

    public static class InitializeRequest {
        private String protocolVersion;
        private Object capabilities;
        private String clientInfo;

        public String getProtocolVersion() { return protocolVersion; }
        public void setProtocolVersion(String protocolVersion) { this.protocolVersion = protocolVersion; }
        public Object getCapabilities() { return capabilities; }
        public void setCapabilities(Object capabilities) { this.capabilities = capabilities; }
        public String getClientInfo() { return clientInfo; }
        public void setClientInfo(String clientInfo) { this.clientInfo = clientInfo; }
    }

    public static class InitializeResult {
        private String protocolVersion = PROTOCOL_VERSION;
        private Object capabilities;
        private String serverInfo;

        public InitializeResult() {}
        public InitializeResult(Object capabilities, String serverInfo) {
            this.capabilities = capabilities;
            this.serverInfo = serverInfo;
        }

        public String getProtocolVersion() { return protocolVersion; }
        public Object getCapabilities() { return capabilities; }
        public void setCapabilities(Object capabilities) { this.capabilities = capabilities; }
        public String getServerInfo() { return serverInfo; }
        public void setServerInfo(String serverInfo) { this.serverInfo = serverInfo; }
    }
}