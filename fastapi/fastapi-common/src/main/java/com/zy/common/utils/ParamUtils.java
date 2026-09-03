package com.zy.common.utils;

import com.zy.common.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 参数处理工具类
 * @author javachen
 */
public class ParamUtils
{

    /**
     * 将request中的请求,以map格式返回
     * @return
     */
    public static Map<String, Object> getRequestParamMap(HttpServletRequest request){
        Map<String, Object> requestParamMap = new HashMap<>();
        String paramMapStr = request.getQueryString();

        if(StringUtils.isNotEmpty(paramMapStr)){
            requestParamMap = UrlUtils.urlTpMap(paramMapStr);
        }
        return requestParamMap;
    }

    /**
     * 将所有map里的参数以key=value,的形式进行拼接
     * @param inputParamMap
     * @return String
     */
    public static String getParamsStrByMap(Map<String, Object> inputParamMap){

        String str = "";

        for (Map.Entry<String, Object> entry : inputParamMap.entrySet()) {
            str=str+entry.getKey()+"="+entry.getValue()+",";
        }

        if(str.endsWith(",")){
            str = str.substring(0,str.length()-1);
        }

        return str;

    }


    /**
     * 得到sql语句中所有占位参数
     * 输入select * from sys_user where [`user_name={userName}`]  [ and `dept_id={deptId}`]
     * 输出deptId:null
     *    userName:null
     * @return
     */
    public static Map<String, Object> getAllArgsParamMap(String ...sqlStr){

        Map<String, Object> resultMap = new HashMap<>();

        Pattern p = Pattern.compile("\\{(.*?)\\}");
        for(String sql : sqlStr) {
            if(StringUtils.isNotEmpty(sql)){
                Matcher m = p.matcher(sql);
                while(m.find()) {
                    resultMap.put(m.group(1)+"",null);
                }
            }
        }

        return resultMap;
    }

    /**
     * 得到sql语句中分隔符内容
     * 输入select * from sys_user where [`user_name={userName}`]  [ and `dept_id={deptId}`]
     * 输出deptId:null
     *    userName:null
     * @return
     */
    public static List<String> getRegexSpList(String str) {

        List<String> matchList = new ArrayList<String>();
        try {
            Pattern regex = Pattern.compile("\\[`(.*?)\\`]");
            Matcher regexMatcher = regex.matcher(str);
            while (regexMatcher.find()) {
                matchList.add(regexMatcher.group(0));
            }
        } catch (PatternSyntaxException ex) {
            throw ex;
        }

        return matchList;

    }

    /**
     * 替换参数
     * select count(1) from sys_role where 1=1 [`and role_id={roleId}`]
     * 替换为
     * select count(1) from sys_role where 1=1 and role_id=1439064087072911400
     * @return
     */
    public static String replaceParam(String sqlStr,String sqlPlaceHolder, String paramKey,Object paramValue){

        if(null==paramValue||(paramValue+"").equals("")){
            return sqlStr.replaceAll(escapeQueryChars(sqlPlaceHolder), "");
        }

        String beforeSqlPlaceHolder = sqlPlaceHolder;

        String afterSqlPlaceHolder = sqlPlaceHolder.replaceAll("\\[`", "").replaceAll("`]", "");
        afterSqlPlaceHolder = sqlPlaceHolderReplact( afterSqlPlaceHolder, paramKey, paramValue);

        return sqlStr.replaceAll(escapeQueryChars(beforeSqlPlaceHolder), escapeQueryChars(afterSqlPlaceHolder));

    }

    public static String sqlPlaceHolderReplact(String afterSqlPlaceHolder,String paramKey,Object paramValue){
        String handleParamKey = "";
        Object handleParamValue = "";
        if(afterSqlPlaceHolder.contains("%{"+paramKey+"}%")){
            handleParamKey = "%\\{"+paramKey+"}%";
            handleParamValue = "'%"+paramValue+"%'";
        }else
        if(afterSqlPlaceHolder.contains("%{"+paramKey+"}")){
            handleParamKey = "%\\{"+paramKey+"}";
            handleParamValue = "'%"+paramValue+"'";;
        } else
        if(afterSqlPlaceHolder.contains("{"+paramKey+"}%")){
            handleParamKey = "\\{"+paramKey+"}%";
            handleParamValue = "'"+paramValue+"%'";;
        }else
        if(afterSqlPlaceHolder.contains("{"+paramKey+"}")){
            handleParamKey = "\\{"+paramKey+"}";
            if (paramValue instanceof String) {
                handleParamValue = "'"+paramValue+"'";
            }else{
                handleParamValue = paramValue;
            }
        }
        return afterSqlPlaceHolder.replaceAll(  handleParamKey, handleParamValue+" ");
    }
    /**
     * @param s 需要转义的字符串
     * @return 返回转义后的字符串
     */
    public static String escapeQueryChars(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        //查询字符串一般不会太长，挨个遍历也花费不了多少时间
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
                    || c == ':' || c == '^'	|| c == '[' || c == ']' || c == '\"'
                    || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
                    || c == '|' || c == '&' || c == ';' || c == '/' || c == '.'
                    || c == '$' || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "and dept_name like %{deptName}";
        str = str.replaceAll( "%\\{deptName}", "'%123%' ");
        System.out.println(str);
    }
}
