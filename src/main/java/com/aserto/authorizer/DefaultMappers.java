package com.aserto.authorizer;

import com.aserto.AuthorizerClient;
import com.aserto.authorizer.config.loader.spring.AuthorizerLoader;
import com.aserto.authorizer.mapper.identity.IdentityMapper;
import com.aserto.authorizer.mapper.identity.NoneIdentityMapper;
import com.aserto.authorizer.mapper.policy.HttpPathPolicyMapper;
import com.aserto.authorizer.mapper.policy.PolicyMapper;
import com.aserto.authorizer.mapper.resource.PathParamsResourceMapper;
import com.aserto.authorizer.mapper.resource.ResourceMapper;
import com.aserto.ChannelBuilder;
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
    private final RequestMappingHandlerMapping handlerMapping;
    private final AuthorizerLoader authorizerLoader;

    @Autowired
    public DefaultMappers(RequestMappingHandlerMapping handlerMapping,
                          AuthorizerLoader authorizerLoader) {
        this.handlerMapping = handlerMapping;
        this.authorizerLoader = authorizerLoader;
    }

    @Bean
    @ConditionalOnMissingBean(IdentityMapper.class)
    public IdentityMapper identityDiscoverer() {
        return new NoneIdentityMapper();
    }

    @Bean
    @ConditionalOnMissingBean(PolicyMapper.class)
    public PolicyMapper policyMapperDiscoverer() {
        return new HttpPathPolicyMapper(handlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean(ResourceMapper.class)
    public ResourceMapper mapperDiscoverer() {
        return new PathParamsResourceMapper(handlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean(AuthorizerClient.class)
    public AuthorizerClient authorizerClientDiscoverer() throws SSLException {
        Config authzCfg = authorizerLoader.loadConfig();
        ManagedChannel channel = new ChannelBuilder(authzCfg).build();

        return new AuthzClient(channel);
    }
}
