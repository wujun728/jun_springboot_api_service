package com.yatop.lambda.base.model;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.yatop.lambda.base.model.views.components.Page;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CustomJsonNodeFactory;
import com.yuyaogc.lowcode.engine.util.CustomParserFactory;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.util.*;

import static com.yatop.lambda.base.model.views.Json_view_obj.*;

@Table(name = "base_ui_view")
public class IrUiView extends Model<IrUiView> {
    @Id
    private Long id;
    @Column(label = "视图名")
    private String name;
    @Column(label = "模型")
    private String model;
    @Column(label = "唯一")
    private String key;
    @Column(label = "模式")
    private String mode;
    @Column(label = "激活")
    private boolean active;
//    @ManyToOne
//    @JoinColumn(name = "inherit_id")
//    private IrUiView inherit;

    @Column(type = DataTypeEnum.TEXT,label = "内容")
    private String arch;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;




    public Long getId() {
        return (Long) this.get("id");
    }

    public IrUiView setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public IrUiView setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getModel() {
        return (String) this.get("model");
    }

    public IrUiView setModel(String model) {
        this.set("model", model);
        return this;
    }

    public String getKey() {
        return (String) this.get("key");
    }

    public IrUiView setKey(String key) {
        this.set("key", key);
        return this;
    }

    public String getMode() {
        return (String) this.get("mode");
    }

    public IrUiView setMode(String mode) {
        this.set("mode", mode);
        return this;
    }

    public boolean isActive() {
        return (boolean) this.get("active");
    }

    public IrUiView setActive(boolean active) {
        this.set("active", active);
        return this;
    }

    public IrUiView setArch(String arch) {
        this.set("arch", arch);
        return this;
    }

    public String getArch() {
        return (String) this.get("arch");
    }

    public String getCreateBy() {
        return (String) this.get("createBy");
    }

    public IrUiView setCreateBy(String createBy) {
        this.set("createBy", createBy);
        return this;
    }

    public Date getCreateTime() {
        return (Date) this.get("createTime");
    }

    public IrUiView setCreateTime(Date createTime) {
        this.set("createTime", createTime);
        return this;
    }

    public String getUpdateBy() {
        return (String) this.get("updateBy");
    }

    public IrUiView setUpdateBy(String updateBy) {
        this.set("updateBy", updateBy);
        return this;
    }

    public Date getUpdateTime() {
        return (Date) this.get("updateTime");
    }

    public IrUiView setUpdateTime(Date updateTime) {
        this.set("updateTime", updateTime);
        return this;
    }



    @Service(displayName = "加载试图")
    public String loadView(String key, String model, String module) {
        IrUiView uiView = new IrUiView();
        List<IrUiView> views = uiView.search(Criteria.equal("key", key), 0, 0, "");
        IrUiView primary = null;
        List<IrUiView> extension = new ArrayList<>();
        for (IrUiView view : views) {
          //  if (primary == null && "primary".equals(view.get("mode"))) {
                primary = view;
//            }
//            if ("extension".equals(view.get("mode"))) {
//                extension.add(view);
//            }
        }
        if (primary == null) {
            Container container = Container.me();
            Application app = container.get(module);
            EntityClass entityClass =  app.getEntity(model);

            Page page = new Page();
            page.setTitle(entityClass.getDisplayName());
            page.setType("page");

            page.setToolbar(Arrays.asList(buildToolbar(entityClass)));

            page.setBody(Arrays.asList(buildCurd2(entityClass)));


            page.setAside(Arrays.asList());



            return JSON.toJSONString( page);
            //throw new EngineException("找不到视图");
        }

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(primary.getArch().getBytes());
            CustomParserFactory customParserFactory = new CustomParserFactory();
            ObjectMapper mapper = new ObjectMapper(customParserFactory);
            CustomJsonNodeFactory factory = new CustomJsonNodeFactory(mapper.getDeserializationConfig().getNodeFactory(),
                    customParserFactory);
            mapper.setConfig(mapper.getDeserializationConfig().with(factory));
            Configuration config = Configuration.builder().mappingProvider(new JacksonMappingProvider(mapper))
                    .jsonProvider(new JacksonJsonNodeJsonProvider(mapper)).options(Option.ALWAYS_RETURN_LIST)
                    .options(Option.SUPPRESS_EXCEPTIONS).build();
            JsonContext context = (JsonContext) JsonPath.parse(inputStream, config);
            return context.jsonString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void combined(Elements base, Elements data) {
        for (Element el : data) {
            String position = el.attr("position");
            if ("xpath".equals(el.tagName())) {
                String has = el.attr("has");
                if (StringUtils.isNotEmpty(has)) {
                    Elements found = base.select(has);
                    if (found.size() == 0) {
                        continue;
                    }
                }
                String hasNot = el.attr("has_not");
                if (StringUtils.isNotEmpty(hasNot)) {
                    Elements found = base.select(hasNot);
                    if (found.size() > 0) {
                        continue;
                    }
                }
                String expr = el.attr("expr");
                Elements selects = base.select(expr);
                for (Element select : selects) {
                    combined(position, select, el.childNodes().toArray(new Node[el.childNodeSize()]));
                }
            } else {
                for (Element select : base) {
                    combined(position, select, el);
                }
            }
        }
    }

    static void combined(String position, Element target, Node... nodes) {
        if ("before".equals(position)) {
            for (Node node : nodes) {
                target.before(node);
            }
        } else if ("after".equals(position)) {
            for (Node node : nodes) {
                target.after(node);
            }
        } else if ("replace".equals(position)) {
            boolean first = true;
            for (Node node : nodes) {
                if (first) {
                    first = false;
                    target.replaceWith(node);
                } else {
                    target.after(node);
                }
            }
        } else if ("inside".equals(position)) {
            target.appendChildren(Arrays.asList(nodes));
        } else if ("attribute".equals(position)) {
            for (Node node : nodes) {
                target.attributes().addAll(node.attributes());
            }
        }
    }

    Elements getData(Document doc) {
        for (Element el : doc.children()) {
            if ("data".equals(el.tagName())) {
                return el.children();
            }
        }
        return doc.children();
    }
}
