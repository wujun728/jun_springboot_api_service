package com.zapi.onlyoffice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import com.zapi.file.strategy.FileStorageStrategy;
import com.zapi.onlyoffice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/onlyoffice")
@RequiredArgsConstructor
public class OnlyOfficeController {

    private final JwtUtils jwtUtils;

    @Value("${zapi.file.local.root-path}")
    private String storagePath;

    @Value("${onlyoffice.document-server-url}")
    private String documentServerUrl;

    // 对 Docs Server 可达的后端基地址，例如 http://192.168.3.108:8080/zapi-allin-sys
    @Value("${onlyoffice.external-base-url}")
    private String externalBaseUrl;

    // Docs 若开启JWT（7.2+默认），这里也需要开启并生成token；若关闭，这里也设为false且不下发token
    @Value("${onlyoffice.jwt-enabled:false}")
    private boolean jwtEnabled;

    @Autowired
    private FileService fileService;

    @Autowired
    private final FileStorageStrategy fileStorageStrategy;

    @PostConstruct
    public void init() {
        File dir = new File(storagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /****
     *  获取编辑文档所需的配置
     */
    @GetMapping("/edit-config/{fileId}")
    public ResponseEntity<Map<String, Object>> getDocumentConfig(@PathVariable Long fileId) {
        try {
            /// todo 自行实现，根据文件id进行查询文件信息
            FileInfo fileInfo = fileService.getFileInfo(fileId);
            String documentTitle = fileInfo.getFileName();
            /// 组装文件能访问的下载地址 禁止使用 localhost/127.0.0.1
            String documentUrl = externalBaseUrl + "/file/download/" + fileInfo.getId();
            /// 组装文件回调地址，onlyoffice操作成功后回调到当前应用的接口地址  禁止使用 localhost/127.0.0.1
            String callbackUrl = externalBaseUrl + "/onlyoffice/callback/" + fileId;
            String ext = getFileExtension(documentTitle).toLowerCase();
            String documentType = mapDocumentType(ext);    // word|cell|slide
            String fileType = ext;                         // docx|xlsx|pptx|...
            long versionTs = System.currentTimeMillis();
            String key = "file_" + fileId + "_" + versionTs;
            // document
            Map<String, Object> documentMap = new HashMap<>();
            documentMap.put("title", documentTitle);
            documentMap.put("url", documentUrl);
            documentMap.put("fileType", fileType);
            documentMap.put("key", key);
            Map<String, Object> permissions = new HashMap<>();
            permissions.put("edit", true);
            permissions.put("download", true);
            documentMap.put("permissions", permissions);

            // editorConfig
            Map<String, Object> editorConfigMap = new HashMap<>();
            editorConfigMap.put("mode", "edit");
            editorConfigMap.put("callbackUrl", callbackUrl);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", "1");
            userMap.put("name", "zhonglz");
            editorConfigMap.put("user", userMap);
            editorConfigMap.put("lang", "zh-CN");

            // config（发送给Docs的配置；token 仅在开启JWT时下发）
            Map<String, Object> config = new HashMap<>();
            config.put("documentServerUrl", documentServerUrl);
            config.put("documentType", documentType);
            config.put("document", documentMap);
            config.put("editorConfig", editorConfigMap);
            config.put("width", "100%");
            config.put("height", "100%");
            config.put("type", "desktop");
            if (jwtEnabled) {
                Map<String, Object> claimsToSign = new HashMap<>(config);
                claimsToSign.remove("documentServerUrl");
                claimsToSign.remove("token");
                String token = jwtUtils.generateToken(claimsToSign);
                config.put("token", token);
            } else {
                config.remove("token");
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("config", config);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            // 出错也返回200，便于前端显示错误信息
            Map<String, Object> err = new HashMap<>();
            err.put("error", 0);
            err.put("message", "generate config failed: " + e.getMessage());
            return ResponseEntity.ok(err);
        }
    }

    /****
     * 强制保存，请求onlyoffice发送指令告诉服务器
     * 我已准备好文档进行保存，
     * 服务会直接调用下面的回调方法进行保存
     * @param key
     * @return
     */
    @GetMapping("/force-save/{key}")
    public ResponseEntity<?> forceSave(@PathVariable String key) {
        try {
            long ts = System.currentTimeMillis();
            if (key == null || key.isEmpty()) {
                return ResponseEntity.badRequest().body("key required");
            }
            // 常见两种路径，逐个尝试
            String base = documentServerUrl.endsWith("/") ? documentServerUrl : (documentServerUrl + "/");
            String[] candidates = new String[]{
                    base + "coauthoring/CommandService.ashx",
                    base + "coauthoring/CommandService"
            };
            Map<String, Object> payload = new HashMap<>();
            payload.put("c", "forcesave");
            payload.put("key", key);
            // payload.put("userdata", "manual-trigger-"+ts); // 可选
            String json = new ObjectMapper().writeValueAsString(payload);
            String lastErr = null;
            for (String cmdUrl : candidates) {
                try {
                    org.apache.http.client.fluent.Request req = org.apache.http.client.fluent.Request
                            .Post(cmdUrl)
                            .connectTimeout(10000)
                            .socketTimeout(30000)
                            .bodyString(json, org.apache.http.entity.ContentType.APPLICATION_JSON);
                    if (jwtEnabled) {
                        String token = jwtUtils.generateToken(payload);
                        req.addHeader("Authorization", "Bearer " + token);
                    }
                    org.apache.http.client.fluent.Response resp = req.execute();
                    // 取状态码与响应体
                    org.apache.http.HttpResponse raw = resp.returnResponse();
                    int status = raw.getStatusLine().getStatusCode();
                    String respBody = org.apache.http.util.EntityUtils.toString(raw.getEntity(), "UTF-8");
                    // 200/202/204 都视为已触发（Docs版本不同可能不同）
                    if (status >= 200 && status < 300) {
                        return ResponseEntity.ok(respBody);
                    }
                    lastErr = "HTTP " + status + ": " + respBody;
                } catch (Exception exOne) {
                    exOne.printStackTrace();
                    lastErr = exOne.getMessage();
                }
            }
            return ResponseEntity.status(502).body("CommandService call failed: " + lastErr);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("forceSave error for key=" + key + ", " + e.getMessage());
        }
    }


    /****
     * onlyoffice服务器调用，回调方法进行文件保存
     * @param documentId 文件编号，也可以是fileId
     * @param body
     * @return
     */
    @PostMapping("/callback/{documentId}")
    public ResponseEntity<String> handleCallback(@PathVariable Long documentId,
                                                 @RequestBody Map<String, Object> body) {
        long ts = System.currentTimeMillis();
        try {
            String status = String.valueOf(body.get("status"));
            String downloadUrl = body.get("url") != null ? body.get("url").toString() : null;
            if (jwtEnabled) {
                try {
                    Object headers = body.get("headers");
                    if (headers instanceof Map) {
                        Object auth = ((Map<?, ?>) headers).get("Authorization");
                        if (auth != null && auth.toString().startsWith("Bearer ")) {
                            String jwt = auth.toString().substring(7);
                            Claims claims = jwtUtils.getClaimsFromToken(jwt);
                        }
                    }
                } catch (Exception ignore) {
                }
            }
            /****
             * 已成功触发 CommandService，Docs 回调已到达，但回调状态是 6（强制保存回调），当前后端仅在 status==2 时才落盘，所以“跳过了保存”。按官方回调规范，强制保存需处理 status==6（必要时也处理 7）。只要把后端回调里判断改为同时接受 2 或 6，即可立即下载 url 落地。
             * 1 - 正在编辑文档，
             * 2 - 文档已准备好保存，
             * 3 - 发生文档保存错误，
             * 4 - 文档已关闭，没有任何更改，
             * 6 - 正在编辑文档，但保存了当前文档状态，
             * 7 - 强制保存文档时发生错误。
             */

            if (("2".equals(status) || "6".equals(status)|| "7".equals(status)) && downloadUrl != null && !downloadUrl.isEmpty()) {
                try {
                    saveDocumentFromUrl(downloadUrl, documentId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
            }

            return ResponseEntity.ok("{\"error\":0}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("{\"error\":0}");
        }
    }

    private static String getFileExtension(String fileName) {
        int idx = fileName == null ? -1 : fileName.lastIndexOf('.');
        return (idx >= 0 && idx < fileName.length() - 1) ? fileName.substring(idx + 1) : "";
    }

    private static String mapDocumentType(String ext) {
        switch (ext) {
            case "doc":
            case "docx":
            case "rtf":
            case "odt":
            case "txt":
            case "pdf":
                return "word";
            case "xls":
            case "xlsx":
            case "ods":
            case "csv":
                return "cell";
            case "ppt":
            case "pptx":
            case "odp":
                return "slide";
            default:
                return "word";
        }
    }

    private void saveDocumentFromUrl(String url, Long documentId) throws Exception {
        long ts = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            inputStream = org.apache.http.client.fluent.Request.Get(url)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .execute()
                    .returnContent()
                    .asStream();
            /// todo 自行实现根据文件编号进行文件信息查询。
            FileInfo fileInfo = fileService.getFileInfo(documentId);
            String fileName = fileInfo.getFileName();
            // todo 自行实现存储策略方法进行存储，由于是在线编辑，所以这里直接获取原来存储的方法，进行文件的替换
            fileStorageStrategy.uploadFileTH(inputStream, fileName, null);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
