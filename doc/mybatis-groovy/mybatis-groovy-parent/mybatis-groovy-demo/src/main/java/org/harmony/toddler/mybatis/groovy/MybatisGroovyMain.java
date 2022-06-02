package org.harmony.toddler.mybatis.groovy;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.harmony.toddler.mybatis.groovy.demo.domain.User;
import org.harmony.toddler.mybatis.groovy.demo.mapper.UserMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Mybatis Groovy Main
 */
public class MybatisGroovyMain {

    private SqlSessionFactory sqlSessionFactory;

    public static void main(String[] args) {
        MybatisGroovyMain mybatis = new MybatisGroovyMain();
        try {
            mybatis.runFromXml();
            mybatis.runFromAnnotation();
//            mybatis.testVarSubstitute();
            mybatis.runGroovySql();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runGroovySql() throws IOException {
        GroovyLangDriverConfig.setRoots(new String[]{"spring-boot-demo/sql"});
        buildingSqlSessionFactory();
        sqlSessionFactory.getConfiguration().getTypeAliasRegistry().registerAlias("groovy", GroovyLangDriver.class);

        SqlSession session = sqlSessionFactory.openSession();
        int id = 1;
        String name = "a";
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectById(id);
        System.out.println(user.getName());
        user = mapper.selectByWrapper(id, name);
        System.out.println(user.getName());
    }

    public void testVarSubstitute() throws IOException {
        buildingSqlSessionFactory();

        SqlSession session = sqlSessionFactory.openSession();
        String condition = "'a'";
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.findByCondition(condition);
        System.out.println(user.getName());
    }

    public void runFromAnnotation() throws IOException {
        buildingSqlSessionFactory();

        SqlSession session = sqlSessionFactory.openSession();
        int id = 1;
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectByPrimaryKey(id);
        System.out.println(user.getName());
    }

    public void runFromXml() throws IOException {
        buildingSqlSessionFactoryFromXml();

        SqlSession session = sqlSessionFactory.openSession();
        int id = 1;
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.findByIdXml(id);
        System.out.println(user.getName());
    }

    /**
     * Building SqlSessionFactory from XML
     *
     * @throws IOException
     */
    private void buildingSqlSessionFactoryFromXml() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    }

    /**
     * Building SqlSessionFactory without XML
     *
     * @throws IOException
     */
    private void buildingSqlSessionFactory() throws IOException {
        DataSource dataSource = getDataSource("db.properties");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development",
                transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    private DataSource getDataSource(String filepath) throws IOException {
        DataSourceFactory dsFactory = new PooledDataSourceFactory();
        Properties props = Resources.getResourceAsProperties(filepath);
        dsFactory.setProperties(props);
        return dsFactory.getDataSource();
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
