package com.discoveryhub.discoveryhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryHubApplication.class, args);
    }

}
