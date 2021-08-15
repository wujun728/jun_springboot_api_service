package cn.edu.scu.virjarjcd.feature.vo;

import cn.edu.scu.virjarjcd.util.ConfigHolder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Created by virjar on 16/5/17.
 *  对json空数据的忽略处理，如果值为空，则json中不包含这一项。一方面节省传输带宽，一方面屏蔽服务器内部数据
 */
public class NoNullIncludePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        if(!"false".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.nonull"))){
            return true;
        }
        return false;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if(topLevelClass.getType().getShortName().toLowerCase().endsWith("vo")|| !"multi".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.modellayer"))){
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonInclude");
            topLevelClass.addAnnotation("@JsonInclude(JsonInclude.Include.NON_NULL)");
        }
        return true;
    }
}
