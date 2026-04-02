package com.gitee.freakchicken.dbapi.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import com.gitee.freakchicken.dbapi.service.DataSourceService;
import com.gitee.freakchicken.dbapi.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 10:43
 **/
@Slf4j
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping("/add")
    public void add(DataSource dataSource) {
        dataSourceService.add(dataSource);
    }

    @RequestMapping("/getAll")
    public List<DataSource> getAll() {
        return dataSourceService.getAll();
    }

    @RequestMapping("/detail/{id}")
    public DataSource detail(@PathVariable String id) {
        return dataSourceService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        return dataSourceService.delete(id);
    }

    @RequestMapping("/update")
    public DataSource update(DataSource dataSource) {
        dataSourceService.update(dataSource);
        return null;
    }

    @RequestMapping("/connect")
    public ResponseDto connect(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = JdbcUtil.getConnection(dataSource);
            return ResponseDto.apiSuccess(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @RequestMapping("/export")
    public void export(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        List<DataSource> list = dataSourceService.selectBatch(collect);
        String s = JSON.toJSONString(list);
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=datasource.json");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(s.getBytes("utf-8"));
        } catch (Exception e) {
            log.error("导出数据源失败", e);
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                log.error("关闭输出流失败", e);
            }
        }
    }


    private static final long MAX_UPLOAD_SIZE = 10 * 1024 * 1024; // 10MB

    @RequestMapping(value = "/import", produces = "application/json;charset=UTF-8")
    public ResponseDto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        // 文件大小校验
        if (file.isEmpty()) {
            return ResponseDto.fail("上传文件不能为空");
        }
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            return ResponseDto.fail("上传文件大小不能超过10MB");
        }

        // 文件类型校验：仅允许 JSON 文件
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".json")) {
            return ResponseDto.fail("仅支持 .json 格式的文件");
        }
        String contentType = file.getContentType();
        if (contentType != null && !contentType.contains("json") && !contentType.equals("application/octet-stream")) {
            return ResponseDto.fail("文件内容类型不合法，仅支持 JSON");
        }

        String s = IOUtils.toString(file.getInputStream(), "utf-8");

        // 内容校验：确保是合法的 JSON 数组
        List<DataSource> list;
        try {
            list = JSON.parseArray(s, DataSource.class);
        } catch (Exception e) {
            log.warn("导入数据源文件反序列化失败: {}", e.getMessage());
            return ResponseDto.fail("文件内容不是合法的 JSON 数组");
        }
        if (list == null || list.isEmpty()) {
            return ResponseDto.fail("文件中未包含有效的数据源配置");
        }

        dataSourceService.insertBatch(list);
        return ResponseDto.successWithMsg("成功导入 " + list.size() + " 个数据源");
    }
}
