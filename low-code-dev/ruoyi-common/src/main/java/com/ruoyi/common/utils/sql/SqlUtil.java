package com.ruoyi.common.utils.sql;

import com.ruoyi.common.exception.UtilException;
import com.ruoyi.common.utils.StringUtils;

/**
 * sql操作工具类
 *
 * @author ruoyi
 */
public class SqlUtil
{
    /**
     * 数据类型转化JAVA
     *
     * @param sqlType：类型名称
     * @return
     */
    public static String toSqlToJava(String sqlType) {
        if (sqlType == null || sqlType.trim().length() == 0) return sqlType;
        sqlType = sqlType.split("\\(")[0].toLowerCase();
        switch (sqlType) {
            case "nvarchar":
            case "char":
            case "varchar":
            case "text":
            case "nchar":
                return "String";
            case "blob":
            case "image":
                return "byte[]";
            case "integer":
            case "id":
                return "Long";
            case "tinyint":
            case "smallint":
            case "mediumint":
                return "Integer";
            case "bit":
            case "boolean":
                return "Boolean";
            case "bigint":
                return "java.math.BigInteger";
            case "float":
                return "Fload";
            case "double":
            case "money":
            case "smallmoney":
                return "Double";
            case "decimal":
            case "numeric":
            case "real":
                return "java.math.BigDecimal";
            case "date":
            case "datetime":
            case "year":
                return "java.util.Date";
            case "time":
                return "java.sql.Time";
            case "timestamp":
                return "java.sql.Timestamp";
            default:
                System.out.println("-----------------》转化失败：未发现的类型" + sqlType);
                break;
        }
        return sqlType;
    }
    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |+|user()";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords)
        {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1)
            {
                throw new UtilException("参数存在SQL注入风险");
            }
        }
    }
}
