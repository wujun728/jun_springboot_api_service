package ${basePackage}.domains.condition;

import ${basePackage}.web.page.Pager;
import lombok.Data;

import java.io.Serializable;


/**
* Title
* Author ${author}
* DateTime  ${date}.
* Version V1.0.0
*/
@Data
public class ${modelNameUpperCamel}Condition extends Pager implements Serializable{

    <#list 0..(fieldNameList!?size-1) as i>
    /**
     * ${columnComments[i]}
     */
    private ${fieldTypeList[i]} ${fieldNameList[i]!};

    </#list>
}
