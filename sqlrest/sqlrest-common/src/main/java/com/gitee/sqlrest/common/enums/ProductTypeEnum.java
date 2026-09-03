package com.gitee.sqlrest.common.enums;

import com.gitee.sqlrest.common.dto.ProductContext;
import java.util.Arrays;
import java.util.Collections;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@Getter
public enum ProductTypeEnum {

  /**
   * MySQL数据库类型
   */
  MYSQL(
      ProductContext.builder()
          .id(1)
          .quote("`")
          .name("mysql")
          .driver("com.mysql.jdbc.Driver")
          .defaultPort(3306)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:mysql://")
          .tplUrls(new String[]{"jdbc:mysql://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample(
              "jdbc:mysql://172.17.2.10:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&tinyInt1isBit=false&rewriteBatchedStatements=true&useCompression=true")
          .sqlSchemaList("SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`")
          .adapter(database -> Pair.of(database, null))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),
  /**
   * MariaDB数据库类型
   */
  MARIADB(
      ProductContext.builder()
          .id(2)
          .quote("`")
          .name("mariadb")
          .driver("org.mariadb.jdbc.Driver")
          .defaultPort(3306)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:mariadb://")
          .tplUrls(new String[]{"jdbc:mariadb://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample(
              "jdbc:mariadb://172.17.2.10:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&tinyInt1isBit=false&rewriteBatchedStatements=true&useCompression=true")
          .sqlSchemaList("SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`")
          .adapter(database -> Pair.of(database, null))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),
  /**
   * Oracle数据库类型
   */
  ORACLE(
      ProductContext.builder()
          .id(3)
          .quote("\"")
          .name("oracle")
          .driver("oracle.jdbc.driver.OracleDriver")
          .defaultPort(1521)
          .testSql("SELECT 'Hello' from DUAL")
          .urlPrefix("jdbc:oracle:thin:@")
          .tplUrls(new String[]{"jdbc:oracle:thin:@{host}:{port}:{database}",
              "jdbc:oracle:thin:@//{host}[:{port}]/{database}"})
          .urlSample("jdbc:oracle:thin:@172.17.2.10:1521:ORCL")
          .sqlSchemaList("SELECT USERNAME FROM SYS.ALL_USERS")
          .adapter(database -> Pair.of(null, database))
          .pageSql("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( %s ) ALIAS WHERE ROWNUM <= ? ) WHERE ROW_ID > ?")
          .build()),
  /**
   * Microsoft SQL Server数据库类型(>=2005)
   */
  SQLSERVER(
      ProductContext.builder()
          .id(4)
          .quote("\"")
          .name("sqlserver")
          .driver("com.microsoft.sqlserver.jdbc.SQLServerDriver")
          .defaultPort(1433)
          .testSql("SELECT 1+2 as a")
          .urlPrefix("jdbc:sqlserver://")
          .tplUrls(new String[]{"jdbc:sqlserver://{host}[:{port}][;DatabaseName={database}][;{params}]"})
          .urlSample("jdbc:sqlserver://172.17.2.10:1433;DatabaseName=test")
          .sqlSchemaList("select schema_name from INFORMATION_SCHEMA.SCHEMATA")
          .hasCatalogAndSchema(true)
          .adapter(database -> Pair.of(null, database))
          .pageSql("SELECT * FROM (%s) ALIAS OFFSET ? ROWS FETCH NEXT ? ROWS ONLY")
          .build()),

  /**
   * PostgreSQL数据库类型
   */
  POSTGRESQL(
      ProductContext.builder()
          .id(5)
          .quote("\"")
          .name("postgresql")
          .driver("org.postgresql.Driver")
          .defaultPort(5432)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:postgresql://")
          .tplUrls(new String[]{"jdbc:postgresql://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:postgresql://172.17.2.10:5432/test")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * DB2数据库类型
   */
  DB2(
      ProductContext.builder()
          .id(6)
          .quote("\"")
          .name("db2")
          .driver("com.ibm.db2.jcc.DB2Driver")
          .defaultPort(50000)
          .testSql("SELECT 1 FROM SYSIBM.SYSDUMMY1")
          .urlPrefix("jdbc:db2://")
          .tplUrls(new String[]{"jdbc:db2://{host}:{port}/{database}[:{params}]"})
          .urlSample(
              "jdbc:db2://172.17.2.10:50000/testdb:driverType=4;fullyMaterializeLobData=true;fullyMaterializeInputStreams=true;progressiveStreaming=2;progresssiveLocators=2;")
          .sqlSchemaList("SELECT SCHEMANAME FROM SYSCAT.SCHEMATA ")
          .adapter(database -> Pair.of(null, database))
          .pageSql(
              "SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( %s ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN ? AND ?")
          .build()),

  /**
   * [国产] 达梦(DM)数据库类型
   */
  DM(
      ProductContext.builder()
          .id(7)
          .quote("\"")
          .name("dm")
          .driver("dm.jdbc.driver.DmDriver")
          .defaultPort(5236)
          .testSql("SELECT 'Hello' from DUAL")
          .urlPrefix("jdbc:dm://")
          .tplUrls(new String[]{"jdbc:dm://{host}:{port}[/{database}][\\\\?{params}]"})
          .urlSample("jdbc:dm://172.17.2.10:5236")
          .sqlSchemaList("SELECT DISTINCT object_name FROM ALL_OBJECTS WHERE OBJECT_TYPE = 'SCH'")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * [国产] 金仓(Kingbase)数据库类型
   */
  KINGBASE(
      ProductContext.builder()
          .id(8)
          .quote("\"")
          .name("kingbase")
          .driver("com.kingbase8.Driver")
          .defaultPort(54321)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:kingbase8://")
          .tplUrls(new String[]{"jdbc:kingbase8://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:kingbase8://172.17.2.10:54321/test")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * [国产] 神通(Oscar)数据库类型
   */
  OSCAR(
      ProductContext.builder()
          .id(9)
          .quote("\"")
          .name("oscar")
          .driver("com.oscar.Driver")
          .defaultPort(2003)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:oscar://")
          .tplUrls(new String[]{"jdbc:oscar://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:oscar://172.17.2.10:2003/OSCRDB")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * [国产] 南大通用(GBase8A)数据库类型
   */
  GBASE8A(
      ProductContext.builder()
          .id(10)
          .quote("`")
          .name("gbase8a")
          .driver("com.gbase.jdbc.Driver")
          .defaultPort(5258)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:gbase://")
          .tplUrls(new String[]{"jdbc:gbase://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:gbase://172.17.2.10:5258/test")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * Sybase 数据库类型
   */
  SYBASE(
      ProductContext.builder()
          .id(11)
          .quote("\"")
          .name("sybase")
          .driver("com.sybase.jdbc4.jdbc.SybDriver")
          .defaultPort(5000)
          .testSql("SELECT 1+2 as a")
          .urlPrefix("jdbc:sybase:Tds:")
          .tplUrls(new String[]{"jdbc:sybase:Tds:{host}[:{port}][/{database}][\\?{params}]"})
          .urlSample("jdbc:sybase:Tds:172.17.2.10:5000/test?charset=cp936")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("SELECT * FROM (%s) ALIAS OFFSET ? ROWS FETCH NEXT ? ROWS ONLY")
          .build()),

  /**
   * Hive 数据库类型
   */
  HIVE(
      ProductContext.builder()
          .id(12)
          .quote("`")
          .name("hive")
          .driver("org.apache.hive.jdbc.HiveDriver")
          .defaultPort(10000)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:hive2://")
          .tplUrls(new String[]{"jdbc:hive2://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:hive2://172.17.2.12:10000/default")
          .sqlSchemaList("SHOW DATABASES")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),

  /**
   * Sqlite v3数据库类型
   */
  // 参考文章：https://blog.csdn.net/wank1259162/article/details/104946744
  SQLITE3(
      ProductContext.builder()
          .id(13)
          .quote("\"")
          .name("sqlite3")
          .driver("org.sqlite.JDBC")
          .defaultPort(0)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:sqlite:")
          .tplUrls(new String[]{"jdbc:sqlite:{file}", "jdbc:sqlite::resource:{file}"})
          .urlSample("jdbc:sqlite:/tmp/test.db")
          .sqlSchemaList(null)
          .retSchemaList(Collections.singletonList("sqlite_master"))
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * OpenGauss数据库类型
   */
  OPENGAUSS(
      ProductContext.builder()
          .id(14)
          .quote("\"")
          .name("opengauss")
          .driver("org.opengauss.Driver")
          .defaultPort(15432)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:opengauss://")
          .tplUrls(new String[]{"jdbc:opengauss://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:opengauss://172.17.2.10:5432/test")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? offset ? ")
          .build()),

  /**
   * ClickHouse数据库类型
   */
  CLICKHOUSE(
      ProductContext.builder()
          .id(15)
          .quote("`")
          .name("clickhouse")
          .driver("com.clickhouse.jdbc.ClickHouseDriver")
          .defaultPort(8123)
          .testSql("SELECT 1")
          .urlPrefix("jdbc:clickhouse://")
          .tplUrls(new String[]{"jdbc:clickhouse://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample("jdbc:clickhouse://172.17.2.10:8123/default")
          .sqlSchemaList("SELECT schema_name FROM information_schema.schemata ")
          .adapter(database -> Pair.of(null, database))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),

  /**
   * Doris数据库类型
   */
  DORIS(
      ProductContext.builder()
          .id(16)
          .quote("`")
          .name("doris")
          .driver("com.mysql.jdbc.Driver")
          .defaultPort(3306)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:mysql://")
          .tplUrls(new String[]{"jdbc:mysql://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample(
              "jdbc:mysql://172.17.2.10:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&tinyInt1isBit=false&rewriteBatchedStatements=true&useCompression=true")
          .sqlSchemaList("SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`")
          .adapter(database -> Pair.of(database, null))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),

  /**
   * StarRocks数据库类型
   */
  STARROCKS(
      ProductContext.builder()
          .id(17)
          .quote("`")
          .name("starrocks")
          .driver("com.mysql.jdbc.Driver")
          .defaultPort(3306)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:mysql://")
          .tplUrls(new String[]{"jdbc:mysql://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample(
              "jdbc:mysql://172.17.2.10:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&tinyInt1isBit=false&rewriteBatchedStatements=true&useCompression=true")
          .sqlSchemaList("SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`")
          .adapter(database -> Pair.of(database, null))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),

  /**
   * OceanBase数据库类型
   */
  OCEANBASE(
      ProductContext.builder()
          .id(18)
          .quote("`")
          .name("oceanbase")
          .driver("com.oceanbase.jdbc.Driver")
          .defaultPort(2881)
          .testSql("/* ping */ SELECT 1")
          .urlPrefix("jdbc:oceanbase://")
          .tplUrls(new String[]{"jdbc:oceanbase://{host}[:{port}]/[{database}][\\?{params}]"})
          .urlSample(
              "jdbc:oceanbase://127.0.0.1:2881/test?pool=false&useUnicode=true&characterEncoding=utf-8&useSSL=false")
          .sqlSchemaList("SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`")
          .adapter(database -> Pair.of(database, null))
          .pageSql("select * from (%s) alias limit ? OFFSET ? ")
          .build()),
  ;

  private ProductContext context;

  ProductTypeEnum(ProductContext context) {
    this.context = context;
  }

  public int getId() {
    return this.context.getId();
  }

  public String getName() {
    return this.context.getName();
  }

  public String getDriver() {
    return this.context.getDriver();
  }

  public String getUrlPrefix() {
    return this.context.getUrlPrefix();
  }

  public String[] getUrl() {
    return this.context.getTplUrls();
  }

  public String getTestSql() {
    return this.context.getTestSql();
  }

  public String getSample() {
    return this.context.getUrlSample();
  }

  public String getSql() {
    return this.context.getTestSql();
  }

  public boolean hasDatabaseName() {
    return !Arrays.asList(DM, SQLITE3, MYSQL, MARIADB, GBASE8A).contains(this);
  }

  public boolean hasFilePath() {
    return this == SQLITE3;
  }

  public boolean hasAddress() {
    return this != SQLITE3;
  }

  public String quoteName(String name) {
    return String.format("%s%s%s", context.getQuote(), name, context.getQuote());
  }

  public String quoteSchemaTableName(String schema, String table) {
    String quote = context.getQuote();
    return String.format("%s%s%s.%s%s%s", quote, schema, quote, quote, table, quote);
  }

  public String getPageSql(String sql) {
    return String.format(context.getPageSql(), sql);
  }

  /**
   * 是否存在指定字符串名称的数据库类型
   *
   * @param name 字符串名称
   * @return boolean
   */
  public static boolean exists(String name) {
    return Arrays.stream(values()).anyMatch(item -> item.name().equalsIgnoreCase(name));
  }

  /**
   * 将字符串名称转换为枚举值
   *
   * @param name 字符串名称
   * @return ProductTypeEnum
   */
  public static ProductTypeEnum of(String name) {
    if (!StringUtils.isEmpty(name)) {
      for (ProductTypeEnum type : ProductTypeEnum.values()) {
        if (type.getContext().getName().equalsIgnoreCase(name)) {
          return type;
        }
      }
    }

    throw new IllegalArgumentException("cannot find enum name: " + name);
  }

}