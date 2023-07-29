package com.syed.rbacserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.syed.code")
@ComponentScan(basePackages = {"com.syed.code"})
@EntityScan("com.syed.code")
public class RbacServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RbacServerApplication.class, args);
    }

}
