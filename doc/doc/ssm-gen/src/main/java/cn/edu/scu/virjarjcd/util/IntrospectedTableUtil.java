package cn.edu.scu.virjarjcd.util;

import cn.edu.scu.virjarjcd.exception.CodegenException;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class IntrospectedTableUtil {

    public static Set<String> tableRelateImportCalc(IntrospectedTable introspectedTable) {
        Set<String> answer = new TreeSet<String>();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            String javaName = introspectedColumn.getFullyQualifiedJavaType().getShortName();
            if (javaName.startsWith("Set<")) {
                answer.add("java.util.Set");
            } else if (javaName.equals("Date")) {
                answer.add("java.util.Date");
            } else if (javaName.startsWith("Map<")) {
                answer.add("java.util.Map");
            } else if (javaName.startsWith("List<")) {
                answer.add("java.util.List");
            } else if (javaName.equals("BigDecimal")) {
                answer.add("java.math.BigDecimal");
            }
        }
        return answer;
    }

    public static String getPKType(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        if (0 == primaryKeyColumns.size())
            throw new CodegenException("table name is " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " does not have a primary key column");
        if (primaryKeyColumns.size() > 1) {
            throw new CodegenException("codegen does not support the composite primary key");
        }
        return primaryKeyColumns.get(0).getFullyQualifiedJavaType().getShortName();
    }


    public static Method genNoneParameterizedConstructor(TopLevelClass topLevelClass){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        method.addBodyLine("");
        return method;
    }

    public static Method genParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());

        List<Field> fields = topLevelClass.getFields();
        for (Field filed : fields) {
            method.addParameter(new Parameter(filed.getType(), filed.getName()));
        }

        StringBuilder sb = new StringBuilder();


        for (Field filed : fields) {
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(filed.getName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(filed.getName());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
        return method;
    }

}
