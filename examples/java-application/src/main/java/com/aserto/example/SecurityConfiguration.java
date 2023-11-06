package com.aserto.example;

import com.aserto.authroizer.AuthzConfig;
import com.aserto.authroizer.CheckConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {
    private AuthzConfig authzCfg;
    public SecurityConfiguration(AuthzConfig authzCfg) {
        this.authzCfg = authzCfg;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .antMatchers(HttpMethod.GET, "/todos")
                    .access(new CheckConfig(authzCfg, "viewer", "group", "member").getAuthManager())

                    .antMatchers(HttpMethod.POST, "/todos")
                    .access(new CheckConfig(authzCfg, "admin", "group", "member").getAuthManager())

                    .anyRequest().denyAll()
        );
        return http.build();
    }
}

