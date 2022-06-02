package com.soukuan.demo

import com.alibaba.druid.pool.DruidDataSource
import groovy.sql.Sql

import javax.sql.DataSource


class JdbcDemo {

    static void testSql() {
        // Creating a connection to the database
        def sql = Sql.newInstance('jdbc:mysql://localhost:3306/test', 'root',
                '', 'com.mysql.jdbc.Driver')
        sql.connection.autoCommit = false
        def sqlstr = """INSERT INTO T_TEST(ID, NAME) VALUES (null,"xxx")"""
        try {
            sql.execute(sqlstr);
            sql.commit()
            println("Successfully committed")
        } catch (Exception ex) {
            sql.rollback()
            println("Transaction rollback")
        }
        sql.close()
    }

    static void testDataSource() {
        DataSource dataSource = new DruidDataSource()
        dataSource.setUrl("jdbc:mysql://localhost:3306/oms201705")
        dataSource.setUsername("root")
        dataSource.setPassword("")
        dataSource.setDriverClassName("com.mysql.jdbc.Driver")
        dataSource.setInitialSize(3)
        dataSource.setMinIdle(3)
        dataSource.setMaxActive(200)
        dataSource.setMaxWait(60000)
        dataSource.setTimeBetweenEvictionRunsMillis(60000)
        dataSource.setMinEvictableIdleTimeMillis(300000)
        dataSource.setValidationQuery("select 1")
        dataSource.setTestWhileIdle(true)
        dataSource.setTestOnBorrow(false)
        dataSource.setTestOnReturn(false)
        dataSource.setPoolPreparedStatements(true)
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20)
        dataSource.init()
        Sql sql = new Sql(dataSource)
    }

    static void main(String[] args) {
        testSql()
    }
}
