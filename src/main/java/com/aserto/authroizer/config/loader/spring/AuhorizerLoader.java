package com.aserto.authroizer.config.loader.spring;

import com.aserto.authroizer.config.loader.ConfigLoader;
import com.aserto.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuhorizerLoader implements ConfigLoader {
    @Value("${aserto.tenantId:}")
    private String tenantId;

    @Value("${aserto.authorizer.serviceUrl:localhost:8282}")
    private String serviceUrl;

    @Value("${aserto.authorizer.apiKey:}")
    private String apiKey;

    @Value("${aserto.authorizer.insecure:false}")
    private Boolean insecure;

    @Value("${aserto.authorizer.grpc.caCertPath:}")
    private String caCertPath;

    @Override
    public Config loadConfig() {
        Config cfg = new Config();
        Address address = new Address(serviceUrl);

        cfg.setTenantId(tenantId);
        cfg.setHost(address.getHost());
        cfg.setPort(address.getPort());
        cfg.setApiKey(apiKey);
        cfg.setInsecure(insecure);
        cfg.setCaCertPath(caCertPath);

        return cfg;
    }
}
