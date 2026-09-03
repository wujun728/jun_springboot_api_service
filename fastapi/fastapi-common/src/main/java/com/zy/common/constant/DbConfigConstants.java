package com.zy.common.constant;

public class DbConfigConstants {

    /** mysql驱动前缀 */
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    /** oracle驱动前缀 */
    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    /** postgresql驱动前缀 */
    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    /** sqlserver驱动前缀 */
    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /** clickhouse驱动前缀 */
    public static final String CLICKHOUSE_DRIVER = "com.clickhouse.jdbc.ClickHouseDriver";

    /** 检查状态-健康 */
    public static final Long CHECK_STATUS_OK = 1L;

    /** 检查状态-异常 */
    public static final Long CHECK_STATUS_ERROR = 2L;

}
