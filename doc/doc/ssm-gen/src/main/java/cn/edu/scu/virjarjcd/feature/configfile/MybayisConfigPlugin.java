package cn.edu.scu.virjarjcd.feature.configfile;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.TextNodeBuilder;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virjar on 16/4/10.
 */
public class MybayisConfigPlugin extends MergePluginAdpter{
    private String resourcePath;
    private XmlFormatter xmlFormatter;

    @Override
    public boolean calcEnv() {
        resourcePath = ConfigHolder.instance.getProperty("sys.webapp.resoucepath");
        if (ConfigHolder.instance.getProperty("formater.spring", "mybatis").trim().equalsIgnoreCase("jcg")) {
            xmlFormatter = new VirjarXmlFormater();
            xmlFormatter.setContext(getContext());
        } else {
            xmlFormatter = getContext().getXmlFormatter();
        }
        return true;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        Document document = new Document("-//mybatis.org//DTD SQL Map Config 3.0//EN", "http://mybatis.org/dtd/mybatis-3-config.dtd");
        XmlElement rootElement = new XmlElement("configuration");
        GeneratedXmlFile gxf = new GeneratedXmlFile(document,"mybatis-config.xml", "",resourcePath,false, xmlFormatter);
        answer.add(gxf);
        document.setRootElement(rootElement);

        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("settings")
                        .addNode(
                                new XmlElementChainBuider("setting")
                                .addAttribute("name","cacheEnabled")
                                .addAttribute("value","false")
                        )
                        .addNode(
                                new XmlElementChainBuider("setting")
                                .addAttribute("name","defaultExecutorType")
                                .addAttribute("value","REUSE")
                        )
                )
//                .addNode(
//                        new XmlElementChainBuider("typeAliases")
//                )
//                .addNode(
//                        new XmlElementChainBuider("typeHandlers")
//                )
//                .addNode(
//                        new XmlElementChainBuider("plugins")
//                )
                .build();


        return answer;
    }
}
