package com.sqlmurdermystery.casecontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CaseContentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaseContentServiceApplication.class, args);
    }
}
