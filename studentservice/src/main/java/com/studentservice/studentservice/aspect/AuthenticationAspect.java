package com.studentservice.studentservice.aspect;

import com.studentservice.studentservice.config.SystemConfig;
import com.studentservice.studentservice.constants.ConstantService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class AuthenticationAspect {

    private final RestTemplate restTemplate;

    private final SystemConfig systemConfig;

    @Autowired
    public AuthenticationAspect(RestTemplate restTemplate, SystemConfig systemConfig) {
        this.restTemplate = restTemplate;
        this.systemConfig = systemConfig;
    }

    @Before("execution(* com.studentservice.studentservice.controller.*.*(..))")
    public void authCheck() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("accept", "*/*");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(
                systemConfig.getEasyAuthUrl() + ConstantService.getAUTH_ENDPOINT(),
                HttpMethod.GET,
                requestEntity,
                Boolean.class
        );
    }

}
