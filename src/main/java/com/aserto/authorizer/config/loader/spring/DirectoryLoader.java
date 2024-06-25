package com.aserto.authorizer.config.loader.spring;

import com.aserto.authorizer.config.loader.ConfigLoader;
import com.aserto.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryLoader implements ConfigLoader {
    @Value("${aserto.tenantId:}")
    private String tenantId;

    @Value("${aserto.directory.serviceUrl:localhost:9292}")
    private String serviceUrl;

    @Value("${aserto.directory.apiKey:}")
    private String apiKey;

    @Value("${aserto.directory.insecure:false}")
    private Boolean insecure;

    @Value("${aserto.directory.grpc.caCertPath:}")
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
