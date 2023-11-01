package com.aserto.example.security;

import com.aserto.authroizer.mapper.extractor.Extractor;
import com.aserto.authroizer.mapper.extractor.HeaderExtractor;
import com.aserto.authroizer.mapper.identity.IdentityMapper;
import com.aserto.authroizer.mapper.identity.JwtIdentityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Bean
    public IdentityMapper identityMapper() {
        Extractor hostNameExtractor = new HeaderExtractor("authorization");
        return new JwtIdentityMapper(hostNameExtractor);
    }
}
