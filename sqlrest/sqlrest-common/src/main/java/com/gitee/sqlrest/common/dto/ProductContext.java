package com.gitee.sqlrest.common.dto;

import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductContext {

  private int id;
  private String quote;
  private String name;
  private String driver;
  private int defaultPort;
  private String testSql;
  private String urlPrefix;
  private String[] tplUrls;
  private String urlSample;
  private String sqlSchemaList;
  private List<String> retSchemaList;
  private boolean hasCatalogAndSchema;
  private Function<String, Pair<String, String>> adapter;
  private String pageSql;
}
