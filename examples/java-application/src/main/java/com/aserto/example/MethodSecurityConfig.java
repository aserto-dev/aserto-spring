package com.aserto.example;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationManagerAfterMethodInterceptor;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
class MethodSecurityConfig {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor preAuthorize(MyAuthorizationManager manager) {
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
    }

//    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    Advisor postAuthorize(MyAuthorizationManager manager) {
//        return AuthorizationManagerAfterMethodInterceptor.postAuthorize(manager);
//    }
}