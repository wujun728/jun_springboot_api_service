package org.ssssssss.script;

import org.junit.Assert;
import org.junit.Test;

import javax.script.*;
import java.util.Map;

public class LanguageTests extends BaseTest{

	@Test
	public void customLanguageTest(){
		MagicResourceLoader.addScriptLanguageLoader((language)->{
			if("custom".equalsIgnoreCase(language)){
				return (context,content)-> "get name is " + context.get("name");
			}
			return null;
		});
		Assert.assertEquals("get name is hello",execute("language/custom.ms"));
	}

	@Test
	public void jsrTest(){
		ScriptEngineManager sem = new ScriptEngineManager();
		MagicResourceLoader.addScriptLanguageLoader((language)->{
			ScriptEngine engine = sem.getEngineByName(language);
			if(engine != null){
				return (context,content)-> {
					try {
						return engine.eval(content,new SimpleBindings(context));
					} catch (ScriptException e) {
						throw new RuntimeException(e);
					}
				};
			}
			return null;
		});
		System.out.println(execute("language/javascript.ms"));
	}
}
