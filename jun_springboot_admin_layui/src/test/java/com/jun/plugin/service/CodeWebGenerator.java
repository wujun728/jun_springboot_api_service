package com.jun.plugin.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CodeWebGenerator {


    public static void web(AutoGenerator mpg) throws Exception {
        String projectPath = System.getProperty("user.dir");
        List<TableInfo> tableInfoList = mpg.getConfig().getTableInfoList();
        Map<String, Object> map = mpg.getCfg().getMap();
        String easyMain = (String) map.get("easyMain");
        String easyModule = (String) map.get("easyModule");
        FileUtil.mkdir(projectPath + "/web/admin/view/" + easyModule);
        FileUtil.mkdir(projectPath + "/web/admin/view/" + easyModule + "/" + easyMain);
        for (TableInfo tableInfo : tableInfoList) {
            Map<String, Object> objectMap = mpg.getTemplateEngine().getObjectMap(tableInfo);
            System.out.println(JSONUtil.toJsonPrettyStr(tableInfo));

            handle(mpg, objectMap, "main.html.ftl", projectPath + "/web/admin/view/" + easyModule + "/" + easyMain + ".html");
            handle(mpg, objectMap, "add.html.ftl", projectPath + "/web/admin/view/" + easyModule + "/" + easyMain + "/add.html");
            handle(mpg, objectMap, "edit.html.ftl", projectPath + "/web/admin/view/" + easyModule + "/" + easyMain + "/edit.html");
        }


    }

    private static void handle(AutoGenerator mpg, Map<String, Object> objectMap, String templateName, String out) throws IOException, TemplateException {
        //1.创建一个 Configuration 对象， 参数是freemarker 的版本号
        Configuration configuration = new Configuration(Configuration.getVersion());

        //2.设置模板文件所在的路径
        configuration.setClassForTemplateLoading(CodeWebGenerator.class, "/templates/web");

        //3. 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        //4. 获取模板
        Template template = configuration.getTemplate(templateName);


        //6. 创建一个 Writer 对象，一般创建 FileWriter 对象，指定生成的文件名
        FileWriter fileWriter = new FileWriter(out);

        //7. 调用模板对象的 process 方法输出文件
        Map<String, Object> map = mpg.getCfg().getMap();
        objectMap.put("cfg", map);
        template.process(objectMap, fileWriter);

        //8. 关闭流
        fileWriter.close();
    }
}