package com.gitee.sqlrest.template;

import com.gitee.sqlrest.template.fragment.MixedSqlFragment;
import com.gitee.sqlrest.template.fragment.SqlFragment;
import com.gitee.sqlrest.template.fragment.impl.ChooseFragment;
import com.gitee.sqlrest.template.fragment.impl.ForEachFragment;
import com.gitee.sqlrest.template.fragment.impl.IfFragment;
import com.gitee.sqlrest.template.fragment.impl.SetFragment;
import com.gitee.sqlrest.template.fragment.impl.TextFragment;
import com.gitee.sqlrest.template.fragment.impl.TrimFragment;
import com.gitee.sqlrest.template.fragment.impl.WhenFragment;
import com.gitee.sqlrest.template.fragment.impl.WhereFragment;
import com.gitee.sqlrest.template.token.GenericTokenParser;
import com.gitee.sqlrest.template.token.TokenHandler;
import com.gitee.sqlrest.template.util.OgnlCacheUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SqlTemplate {

  private SqlFragment root;
  private Configuration cfg;

  public SqlTemplate(SqlFragment root, Configuration cfg) {
    super();
    this.root = root;
    this.cfg = cfg;
  }

  public SqlMeta process(Object data) {
    SqlContext context = new SqlContext(cfg, data);
    calculate(context);
    parseParameter(context);
    return new SqlMeta(context.getSql(), context.getParameter());
  }

  public Map<String, Boolean> getParameterNames() {
    Map<String, Boolean> inputs = new HashMap<>();
    this.root.applyParameter(inputs);
    Map<String, Boolean> names = new HashMap<>();
    Pattern pattern = Pattern.compile("\\([^()]*\\)|\\[[^\\[\\]]*\\]|\\{[^{}]*}");

    for (Map.Entry<String, Boolean> entry : inputs.entrySet()) {
      String name = entry.getKey();
      Matcher matcher = pattern.matcher(name);
      while (matcher.find()) {
        name = name.replaceAll(Pattern.quote(matcher.group()), "");
        matcher = pattern.matcher(name);
      }
      if (entry.getValue()) {
        names.put(name, true);
      } else {
        names.putIfAbsent(name, entry.getValue());
      }
    }

    return names;
  }

  private void parseParameter(final SqlContext context) {
    String sql = context.getSql();
    GenericTokenParser parser1 = new GenericTokenParser("#{", "}",
        new TokenHandler() {

          public String handleToken(String content) {
            Object value = OgnlCacheUtils.getValue(content, context.getBinding());
            if (value == null) {
              throw new RuntimeException("Can not found "
                  + content + " value");
            }

            context.addParameter(value);

            return "?";
          }
        });

    sql = parser1.parse(sql);

    context.setSql(sql);
  }

  private void calculate(SqlContext context) {
    this.root.apply(context);
  }

  static public class SqlTemplateBuilder {

    private Configuration cfg;
    private String templateContent;

    public SqlTemplateBuilder(Configuration cfg, String templateContent) {
      super();
      this.cfg = cfg;
      this.templateContent = templateContent;
    }

    public SqlTemplate build() {
      Document document = null;
      try {
        document = buildXml(templateContent);
      } catch (Exception e) {
        throw new RuntimeException("Error constructing the XML object");
      }

      List<SqlFragment> contents = buildDynamicTag(document.getElementsByTagName("script").item(0));
      return new SqlTemplate(new MixedSqlFragment(contents), cfg);
    }

    private List<SqlFragment> buildDynamicTag(Node node) {
      List<SqlFragment> contents = new ArrayList<>();
      NodeList children = node.getChildNodes();

      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        String nodeName = child.getNodeName();
        if (child.getNodeType() == Node.CDATA_SECTION_NODE
            || child.getNodeType() == Node.TEXT_NODE) {
          String sql = child.getTextContent();
          TextFragment textFragment = new TextFragment(sql);
          contents.add(textFragment);
        } else if (child.getNodeType() == Node.ELEMENT_NODE) {
          TagHandler tagHandler = this.nodeHandlers.get(nodeName.toLowerCase());
          if (tagHandler != null) {
            tagHandler.handleNode(child, contents);
          }

        }
      }

      return contents;
    }

    private Document buildXml(String templateContent)
        throws ParserConfigurationException, SAXException, IOException {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(false);
      factory.setIgnoringComments(true);
      factory.setIgnoringElementContentWhitespace(false);
      factory.setCoalescing(false);
      factory.setExpandEntityReferences(true);

      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(new EntityResolver() {

        public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
          return new InputSource(SqlTemplate.class.getClassLoader().getResourceAsStream("script-1.0.dtd"));
        }
      });
      builder.setErrorHandler(new ErrorHandler() {
        public void error(SAXParseException exception)
            throws SAXException {
          throw exception;
        }

        public void fatalError(SAXParseException exception)
            throws SAXException {
          throw exception;
        }

        public void warning(SAXParseException exception)
            throws SAXException {
        }
      });

      String format = "<?xml version = \"1.0\" ?>\r\n<!DOCTYPE script SYSTEM \"script-1.0.dtd\">\r\n<script>%s</script>";
      InputSource inputSource = new InputSource(new StringReader(String
          .format(format, templateContent)));

      return builder.parse(inputSource);
    }

    private Map<String, TagHandler> nodeHandlers = new HashMap<String, TagHandler>() {
      private static final long serialVersionUID = 7123056019193266281L;

      {
        put("trim", new TrimHandler());
        put("where", new WhereHandler());
        put("set", new SetHandler());
        put("foreach", new ForEachHandler());
        put("if", new IfHandler());
        put("choose", new ChooseHandler());
        put("when", new IfHandler());
        put("otherwise", new OtherwiseHandler());
      }
    };

    private interface TagHandler {

      void handleNode(Node nodeToHandle, List<SqlFragment> targetContents);
    }

    private class TrimHandler implements TagHandler {

      public void handleNode(Node nodeToHandle,
          List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);

        NamedNodeMap attributes = nodeToHandle.getAttributes();
        Node prefixAtt = attributes.getNamedItem("prefix");
        String prefix = prefixAtt == null ? null : prefixAtt.getTextContent();
        Node prefixOverridesAtt = attributes.getNamedItem("prefixOverrides");
        String prefixOverrides = prefixOverridesAtt.getTextContent();
        Node suffixAtt = attributes.getNamedItem("suffix");
        String suffix = suffixAtt == null ? null : suffixAtt.getTextContent();
        Node suffixOverridesAtt = attributes.getNamedItem("suffixOverrides");
        String suffixOverrides = suffixOverridesAtt == null ? null : suffixOverridesAtt.getTextContent();
        TrimFragment trim = new TrimFragment(mixedSqlFragment, prefix,
            suffix, prefixOverrides, suffixOverrides);
        targetContents.add(trim);
      }
    }

    private class WhereHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        WhereFragment where = new WhereFragment(mixedSqlFragment);
        targetContents.add(where);
      }
    }

    private class SetHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        SetFragment set = new SetFragment(mixedSqlFragment);
        targetContents.add(set);
      }
    }

    private class ForEachHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        NamedNodeMap attributes = nodeToHandle.getAttributes();
        Node collectionAtt = attributes.getNamedItem("collection");

        if (collectionAtt == null) {
          throw new RuntimeException(nodeToHandle.getNodeName() + " must has a collection attribute !");
        }

        String collection = collectionAtt.getTextContent();
        Node itemAtt = attributes.getNamedItem("item");
        String item = itemAtt == null ? "item" : itemAtt.getTextContent();
        Node indexAtt = attributes.getNamedItem("index");
        String index = indexAtt == null ? "index" : indexAtt.getTextContent();
        Node openAtt = attributes.getNamedItem("open");
        String open = openAtt == null ? null : openAtt.getTextContent();
        Node closeAtt = attributes.getNamedItem("close");
        String close = closeAtt == null ? null : closeAtt.getTextContent();
        Node separatorAtt = attributes.getNamedItem("separator");
        String separator = separatorAtt == null ? null : separatorAtt.getTextContent();
        ForEachFragment forEachSqlFragment = new ForEachFragment(
            mixedSqlFragment, collection, index, item, open, close, separator);
        targetContents.add(forEachSqlFragment);
      }
    }

    private class IfHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        NamedNodeMap attributes = nodeToHandle.getAttributes();
        Node testAtt = attributes.getNamedItem("test");
        if (testAtt == null) {
          throw new RuntimeException(nodeToHandle.getNodeName() + " must has test attribute ! ");
        }

        String test = testAtt.getTextContent();
        IfFragment ifSqlFragment = new IfFragment(mixedSqlFragment, test);
        targetContents.add(ifSqlFragment);
      }
    }

    private class OtherwiseHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        targetContents.add(mixedSqlFragment);
      }
    }

    private class ChooseHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> whenSqlFragments = new ArrayList<SqlFragment>();
        List<SqlFragment> otherwiseSqlFragments = new ArrayList<SqlFragment>();
        handleWhenOtherwiseNodes(nodeToHandle, whenSqlFragments, otherwiseSqlFragments);
        SqlFragment defaultSqlFragment = getDefaultSqlFragment(otherwiseSqlFragments);
        ChooseFragment chooseSqlFragment = new ChooseFragment(
            whenSqlFragments, defaultSqlFragment);
        targetContents.add(chooseSqlFragment);
      }

      private void handleWhenOtherwiseNodes(Node chooseSqlFragment,
          List<SqlFragment> ifSqlFragments,
          List<SqlFragment> defaultSqlFragments) {
        NodeList children = chooseSqlFragment.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
          Node child = children.item(i);
          if (child.getNodeType() == Node.ELEMENT_NODE) {
            String nodeName = child.getNodeName();
            TagHandler handler = nodeHandlers.get(nodeName);
            if (handler instanceof IfHandler) {
              handler.handleNode(child, ifSqlFragments);
            } else if (handler instanceof OtherwiseHandler) {
              handler.handleNode(child, defaultSqlFragments);
            }
          }
        }

      }

      private SqlFragment getDefaultSqlFragment(List<SqlFragment> defaultSqlFragments) {
        SqlFragment defaultSqlFragment = null;
        if (defaultSqlFragments.size() == 1) {
          defaultSqlFragment = defaultSqlFragments.get(0);
        } else if (defaultSqlFragments.size() > 1) {
          throw new RuntimeException("Too many default (otherwise) elements in choose statement.");
        }
        return defaultSqlFragment;
      }
    }

    private class WhenHandler implements TagHandler {

      public void handleNode(Node nodeToHandle, List<SqlFragment> targetContents) {
        List<SqlFragment> contents = buildDynamicTag(nodeToHandle);
        MixedSqlFragment mixedSqlFragment = new MixedSqlFragment(contents);
        NamedNodeMap attributes = nodeToHandle.getAttributes();
        Node testAtt = attributes.getNamedItem("test");
        if (testAtt == null) {
          throw new RuntimeException(nodeToHandle.getNodeName() + " must has test attribute ! ");
        }
        String test = testAtt.getTextContent();
        WhenFragment whenSqlFragment = new WhenFragment(mixedSqlFragment, test);
        targetContents.add(whenSqlFragment);
      }
    }
  }

}
