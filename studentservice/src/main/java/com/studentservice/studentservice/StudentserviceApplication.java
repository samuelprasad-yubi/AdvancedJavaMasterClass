package com.studentservice.studentservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Student Service"))
public class StudentserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentserviceApplication.class, args);
    }

}
