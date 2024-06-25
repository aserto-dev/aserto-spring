package com.aserto.example;

import com.aserto.authorizer.AuthzConfig;
import com.aserto.authorizer.CheckConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    private AuthzConfig authzCfg;
    public SecurityConfiguration(AuthzConfig authzCfg) {
        this.authzCfg = authzCfg;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable);
//            This code can be commented out to enable route level authorization
//            .authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers(HttpMethod.DELETE, "/todos/{id}")
//                    .access(new CheckConfig(authzCfg, "group", "admin", "member").getAuthManager())
//
//                    .requestMatchers(HttpMethod.GET, "/todos")
//                    .access(new CheckConfig(authzCfg, "group", "viewer", "member").getAuthManager())
//
//                    .requestMatchers(HttpMethod.GET, "/users/{userID}")
//                    .access(new CheckConfig(authzCfg, "group", "viewer", "member").getAuthManager())
//
//                    .requestMatchers(HttpMethod.POST, "/todos")
//                    .access(new CheckConfig(authzCfg, "group", "editor", "member").getAuthManager())
//
//                    .requestMatchers(HttpMethod.PUT, "/todos/{id}")
//                    .access(new CheckConfig(authzCfg, "group", "editor", "member").getAuthManager())
//
//                    .anyRequest().denyAll()
//        );
        return http.build();
    }
}

