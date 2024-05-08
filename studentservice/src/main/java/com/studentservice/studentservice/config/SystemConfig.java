package com.studentservice.studentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@SuppressWarnings("SpellCheckingInspection")
@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Validated
public class SystemConfig {

    private String easyAuthUrl;

}
