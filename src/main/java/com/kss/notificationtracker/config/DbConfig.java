//package com.kss.notificationtracker.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.Database;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "com.kss.notificationtracker.repository")
//public class DbConfig {
//
//    @Autowired
//    private Environment environment;
//
//  @Autowired
//  DataSource dataSource;
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setDatabase(Database.MYSQL);
//        // vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
//
//        // Use these properties to let spring work on batch insertion
//        Properties jpaProperties = new Properties();
//        jpaProperties.put("hibernate.jdbc.batch_size", 100);
//        jpaProperties.put("hibernate.order_inserts", true);
//        jpaProperties.put("hibernate.order_updates", true);
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setJpaProperties(jpaProperties);
//        factory.setPackagesToScan("com.kss.notificationtracker.entity");
//        factory.setDataSource(dataSource);
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }
//
//}