package com.ruoyi.api.mcp.web;

import com.ruoyi.api.mcp.transport.WebMvcSseServerTransport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/mcp")
public class McpController {

    private final WebMvcSseServerTransport transport;

    public McpController(WebMvcSseServerTransport transport) {
        this.transport = transport;
    }

    @GetMapping(value = "/sse", produces = "text/event-stream")
    public void sse(HttpServletRequest request, HttpServletResponse response) {
        transport.handleSse(request, response);
    }

    @PostMapping("/message")
    public void message(HttpServletRequest request, HttpServletResponse response) throws IOException {
        transport.handleMessage(request, response);
    }
}