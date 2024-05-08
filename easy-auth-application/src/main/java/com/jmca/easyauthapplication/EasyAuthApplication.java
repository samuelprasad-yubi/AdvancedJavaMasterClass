package com.jmca.easyauthapplication;

import com.jmca.easyauthapplication.controller.AuthController;
import com.jmca.easyauthapplication.payload.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EasyAuthApplication implements CommandLineRunner {

    @Autowired
    AuthController authController;

    public static void main(String[] args) {
        SpringApplication.run(EasyAuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        authController.registerUser(
                new SignupRequest("admin", "admin@gmail.com", "admin", "4c782eB4278aC"));
    }

}
