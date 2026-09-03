package com.yuyaogc.lowcode.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.exception.ValueException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.KvMap;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.checkerframework.checker.units.qual.C;
import org.dom4j.Document;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.Locator;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportUtils {
    public interface ErrorHandle {
        void handle(String message, Exception ex);
    }

    public static void importXml(InputStream input, String module, Context env, ErrorHandle onError) {
        new XmlImport(input, module, env, onError).load();
    }

    public static void importJson(InputStream input, String module,  Context context){
        new JsonImport(input,module,context).load();
    }

}


class JsonImport{
    DocumentContext documentContext;
    String module;

    Context context;

    public JsonImport(InputStream input, String module,Context context){
        this.module = module;
        this.context = context;
        documentContext = buildDocumentContext(input, "", null);
    }


    public DocumentContext buildDocumentContext(InputStream inputStream, String filePath, String appName) {
        try {
            CustomParserFactory customParserFactory = new CustomParserFactory();
            ObjectMapper mapper = new ObjectMapper(customParserFactory);
            CustomJsonNodeFactory factory = new CustomJsonNodeFactory(mapper.getDeserializationConfig().getNodeFactory(),
                    customParserFactory);
            mapper.setConfig(mapper.getDeserializationConfig().with(factory));
            Configuration config = Configuration.builder().mappingProvider(new JacksonMappingProvider(mapper))
                    .jsonProvider(new JacksonJsonNodeJsonProvider(mapper)).options(Option.ALWAYS_RETURN_LIST)
                    .options(Option.SUPPRESS_EXCEPTIONS).build();
            JsonContext context = (JsonContext) JsonPath.parse(inputStream, config);
            return context;
        } catch (InvalidJsonException e) {
            if (e.getCause() instanceof JsonParseException) {
                JsonParseException cause = (JsonParseException) e.getCause();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }

        }
        return null;
    }


    public void load(){
        parseDocument(documentContext);
    }


    public void parseDocument(DocumentContext documentContext) {
        try {
            if (documentContext == null) {
                return;
            }

            ArrayNode dataNodes = documentContext.read("$.data");
            if (dataNodes.size() > 0) {
                ObjectNode dataNode = (ObjectNode) dataNodes.get(0);
                for (Iterator<Map.Entry<String, JsonNode>> it = dataNode.fields(); it.hasNext();) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    handleRecordItem(entry);
                }

            }

            ArrayNode viewsNodes = documentContext.read("$.views");
            if (viewsNodes.size() > 0) {
                ObjectNode viewNode = (ObjectNode) viewsNodes.get(0);
                for (Iterator<Map.Entry<String, JsonNode>> it = viewNode.fields(); it.hasNext();) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    handleRecordItem(entry);
                }

            }

        }catch (Exception e){
            throw e;
        }
    }

    public void handleRecordItem(Map.Entry<String, JsonNode> entry) {
        String name = entry.getKey();
        ObjectNode jsonObject = (ObjectNode) entry.getValue();

        Model  v =  context.get("base.base_ui_view").selectOne(Criteria.equal("key", name));
        Model model = new Model();
        model.set("key",name);
        model.set("name",name);
        model.set("arch",jsonObject.toPrettyString());
        model.set("createTime", new Date());
        model.set("updateTime", new Date());

        if(v == null){
            context.get("base.base_ui_view").create(model);

            Model modelData = new Model();
            modelData.set("module",module);
            modelData.set("refId",name);
            modelData.set("name",name);
            context.get("base.base_mode_data").create(modelData);
        } else {
            v.set("arch",jsonObject.toPrettyString());
            v.set("updateTime", new Date());
            v.set("updateBy", context.getUserId());
            context.get("base.base_ui_view").updateById(v);
        }



    }


}


class XmlImport {
    Document doc;
    ImportUtils.ErrorHandle onError;
    Context env;
    String module;

    public XmlImport(InputStream input, String module, Context env, ImportUtils.ErrorHandle onError) {
        this.module = module;
        this.env = env;
        this.onError = onError;
        XmlReader reader = new XmlReader();
        reader.setDocumentFactory(new XmlDocumentFactory());
        try {
            doc = reader.read(input);
        } catch (Exception e) {
            onError("读取xml失败", e);
        }
    }

    public void load() {
        Element root = doc.getRootElement();
        for (Element el : root.elements()) {
            try {
                if ("record".equals(el.getName())) {
                    loadRecord(el);
                } else if ("menu".equals(el.getName())) {
                    loadMenu(el);
                } else if ("view".equals(el.getName())) {
                    loadView(el);
                } else if ("web".equals(el.getName())) {
                    loadWeb(el);
                } else if ("action".equals(el.getName())) {
                    loadAction(el);
                }
            } catch (Exception ex) {
                onError(el, ex);
            }
        }
    }

    void loadAction(Element el) {
    }

    void loadWeb(Element el) {
        String id = getAttribute(el, "id");
        if (!id.contains(".")) {
            id = module + "." + id;
        }
        String key = id;
        String inherit_id = getAttributeOr(el, "inherit_id", null);
        String mode = "primary";
        if (StringUtils.isNotEmpty(inherit_id)) {
            if (!inherit_id.contains(".")) {
                inherit_id = module + "." + inherit_id;
            }
           // Records inherit = env.getRef(inherit_id);
           // key = (String) inherit.get("key");
           // inherit_id = inherit.getId();
            mode = "extension";
        }
        String name = getAttribute(el, "name");
        String priority = getAttributeOr(el, "priority", "16");
        String arch = "";
        for (Element e : el.elements()) {
            arch += e.asXML();
        }

//        Records rec = env.findRef(id);
//        if (rec == null) {
//            rec = env.get("ir.ui.view");
//        } else if (!rec.getMeta().getName().equals("ir.ui.view")) {
//            throw new ValueException("模型必需是ir.ui.view");
//        }
//        KvMap values = new KvMap()
//                .set("key", key)
//                .set("name", name)
//                .set("arch", arch)
//                .set("mode", mode)
//                .set("inherit_id", inherit_id)
//                .set("priority", Integer.valueOf(priority))
//                .set("type", "web");
//
//        if (rec.any()) {
//            rec.update(values);
//        } else {
//            rec = rec.create(values);
//            env.get("ir.model.data").create(new KvMap()
//                    .set("name", id.split("\\.", 2)[1])
//                    .set("module", module)
//                    .set("model", "ir.ui.view")
//                    .set("res_id", rec.getId()));
//        }
    }

    void loadView(Element el) {
        String id = getAttribute(el, "id");
        String name = getAttribute(el, "name");
        String model = getAttribute(el, "model");
        if (!id.contains(".")) {
            id = module + "." + id;
        }
        String inherit_id = getAttributeOr(el, "inherit_id", null);
        String key = getAttributeOr(el, "key", null);
        String mode = "primary";
        if (StringUtils.isNotEmpty(inherit_id)) {
            if (!inherit_id.contains(".")) {
                inherit_id = module + "." + inherit_id;
            }
//            Records inherit = env.getRef(inherit_id);
//            inherit_id = inherit.getId();
//            key = (String) inherit.get("key");
            mode = "extension";
        }
        String priority = getAttributeOr(el, "priority", "16");
        String type = getAttributeOr(el, "type", null);
        if (StringUtils.isEmpty(type)) {
            type = el.elements().get(0).getName();
        }
        String arch = "";
        for (Element e : el.elements()) {
            arch += e.asXML();
        }

//        Records rec = env.findRef(id);
//        if (rec == null) {
//            rec = env.get("ir.ui.view");
//        } else if (!rec.getMeta().getName().equals("ir.ui.view")) {
//            throw new ValueException("模型必需是ir.ui.view");
//        }
        KvMap values = new KvMap()
                .set("model", model)
                .set("name", name)
                .set("arch", arch)
                .set("mode", mode)
                .set("key", key)
                .set("inherit_id", inherit_id)
                .set("priority", Integer.valueOf(priority))
                .set("type", type);

//        if (rec.any()) {
//            rec.update(values);
//        } else {
//            rec = rec.create(values);
//            env.get("ir.model.data").create(new KvMap()
//                    .set("name", id.split("\\.", 2)[1])
//                    .set("module", module)
//                    .set("model", "ir.ui.view")
//                    .set("res_id", rec.getId()));
//        }
    }

    void loadMenu(Element el) {
        String id = getAttribute(el, "id");
        if (!id.contains(".")) {
            id = module + "." + id;
        }
        String parent = getAttributeOr(el, "parent", null);
        if (StringUtils.isNotEmpty(parent)) {
            if (!parent.contains(".")) {
                parent = module + "." + parent;
            }
        }
        String name = getAttribute(el, "name");
        String seq = getAttributeOr(el, "seq", "0");
        String url = getAttributeOr(el, "url", null);
        String module = getAttributeOr(el, "module", null);
        String model = getAttributeOr(el, "model", null);
        String view = getAttributeOr(el, "view", null);
        String icon = getAttributeOr(el, "icon", null);
        String click = getAttributeOr(el, "click", null);
        String css = getAttributeOr(el, "css", null);
        // TODO action

        Model values = new Model();
                values.set("name", name)
                .set("key", id)
                .set("url", url)
                .set("model", model)
                .set("parentId", parent)
                .set("icon", icon)
                .set("click", click)
                .set("css", css)
                .set("sequence", Integer.valueOf(seq))
                .set("module", module)
                .set("view", view);


        Model v =  env.get("base.base_ui_menu").selectOne(Criteria.equal("key", id));
        if(v == null){
            env.get("base.base_ui_menu").create(values);

            Model modelData = new Model();
            modelData.set("module",module);
            modelData.set("refId",id);
            modelData.set("name",id.split("\\.", 2)[1]);
            env.get("base.base_mode_data").create(modelData);
        } else {
            values.set("id", v.get("id"));
            env.get("base.base_ui_menu").updateById(values);
        }

//
//        if (rec.any()) {
//            rec.update(values);
//        } else {
//            rec = rec.create(values);
//            env.get("ir.model.data").create(new KvMap()
//                    .set("name", id.split("\\.", 2)[1])
//                    .set("module", module)
//                    .set("model", "ir.ui.menu")
//                    .set("res_id", rec.getId()));
//        }
    }

    String getAttribute(Element el, String name) {
        Attribute attr = el.attribute(name);
        if (attr == null) {
            throw new ValueException(String.format("未指定%s属性", name));
        }
        return attr.getText();
    }

    String getAttributeOr(Element el, String name, String defaultValue) {
        Attribute attr = el.attribute(name);
        if (attr == null) {
            return defaultValue;
        }
        return attr.getText();
    }

    void loadRecord(Element el) {
        String key = getAttribute(el, "id");
        String model = getAttribute(el, "model");
        if (!key.contains(".")) {
            key = module + "." + key;
        }
//        Records rec = env.findRef(key);
//        if (rec == null) {
//            rec = env.get(model);
//        } else if (!rec.getMeta().getName().equals(model)) {
//            throw new ValueException("模型必需是" + model);
//        }
//        KvMap values = new KvMap();
//        for (Element field : el.elements("field")) {
//            try {
//                Tuple<String, Object> tuple = getField(field);
//                values.put(tuple.getItem1(), tuple.getItem2());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                onError(field, ex);
//            }
//        }
//        if (rec.any()) {
//            rec.update(values);
//        } else {
//            rec = rec.create(values);
//            env.get("ir.model.data").create(new KvMap()
//                    .set("name", key.split("\\.", 2)[1])
//                    .set("module", module)
//                    .set("model", model)
//                    .set("res_id", rec.getId()));
//        }
    }

    Pattern refPattern = Pattern.compile("ref\\((?<ref>\\S+?)\\)");

    Object evalValue(String eval) {
        ObjectMapper map = new ObjectMapper();
        try {
            JsonNode node = map.readTree(eval);
            if (node.isBoolean()) {
                return node.asBoolean();
            } else if (node.isDouble()) {
                return node.asDouble();
            } else if (node.isInt()) {
                return node.asInt();
            } else if (node.isNull()) {
                return null;
            } else if (node.isTextual()) {
                return node.asText();
            } else if (node.isArray()) {
                return map.readValue(eval, List.class);
            }
            return map.readValue(eval, Map.class);
        } catch (Exception e) {
            throw new ValueException(String.format("eval的值[%s]解析失败", eval));
        }
    }

//    Tuple<String, Object> getField(Element field) {
//        String name = getAttribute(field, "name");
//        String eval = getAttributeOr(field, "eval", null);
//        if (eval != null) {
//            Matcher m = refPattern.matcher(eval);
//            while (m.find()) {
//                String ref = m.group();
//                String refVal = m.group("ref");
//                Records refRec = env.getRef(refVal);
//                eval = eval.replace(ref, "\"" + refRec.getId() + "\"");
//            }
//            Object val = evalValue(eval);
//            return new Tuple<>(name, val);
//        }
//        String ref = getAttributeOr(field, "ref", null);
//        if (ref != null) {
//            Records refRec = env.getRef(ref);
//            return new Tuple<>(name, refRec.getId());
//        }
//        return new Tuple<>(name, field.getText());
//    }

    public void onError(String message, Exception ex) {
        if (onError != null) {
            onError.handle(message, ex);
        }
    }

    public void onError(Element el, Exception ex) {
        if (onError != null) {
            onError.handle(String.format("解析第[%s]行元素[%s]发生错误：", ((XmlElement) el).getLineNumber(), el.getName())
                    + ThrowableUtils.getCause(ex).getMessage(), ex);
        }
    }

    // #region xml linenumber

    static class XmlReader extends SAXReader {

        @Override
        protected SAXContentHandler createContentHandler(XMLReader reader) {
            return new XmlContentHandler(getDocumentFactory(),
                    getDispatchHandler());
        }

        @Override
        public void setDocumentFactory(DocumentFactory documentFactory) {
            super.setDocumentFactory(documentFactory);
        }

    }

    static class XmlContentHandler extends SAXContentHandler {

        private Locator locator;

        // this is already in SAXContentHandler, but private
        private DocumentFactory documentFactory;

        public XmlContentHandler(DocumentFactory documentFactory,
                ElementHandler elementHandler) {
            super(documentFactory, elementHandler);
            this.documentFactory = documentFactory;
        }

        @Override
        public void setDocumentLocator(Locator documentLocator) {
            super.setDocumentLocator(documentLocator);
            this.locator = documentLocator;
            if (documentFactory instanceof XmlDocumentFactory) {
                ((XmlDocumentFactory) documentFactory).setLocator(documentLocator);
            }

        }

        public Locator getLocator() {
            return locator;
        }
    }

    static class XmlDocumentFactory extends DocumentFactory {

        private Locator locator;

        public XmlDocumentFactory() {
            super();
        }

        public void setLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public Element createElement(QName qname) {
            XmlElement element = new XmlElement(qname);
            if (locator != null)
                element.setLineNumber(locator.getLineNumber());
            return element;
        }

    }

    /**
     * An Element that is aware of it location (line number in) in the source
     * document
     */
    static class XmlElement extends DefaultElement {

        private int lineNumber = -1;

        public XmlElement(QName qname) {
            super(qname);
        }

        public XmlElement(QName qname, int attributeCount) {
            super(qname, attributeCount);

        }

        public XmlElement(String name, Namespace namespace) {
            super(name, namespace);

        }

        public XmlElement(String name) {
            super(name);

        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }

    // #endregion
}