package com.syed.code.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class SessionFactoryConf {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.syed.code");
        emf.setPersistenceUnitName("default");
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean
    public org.hibernate.SessionFactory setSessionFactory(EntityManagerFactory entityManagerFactory) {
        org.hibernate.SessionFactory factory = entityManagerFactory.unwrap(org.hibernate.SessionFactory.class);

        return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class);
    }
}
