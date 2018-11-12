package com.ixinhoo.config;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(
            ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        //这个配置使全局的映射器启用或禁用缓存。系统默认值是true，设置只是为了展示出来
        configuration.setCacheEnabled(true);
        //全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载。 系统默认值是true，设置只是为了展示出来
        configuration.setLazyLoadingEnabled(true);
        //允许或不允许多种结果集从一个单独的语句中返回（需要适合的驱动）。 系统默认值是true，设置只是为了展示出来
        configuration.setMultipleResultSetsEnabled(true);
        //使用列标签代替列名。不同的驱动在这方便表现不同。参考驱动文档或充分测试两种方法来决定所使用的驱动。 系统默认值是true，设置只是为了展示出来
        configuration.setUseColumnLabel(true);
        //允许 JDBC 支持生成的键。需要适合的驱动。如果设置为 true 则这个设置强制生成的键被使用，尽管一些驱动拒绝兼容但仍然有效（比如Derby）。 系统默认值是false，设置只是为了展示出来
        configuration.setUseGeneratedKeys(false);
        //配置默认的执行器。SIMPLE 执行器没有什么特别之处。REUSE 执行器重用预处理语句。BATCH 执行器重用语句和批量更新 系统默认值是SIMPLE，设置只是为了展示出来
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        //设置超时时间，它决定驱动等待一个数据库响应的时间,系统默认值是null
        configuration.setDefaultStatementTimeout(25000);
//        sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:mapper/**/**.xml"));


        return sessionFactory;
    }


}
