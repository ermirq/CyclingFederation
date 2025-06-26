package com.zerogravitysolutions.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories(basePackages = "com.zerogravitysolutions.core")
public class CoreApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}
