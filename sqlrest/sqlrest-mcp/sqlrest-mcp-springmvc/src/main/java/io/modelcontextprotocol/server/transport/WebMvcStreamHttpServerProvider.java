package io.modelcontextprotocol.server.transport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCResponse;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCResponse.JSONRPCError;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.util.Assert;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
public class WebMvcStreamHttpServerProvider {

  public static final String DEFAULT_SSE_ENDPOINT = "/mcp";

  private final ObjectMapper objectMapper;

  private final WebMvcSseServerAuthChecker serverAuthChecker;

  private final String mcpEndpoint;

  private final McpSyncServer mcpSyncServer;

  private final RouterFunction<ServerResponse> routerFunction;

  public WebMvcStreamHttpServerProvider(ObjectMapper objectMapper,
      WebMvcSseServerAuthChecker serverAuthChecker, String mcpEndpoint,
      McpSyncServer mcpSyncServer) {
    Assert.notNull(objectMapper, "ObjectMapper must not be null");
    Assert.notNull(mcpEndpoint, "MCP endpoint must not be null");

    this.objectMapper = objectMapper;
    this.serverAuthChecker = serverAuthChecker;
    this.mcpEndpoint = mcpEndpoint;
    this.mcpSyncServer = mcpSyncServer;
    this.routerFunction = RouterFunctions.route()
        .GET(this.mcpEndpoint, this::handleGet)
        .POST(this.mcpEndpoint, this::handlePost)
        .DELETE(this.mcpEndpoint, this::handleDelete)
        .build();
  }

  public RouterFunction<ServerResponse> getRouterFunction() {
    return routerFunction;
  }

  private ServerResponse handleGet(ServerRequest request) {
    return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED).body("GET method is not allowed.");
  }

  private ServerResponse handleDelete(ServerRequest request) {
    return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED).body("DELETE method is not allowed.");
  }

  private ServerResponse handlePost(ServerRequest request) {
    List<MediaType> acceptHeaders = request.headers().asHttpHeaders().getAccept();
    if (!acceptHeaders.contains(MediaType.APPLICATION_JSON)) {
      return ServerResponse.badRequest()
          .body(new McpError("Invalid Accept headers. Expected APPLICATION_JSON"));
    }

    if (null != this.serverAuthChecker) {
      String tokenParamName = this.serverAuthChecker.getTokenParamName();
      if (!request.param(tokenParamName).isPresent()) {
        return ServerResponse.badRequest().body(new McpError("Token missing in param"));
      }

      String accessToken = request.param(tokenParamName).get();
      if (!this.serverAuthChecker.checkTokenValid(accessToken)) {
        return ServerResponse.badRequest().body(new McpError("Token invalid in param"));
      }
    }

    try {
      String body = request.body(String.class);
      ObjectNode root = objectMapper.readValue(body, ObjectNode.class);
      String method = root.get("method").asText();
      switch (method) {
        case McpSchema.METHOD_INITIALIZE:
          return handleInitialize(root.get("id").asText());
        case McpSchema.METHOD_NOTIFICATION_INITIALIZED:
          return handleNotificationInitialized();
        case McpSchema.METHOD_TOOLS_LIST:
          return handleListTools(root.get("id").asText());
        case McpSchema.METHOD_PING:
          return handlePing(root.get("id").asText());
        case McpSchema.METHOD_TOOLS_CALL:
          return handleCallTool(root.get("id").asText(), root.get("params"));
        case McpSchema.METHOD_LOGGING_SET_LEVEL:
          return handleSettingLoggingLevel(root.get("id").asText(), root.get("params"));
        default:
          return handleUnsupportedMethod(root.get("id").asText(), method);
      }
    } catch (Exception e) {
      log.error("Error handling message: {}", e.getMessage(), e);
      return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new McpError(e.getMessage()));
    }
  }

  private ServerResponse handleInitialize(String id) {
    Map<String, Object> initMap = new HashMap<>(2);
    initMap.put("protocolVersion", McpSchema.LATEST_PROTOCOL_VERSION);
    initMap.put("capabilities", McpSchema.ServerCapabilities.builder()
            .tools(true)
            .build());
    initMap.put("serverInfo", mcpSyncServer.getServerInfo());

    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setResult(initMap);

    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }

  private ServerResponse handleNotificationInitialized() {
    McpSchema.InitializeResult initializeResult = new McpSchema.InitializeResult(
        mcpSyncServer.getServerInfo().getVersion(), mcpSyncServer.getServerCapabilities(),
        mcpSyncServer.getServerInfo(), null);
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(initializeResult);
  }

  private ServerResponse handleUnsupportedMethod(String id, String method) {
    JSONRPCError jsonrpcError = new JSONRPCError();
    jsonrpcError.setCode(McpSchema.ErrorCodes.METHOD_NOT_FOUND);
    jsonrpcError.setMessage("Unsupported method: '" + method + "' ");

    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setError(jsonrpcError);
    return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }

  private ServerResponse handlePing(String id) {
    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setResult(Collections.emptyMap());
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }

  private ServerResponse handleListTools(String id) {
    List<Tool> tools = mcpSyncServer.listTool().stream()
        .map(McpServerFeatures.AsyncToolSpecification::getTool)
        .collect(Collectors.toList());

    Map<String, Object> toolsMap = new HashMap<>(2);
    toolsMap.put("tools", tools);

    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setResult(toolsMap);
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }

  private ServerResponse handleCallTool(String id, JsonNode params) {
    McpSchema.CallToolRequest callToolRequest = objectMapper.convertValue(params,
        new TypeReference<CallToolRequest>() {
        });

    Optional<AsyncToolSpecification> toolSpecification = mcpSyncServer.listTool().stream()
        .filter(tr -> callToolRequest.getName().equals(tr.getTool().getName()))
        .findAny();

    if (!toolSpecification.isPresent()) {
      JSONRPCError jsonrpcError = new JSONRPCError();
      jsonrpcError.setCode(McpSchema.ErrorCodes.METHOD_NOT_FOUND);
      jsonrpcError.setMessage("Tool not found: '" + callToolRequest.getName() + "' ");

      JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
      jsonrpcResponse.setId(id);
      jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
      jsonrpcResponse.setError(jsonrpcError);
      return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
    }

    AsyncToolSpecification tool = toolSpecification.get();

    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setResult(tool.getCall().apply(null, callToolRequest.getArguments()).block());
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }

  private ServerResponse handleSettingLoggingLevel(String id, JsonNode params) {
    McpSchema.SettingLoggingLevelRequest settingLoggingLevelRequest = objectMapper.convertValue(params,
        new TypeReference<McpSchema.SettingLoggingLevelRequest>() {
        });

    JSONRPCResponse jsonrpcResponse = new JSONRPCResponse();
    jsonrpcResponse.setId(id);
    jsonrpcResponse.setJsonrpc(McpSchema.JSONRPC_VERSION);
    jsonrpcResponse.setResult(Collections.emptyMap());
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jsonrpcResponse);
  }
}
