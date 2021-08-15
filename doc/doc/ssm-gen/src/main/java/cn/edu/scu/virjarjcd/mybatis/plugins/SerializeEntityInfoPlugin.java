package cn.edu.scu.virjarjcd.mybatis.plugins;

//import com.virjar.pylon.codegen.exception.CodegenException;
//import com.virjar.pylon.codegen.metadata.EntityInfo;
//import com.virjar.pylon.codegen.metadata.FieldInfo;

//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;
import java.util.List;

//import org.mybatis.generator.api.IntrospectedColumn;
//import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
//import org.mybatis.generator.api.dom.java.TopLevelClass;

public class SerializeEntityInfoPlugin extends PluginAdapter
{
  public boolean validate(List<String> warnings)
  {
    return true;
  }

//  public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
//  {
//    EntityInfo entityInfo = new EntityInfo();
//    entityInfo.setName(topLevelClass.getType().getShortName());
//    List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
//    if (0 == primaryKeyColumns.size())
//      throw new CodegenException("table name is " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " does not have a primary key column");
//    if (primaryKeyColumns.size() > 1) {
//      throw new CodegenException("codegen does not support the composite primary key");
//    }
//    entityInfo.setPk(((IntrospectedColumn)primaryKeyColumns.get(0)).getJavaProperty());
//
//    for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
//      FieldInfo fieldInfo = new FieldInfo();
//      fieldInfo.setName(introspectedColumn.getJavaProperty());
//      fieldInfo.setSize(introspectedColumn.getLength());
//      fieldInfo.setScale(introspectedColumn.getScale());
//      fieldInfo.setType(introspectedColumn.getFullyQualifiedJavaType().getShortName());
//
//      entityInfo.addFieldInfo(fieldInfo);
//    }
//
//    serialization(entityInfo);
//
//    return true;
//  }
//
//  private void serialization(EntityInfo entityInfo) {
//    try {
//      FileOutputStream fos = new FileOutputStream("./codegen_" + entityInfo.getName() + ".tmp");
//      ObjectOutputStream oos = new ObjectOutputStream(fos);
//      oos.writeObject(entityInfo);
//      oos.flush();
//      oos.close();
//      fos.close();
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//  }
}
