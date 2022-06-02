package ${basePackage}.domains.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
* Title
* Author ${author}
* DateTime  ${date}.
* Version V1.0.0
*/
@Table(name = "${tableName}")
@Data
public class ${modelNameUpperCamel} implements Serializable{

    /**
     * ${columnComments[0]}
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ${fieldTypeList[0]} ${fieldNameList[0]!};

    <#list 1..(fieldNameList!?size-1) as i>
    /**
     * ${columnComments[i]}
     */
        <#if fieldNameList[i] != columnNameList[i]>
    @Column(name = "${columnNameList[i]}")
        </#if>
    private ${fieldTypeList[i]} ${fieldNameList[i]!};

    </#list>
}
