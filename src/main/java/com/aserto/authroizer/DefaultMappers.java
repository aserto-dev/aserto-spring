package com.aserto.authroizer;

import com.aserto.AuthorizerClient;
import com.aserto.AuthzClient;
import com.aserto.ChannelBuilder;
import com.aserto.authroizer.config.loader.spring.AuhorizerLoader;
import com.aserto.authroizer.mapper.extractor.Extractor;
import com.aserto.authroizer.mapper.extractor.HeaderExtractor;
import com.aserto.authroizer.mapper.identity.IdentityMapper;
import com.aserto.authroizer.mapper.identity.JwtIdentityMapper;
import com.aserto.authroizer.mapper.policy.HttpPathPolicyMapper;
import com.aserto.authroizer.mapper.policy.PolicyMapper;
import com.aserto.authroizer.mapper.resource.EmptyResourceMapper;
import com.aserto.authroizer.mapper.resource.ResourceMapper;
import com.aserto.model.Config;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.net.ssl.SSLException;

@Component
public class DefaultMappers {
    private RequestMappingHandlerMapping handlerMapping;
    private AuhorizerLoader auhorizerLoader;

    @Autowired
    public DefaultMappers(RequestMappingHandlerMapping handlerMapping, AuhorizerLoader auhorizerLoader) {
        this.handlerMapping = handlerMapping;
        this.auhorizerLoader = auhorizerLoader;
    }

    @Bean
    @ConditionalOnMissingBean(IdentityMapper.class)
    public IdentityMapper identityDiscoverer() {
        Extractor headerExtractor = new HeaderExtractor("Authorization");
        return new JwtIdentityMapper(headerExtractor);
    }

    @Bean
    @ConditionalOnMissingBean(PolicyMapper.class)
    public PolicyMapper policyMapperDiscoverer() {
        return new HttpPathPolicyMapper(handlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean(ResourceMapper.class)
    public ResourceMapper mapperDiscoverer() {
        return new EmptyResourceMapper();
    }

    @Bean
    @ConditionalOnMissingBean(AuthorizerClient.class)
    public AuthorizerClient authorizerClientDiscoverer() throws SSLException {
        Config authzCfg = auhorizerLoader.loadConfig();
        ManagedChannel channel = new ChannelBuilder(authzCfg).build();

        return new AuthzClient(channel);
    }
}
