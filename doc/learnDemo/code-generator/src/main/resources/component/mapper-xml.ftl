<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperPackage}.${modelNameUpperCamel}Mapper">
    <resultMap id="baseResultMap" type="${modelPackage}.${modelNameUpperCamel}">
        <id column="${columnNameList[0]}" jdbcType="${jdbcTypeList[0]}" property="${fieldNameList[0]}"/>
        <#list 1..(fieldNameList!?size-1) as i>
        <result column="${columnNameList[i]}" jdbcType="${jdbcTypeList[i]}" property="${fieldNameList[i]}"/>
        </#list>
    </resultMap>

</mapper>