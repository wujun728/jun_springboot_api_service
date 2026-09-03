package com.gitee.sqlrest.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitee.sqlrest.core.util.JacksonUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

public class JacksonUtilsTest {

  @Test
  public void testToJsonStr001() {
    Map<String, Object> result = new HashMap<>();
    result.put("user_name", "test");
    result.put("user_age", 23);
    result.put("user_sex", 1);
    String json = JacksonUtils.toJsonStr(result, Collections.emptyMap());
    System.out.println(json);
  }

  @Test
  public void testJsonStrToMap() throws JsonProcessingException {
    String jsonString = "{\"user_sex\":1,\"user_name\":\"test\",\"user_age\":23}";
    Map<String, Object> resultMap = new HashMap<>();

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(jsonString);
    Iterator<Entry<String, JsonNode>> fields = rootNode.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> entry = fields.next();
      JsonNode jsonNode = entry.getValue();
      Object value = mapper.convertValue(jsonNode, Object.class);
      resultMap.put(entry.getKey(), value);
    }
    System.out.println(resultMap);
  }
}
