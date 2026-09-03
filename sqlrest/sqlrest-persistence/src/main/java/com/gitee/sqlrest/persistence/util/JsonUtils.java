package com.gitee.sqlrest.persistence.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtils {

  private static ObjectMapper jacksonMapper = new ObjectMapper();

  public static String toJsonString(Object object) {
    if (Objects.nonNull(object)) {
      try {
        return jacksonMapper.writeValueAsString(object);
      } catch (JsonProcessingException e) {
        log.error("Convert object to json string error：{}", object.toString(), e);
        throw new RuntimeException("convert object to json string error:" + e.getMessage());
      }
    }
    return null;
  }

  public static <T> T toBeanObject(String jsonString, Class<T> clazz) {
    if (null != jsonString) {
      try {
        return jacksonMapper.readValue(jsonString, clazz);
      } catch (JsonProcessingException e) {
        String className = clazz.getSimpleName();
        log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        throw new RuntimeException("parse json string to object error:" + e.getMessage());
      }
    }
    return null;
  }

  public static <T> T toBeanType(String jsonString, TypeReference<T> clazz) {
    if (null != jsonString) {
      try {
        return jacksonMapper.readValue(jsonString, clazz);
      } catch (JsonProcessingException e) {
        String className = clazz.getType().getTypeName();
        log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        throw new RuntimeException("parse json string to object error:" + e.getMessage());
      }
    }
    return null;
  }

  public static <T> List<T> toBeanList(String jsonString, Class<T> clazz) {
    if (null != jsonString) {
      try {
        return jacksonMapper.readValue(jsonString, getCollectionType(List.class, clazz));
      } catch (JsonProcessingException e) {
        String className = clazz.getSimpleName();
        log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        throw new RuntimeException("parse json string to list error:" + e.getMessage());
      }
    }
    return Collections.emptyList();
  }

  private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return jacksonMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }
}
