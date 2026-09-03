package com.gitee.sqlrest.template;

import java.util.HashMap;
import org.junit.Test;

public class TemplateTest {

  @Test
  public void testIf() {
    String content = "select * from user where <if test='id != null ' > id  = #{id} </if>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    map.put("id", "11");
    SqlMeta process = template.process(map);
    System.out.println(process);
  }

  @Test
  public void testWhere() {
    String content = "select * from user <where> <if test='id != null ' > and id  = #{id} </if>  <if test=' name != null' >name =#{name}</if> </where>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    //map.put("name", "join");
    SqlMeta process = template.process(map);
    System.out.println(process);
  }


  @Test
  public void testSet() {
    String content = "update user <set> <if test='id != null '> id = #{id} ,</if><if test='name != null '> name = #{name} ,</if> </set> ";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    map.put("id", "123");
    map.put("name", "join");
    SqlMeta process = template.process(map);
    System.out.println(process);
  }


  @Test
  public void testChoose() {
    String content = "select  * from user <where><choose><when test=' id!= null '> and id = #{id} </when><when test=' name!= null and name.length()>0 '> and name = #{name} </when></choose> </where>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    //map.put("id", "123");
    map.put("name", "join");
    SqlMeta process = template.process(map);
    System.out.println(process);
  }

  @Test
  public void testForEach() {
    String content = "select  * from user <where> id in <foreach item=\"item\" index=\"index\" collection=\"list\"    open=\"(\" separator=\",\" close=\")\">   ${item}   ${index}  </foreach></where>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    map.put("id", "123");
    map.put("name", "join");
		/*
		List<String> arrayList = new ArrayList<>() ;
		arrayList.add("1") ;
		arrayList.add("2") ;
		arrayList.add("3") ;
		arrayList.add("4") ;
		*/
    HashMap<String, Object> map2 = new HashMap<>();
    map2.put("11", "111");
    map2.put("22", "222");
    map.put("list", map2);
    SqlMeta process = template.process(map);
    System.out.println(process);
  }

  @Test
  public void testLT() {
    //String content = "select * from user where <if test='id != null ' > id  &lt; #{id} </if>";
    String content = "select * from user where <if test='id != null ' > id  <![CDATA[ < ]]> #{id} </if>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());

    HashMap<String, Object> map = new HashMap<>();
    map.put("id", "11");
    SqlMeta process = template.process(map);
    System.out.println(process);
  }

  @Test
  public void parseParams() {
    String content = "SELECT count(DISTINCT datasource_uuid,database_name,table_name) \n"
        + "FROM t_test_record \n"
        + "WHERE is_sensitive=#{status} AND is_confirmed='1' AND is_deleted='0' \n"
        + "<choose>\n"
        + "  <when test=\"dsUuids!=null and dsUuids.size ==1 \">\n"
        + "AND datasource_uuid =#{dsUuids[0]} \n"
        + "  </when>\n"
        + "  <when test=\"dsUuids!=null and dsUuids.size > 1 \">\n"
        + "AND datasource_uuid in \n"
        + "    <foreach item='item' collection='dsUuids' separator=',' open='(' close=')' index=''> \n"
        + "     #{item} \n"
        + "    </foreach> \n"
        + "  </when>\n"
        + "</choose>";
    Configuration cfg = new Configuration();
    SqlTemplate template = cfg.getTemplate(content);

    System.out.println(template.getParameterNames());
  }
}
