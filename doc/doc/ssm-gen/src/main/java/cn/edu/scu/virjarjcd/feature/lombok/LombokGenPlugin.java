package cn.edu.scu.virjarjcd.feature.lombok;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class LombokGenPlugin extends MergePluginAdpter {

	private String lombokAnnotations;
	private boolean data = false;
	private boolean gettersetter = false;
	private boolean tostring = false;
	private boolean allargconstructor = false;
	private boolean noargconstructor = false;



	private void parseparam(){
		String[] params = lombokAnnotations.split(",");
		for(String param:params){
			if(param.trim().equalsIgnoreCase("data")){
				data = true;
			}else if(param.trim().equalsIgnoreCase("gettersetter")){
				gettersetter = true;
			}else if(param.trim().equalsIgnoreCase("tostring")){
				tostring = true;
			}else if(param.trim().equalsIgnoreCase("allargsconstructor")){
				allargconstructor = true;
			}else if(param.trim().equalsIgnoreCase("NoArgsConstructor")){
				noargconstructor = true;
			}
		}

		if(data){
			gettersetter = false;
		}else{
			gettersetter = true;
		}
	}

	@Override
	public boolean modelGetterMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modelSetterMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		if(data){
			if(topLevelClass.getType().getShortName().equals("Data")){
				topLevelClass.addAnnotation("@lombok.Data");
			}else{
				topLevelClass.addAnnotation("@Data");
				topLevelClass.addImportedType("lombok.Data");
			}
		}
		if(allargconstructor){
			topLevelClass.addAnnotation("@AllArgsConstructor");
			topLevelClass.addImportedType("lombok.AllArgsConstructor");
		}
		if(noargconstructor){
			topLevelClass.addAnnotation("@NoArgsConstructor");
			topLevelClass.addImportedType("lombok.NoArgsConstructor");
		}
		if(tostring){
			topLevelClass.addAnnotation("@ToString");
			topLevelClass.addImportedType("lombok.ToString");
		}
		if(gettersetter){
			topLevelClass.addImportedType("lombok.Getter");
			topLevelClass.addImportedType("lombok.Setter");
			for(Field field:topLevelClass.getFields()){
				field.addAnnotation("@Getter");
				field.addAnnotation("@Setter");
			}
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		if(!ConfigHolder.isLombok())
			return false;
		lombokAnnotations = ConfigHolder.instance.getProperty("lombok.annotations","Data");
		parseparam();
		return true;
	}


}
