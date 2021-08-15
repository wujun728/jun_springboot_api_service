package cn.edu.scu.virjarjcd.mybatis;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import cn.edu.scu.virjarjcd.exception.CodegenException;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.MybatisXmlElementParser;
import cn.edu.scu.virjarjcd.util.ReflectUtil;

/**
 * merge xmlMapper文件
 * @author virjar
 *
 */
public class XmlMapperMerger {

	private  String xmlPath = ConfigHolder.instance.getProperty("sys.sqlmapperpath");

	private XmlElement resultMapElement;
	private XmlElement baseColumnListElement;
	private XmlElement insertElement;
	private XmlElement insertSelectiveElement;
	private XmlElement updateByPrimarySelectiveElement;
	private XmlElement updateByPrimaryElement;
	private XmlElement selectCountElement;
	private XmlElement selectPageElement;

	private Document document ;

	public  Document UNSerialization(IntrospectedTable introspectedTable) throws FileNotFoundException, Exception{
		if(!xmlPath.endsWith("/")){
			xmlPath = xmlPath +"/";
		}
		String xmlFileName = introspectedTable.getMyBatis3XmlMapperFileName();
		Document answer = new MybatisXmlElementParser().parse(new FileInputStream(xmlPath+xmlFileName));
		locateXmlElement(answer);
		document = answer;
		return answer;
	}

	public Document getDocument(){
		return document;
	}
	
	public void addField(IntrospectedTable introspectedTable,String args[]){
		String modeAndField = args[3];
		String fieldName = modeAndField.split("\\.")[1];
		IntrospectedColumn addedColumn = null;
		for(IntrospectedColumn introspectedColumn:introspectedTable.getAllColumns()){
			if(introspectedColumn.getFullyQualifiedJavaType().getShortName().toLowerCase().equals(fieldName)){
				addedColumn = introspectedColumn;
				break;
			}
		}
		if(addedColumn == null){
			throw new CodegenException("can not find filed "
					+fieldName +" in table "
					+introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()
					+" Please update db before run this command");
		}

		addResultMapField(addedColumn);
		addBaseColumnList(addedColumn);
		addInsert(introspectedTable,addedColumn);
		addInsertSelective(introspectedTable, addedColumn);
		addUpdateByPrimarySelective(introspectedTable, addedColumn);
		addUpdateByPrimary(introspectedTable, addedColumn);
		addSelectCount(introspectedTable, addedColumn);
		addSelectPage(introspectedTable, addedColumn);
	}

	private void addSelectPage(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(selectPageElement == null){
			return;
		}
		String addColumnName = addColumn.getActualColumnName();
		List<Element> elements = selectPageElement.getElements();
		//test
		int param1end =-1;
		boolean findend =false;
		for(Element element:elements){
			if(!findend){
				param1end ++;
			}
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("if")){
					String testAttribute = getAttributeValue(xmlElement, "test");
					if(testAttribute.contains(addColumnName) && testAttribute.contains("param1")){
						System.err.println("field already add build in ,add cation will ignore");
						return;
					}
					if(!testAttribute.contains("param1")){
						findend = true;
					}
				}

			}
		}
		if(param1end == -1){
			System.err.println("can not find field data, bad code structure for selectPage node");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append(new StringBuilder().append("param1.").append(addColumn.getJavaProperty()).toString());
		sb.append(" != null");

		XmlElement insertNotNullElement = new XmlElement("if");
		insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

		sb.setLength(0);
		sb.append("and ");
		sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(addColumn));
		sb.append(" = ");
		sb.append(MyBatis3FormattingUtilities.getParameterClause(addColumn, "param1."));

		insertNotNullElement.addElement(new TextElement(sb.toString()));

		selectPageElement.addElement(param1end, insertNotNullElement);
	}

	private void addSelectCount(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(selectCountElement == null){
			return;
		}
		String addColumnName = addColumn.getActualColumnName();
		XmlElement whereElement = null;
		List<Element> elements = selectCountElement.getElements();
		for(Element element:elements){
			if(element instanceof XmlElement){
				XmlElement xmlExElement = (XmlElement)element;
				if(xmlExElement.getName().equals("where")){
					whereElement = xmlExElement;
					break;
				}
			}
		}
		if(whereElement == null){
			System.err.println("could not find <where> node,add action will ignore");
			return;
		}
		//test
		List<Element> ifElements = whereElement.getElements();
		for(Element element:ifElements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("if") && getAttributeValue(xmlElement, "test").contains(addColumnName)){
					System.err.println("field already add build in ,add cation will ignore");
					return;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append(addColumn.getJavaProperty());
		sb.append(" != null");

		XmlElement insertNotNullElement = new XmlElement("if");
		insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

		sb.setLength(0);
		sb.append("and ");
		sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(addColumn));
		sb.append(" = ");
		sb.append(MyBatis3FormattingUtilities.getParameterClause(addColumn));

		insertNotNullElement.addElement(new TextElement(sb.toString()));
		ifElements.add(insertNotNullElement);
	}

	private void addUpdateByPrimary(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(updateByPrimaryElement == null){
			return;
		}
		String oldSql = trimTextElement(updateByPrimaryElement.getElements());
		String addColumnName = addColumn.getActualColumnName();
		if(oldSql.contains(addColumnName)){
			System.err.println("field already add build in ,add cation will ignore");
			return;
		}

		List<Element> newElements = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		newElements.add(new TextElement(sb.toString()));

		// set up for first column
		sb.setLength(0);
		sb.append("set "); //$NON-NLS-1$

		Matcher matcher = Pattern.compile("\\S+\\s*=\\s*#\\{.+?\\}").matcher(oldSql);

		boolean first = true;
		while(matcher.find()){
			if(!first){
				sb.setLength(0);
				OutputUtilities.xmlIndent(sb, 1);
			}else{
				first = false;
			}
			sb.append(matcher.group());
			sb.append(",");
			newElements.add(new TextElement(sb.toString()));
		}
		sb.setLength(0);
		sb.append(MyBatis3FormattingUtilities
				.getEscapedColumnName(addColumn));
		sb.append(" = "); //$NON-NLS-1$
		sb.append(MyBatis3FormattingUtilities
				.getParameterClause(addColumn));
		newElements.add(new TextElement(sb.toString()));

		int indexOf = oldSql.indexOf("where");
		newElements.add(new TextElement(oldSql.substring(indexOf)));

		try {
			ReflectUtil.addField(updateByPrimaryElement, "elements", newElements);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addUpdateByPrimarySelective(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(updateByPrimarySelectiveElement == null){
			return;//ignore
		}
		String addColumnName = addColumn.getActualColumnName();
		XmlElement setElement = null;
		List<Element> elements = updateByPrimarySelectiveElement.getElements();
		for(Element element:elements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("set")){
					setElement = xmlElement;
					break;
				}
			}
		}

		if(setElement == null){
			System.err.println("can not find set element,add action will ignore");
			return;
		}

		//test if already add data
		List<Element> testElements = setElement.getElements();
		for(Element element:testElements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("if") && getAttributeValue(xmlElement, "test").startsWith(addColumnName)){
					System.err.println("base_column_list already add field,insert action will ignore");
					return;
				}
			}
		}

		//append data
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		sb.append(addColumn.getJavaProperty());
		sb.append(" != null"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		setElement.addElement(isNotNullElement);

		sb.setLength(0);
		sb.append(MyBatis3FormattingUtilities
				.getEscapedColumnName(addColumn));
		sb.append(" = "); //$NON-NLS-1$
		sb.append(MyBatis3FormattingUtilities
				.getParameterClause(addColumn));
		sb.append(',');

		isNotNullElement.addElement(new TextElement(sb.toString()));

	}

	private void addInsertSelective(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(insertSelectiveElement == null){
			return;//ignore
		}
		String addColumnName = addColumn.getActualColumnName();
		XmlElement insertTrimElement = null;
		XmlElement valueTrimElement = null;
		List<Element> elements = insertSelectiveElement.getElements();
		for(Element element:elements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("trim") && getAttributeValue(xmlElement, "prefix").equals("(")){
					insertTrimElement = xmlElement;
				}else if(xmlElement.getName().equals("trim") && getAttributeValue(xmlElement, "prefix").equals("values (")){
					valueTrimElement = xmlElement;
				}
			}
		}
		if(insertTrimElement == null || valueTrimElement == null){
			System.err.println("bad node form insertSelect");
			return;
		}
		//test if already add data
		List<Element> ifTestElements = insertTrimElement.getElements();
		for(Element element:ifTestElements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals("if") && getAttributeValue(xmlElement, "test").contains(addColumnName)){
					System.err.println("base_column_list already add field,insert action will ignore");
					return;
				}
			}
		}


		XmlElement insertNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		sb.append(addColumn.getJavaProperty());
		sb.append(" != null"); //$NON-NLS-1$
		insertNotNullElement.addAttribute(new Attribute(
				"test", sb.toString())); //$NON-NLS-1$

		sb.setLength(0);
		sb.append(MyBatis3FormattingUtilities
				.getEscapedColumnName(addColumn));
		sb.append(',');
		insertNotNullElement.addElement(new TextElement(sb.toString()));
		insertTrimElement.addElement(insertNotNullElement);


		XmlElement valuesNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append(addColumn.getJavaProperty());
		sb.append(" != null"); //$NON-NLS-1$
		valuesNotNullElement.addAttribute(new Attribute(
				"test", sb.toString())); //$NON-NLS-1$

		sb.setLength(0);
		sb.append(MyBatis3FormattingUtilities
				.getParameterClause(addColumn));
		sb.append(',');
		valuesNotNullElement.addElement(new TextElement(sb.toString()));
		valueTrimElement.addElement(valuesNotNullElement);

	}

	private void addInsert(IntrospectedTable introspectedTable,IntrospectedColumn addColumn){
		if(insertElement == null){
			return;//ignore
		}
		String sql = trimTextElement(insertElement.getElements());
		String addColumnName = addColumn.getActualColumnName();
		if(sql.contains(addColumnName)){
			System.err.println("base_column_list already add field,insert action will ignore");
			return;
		}
		List<String> insertColumn = new ArrayList<>();
		List<String> valueColumn = new ArrayList<>();
		String[] insertColumnArray = cn.edu.scu.virjarjcd.util.StringUtils.cutBy(sql, '(', ')', 1).split(",");
		for(String column:insertColumnArray){
			insertColumn.add(column);
		}

		String[] valueColumnArray = cn.edu.scu.virjarjcd.util.StringUtils.cutBy(sql, '(', ')', 2).split(",");
		for(String column:valueColumnArray){
			valueColumn.add(column);
		}


		StringBuilder insertClause = new StringBuilder();
		StringBuilder valuesClause = new StringBuilder();

		insertClause.append("insert into "); //$NON-NLS-1$
		insertClause.append(introspectedTable
				.getFullyQualifiedTableNameAtRuntime());
		insertClause.append(" ("); //$NON-NLS-1$

		valuesClause.append("values ("); //$NON-NLS-1$

		List<String> valuesClauses = new ArrayList<String>();
		List<String> insertsCaluses = new ArrayList<>();

		for(int i=0;i<valueColumn.size();i++){
			insertClause.append(insertColumn.get(i));
			insertClause.append(",");

			valuesClause.append(valueColumn.get(i));
			valuesClause.append(",");

			if(insertClause.length() > 80){
				insertsCaluses.add(insertClause.toString());
				insertClause.setLength(0);
				OutputUtilities.xmlIndent(insertClause, 1);

				valuesClauses.add(valuesClause.toString());
				valuesClause.setLength(0);
				OutputUtilities.xmlIndent(valuesClause, 1);
			}
		}
		insertClause.append(MyBatis3FormattingUtilities
				.getEscapedColumnName(addColumn));
		valuesClause.append(MyBatis3FormattingUtilities
				.getParameterClause(addColumn));

		insertClause.append(')');
		insertsCaluses.add(insertClause.toString());
		valuesClause.append(')');
		valuesClauses.add(valuesClause.toString());

		List<TextElement> newTextElements = new ArrayList<>();
		for(String insert:insertsCaluses){
			newTextElements.add(new TextElement(insert));
		}
		for(String value:valuesClauses){
			newTextElements.add(new TextElement(value));
		}
		try {
			ReflectUtil.addField(insertElement, "elements", newTextElements);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private String trimTextElement(List<Element> elements){
		List<TextElement> textElements = new ArrayList<TextElement>();
		for(Element element:elements){
			if(element instanceof TextElement){
				//textElement = (TextElement) element;
				//break;
				textElements.add((TextElement)element);
			}
		}
		if(textElements.size() == 0){
			System.err.println("can not find value for baseColumnlist element");
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(TextElement textElement : textElements){
			sb.append(StringUtils.trimToEmpty(textElement.getContent()));
			sb.append(" ");
		}

		return sb.toString();
	}

	private void addBaseColumnList(IntrospectedColumn addColumn){
		if(baseColumnListElement == null){
			return;//ignore
		}
		List<Element> elements = baseColumnListElement.getElements();
		String columns = trimTextElement(elements);


		String columName = addColumn.getActualColumnName();//工具不支持对字段取别名的方式
		if(columns.contains(columName)){
			System.err.println("base_column_list already add field,insert action will ignore");
			return;
		}

		List<TextElement> newTextElements = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		String[] split = columns.split(",");
		for(int i=0;i<split.length ;i++){
			sb.append(split[i]);
			sb.append(",");
			if(sb.length() > 80){
				newTextElements.add(new TextElement(sb.toString()));
				sb.setLength(0);
			}
		}
		sb.append(addColumn.getActualColumnName());
		newTextElements.add(new TextElement(sb.toString()));
		try {
			ReflectUtil.addField(baseColumnListElement, "elements", newTextElements);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private XmlElement getElementByAttribute(XmlElement root,String name,String key,String value){
		List<Element> elements = root.getElements();
		for(Element element:elements){
			if(element instanceof XmlElement){
				XmlElement xmlElement = (XmlElement) element;
				if(xmlElement.getName().equals(name)){
					List<Attribute> attributes = xmlElement.getAttributes();
					for(Attribute attribute:attributes){
						if(attribute.getName().equals(key)&&attribute.getValue().equals(value)){
							return xmlElement;
						}
					}
				}
			}
		}
		return null;
	}

	private void addResultMapField(IntrospectedColumn addColumn){
		if(resultMapElement == null){
			return;//ignore
		}

		//test if element already set in resultMap
		if(null != getElementByAttribute(resultMapElement, "result", "property", addColumn.getJavaProperty())){
			return;//ignore,because the column is add in resultMap
		}

		XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

		resultElement
		.addAttribute(new Attribute(
				"column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(addColumn))); //$NON-NLS-1$
		resultElement.addAttribute(new Attribute(
				"property", addColumn.getJavaProperty())); //$NON-NLS-1$
		resultElement.addAttribute(new Attribute(
				"jdbcType", addColumn.getJdbcTypeName())); //$NON-NLS-1$

		if (stringHasValue(addColumn
				.getTypeHandler())) {
			resultElement.addAttribute(new Attribute(
					"typeHandler", addColumn.getTypeHandler())); //$NON-NLS-1$
		}

		resultMapElement.addElement(resultElement);
	}

	private void locateXmlElement(Document document){
		XmlElement rootElement = document.getRootElement();
		if(rootElement == null){
			throw new CodegenException("document invalidate,rootElement is null");
		}
		List<Element> elements = rootElement.getElements();
		for(Element element:elements){
			if(element instanceof XmlElement){
				recogenizeElement((XmlElement) element);
			}
		}
	}

	private String getAttributeValue(XmlElement xmlElement,String key){
		List<Attribute> attributes = xmlElement.getAttributes();
		for(Attribute attribute:attributes){
			if(attribute.getName().equals(key)){
				return attribute.getValue();
			}
		}
		return "";
	}

	private void recogenizeElement(XmlElement xmlElement){
		String name = xmlElement.getName();
		if("resultMap".equals(name) && getAttributeValue(xmlElement,"id").equals("BaseResultMap")){
			resultMapElement = xmlElement;
		}else if("sql".equals(name) && getAttributeValue(xmlElement, "id").equals("Base_Column_List")){
			baseColumnListElement = xmlElement;
		}else if("insert".equals(name) && getAttributeValue(xmlElement, "id").equals("insert")){
			insertElement = xmlElement;
		}else if("insert".equals(name) && getAttributeValue(xmlElement, "id").equals("insertSelective")){
			insertSelectiveElement = xmlElement;
		}else if("update".equals(name) && getAttributeValue(xmlElement, "id").equals("updateByPrimaryKeySelective")){
			updateByPrimarySelectiveElement = xmlElement;
		}else if("update".equals(name) && getAttributeValue(xmlElement, "id").equals("updateByPrimaryKey")){
			updateByPrimaryElement = xmlElement;
		}else if("select".equals(name) && getAttributeValue(xmlElement, "id").equals("selectCount")){
			selectCountElement = xmlElement;
		}else if("select".equals(name) && getAttributeValue(xmlElement, "id").equals("selectPage")){
			selectPageElement = xmlElement;
		}else{
			//do nothing
		}

	}

}
