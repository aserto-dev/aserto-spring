package com.aserto.authroizer.config.loader.spring;

import com.aserto.authroizer.config.loader.ConfigLoader;
import com.aserto.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuhorizerLoader implements ConfigLoader {
    @Value("${aserto.tenantId:}")
    private String tenantId;

    @Value("${aserto.authorizer.host:localhost}")
    private String host;

    @Value("${aserto.authorizer.port:9292}")
    private Integer port;

    @Value("${aserto.authorizer.apiKey:}")
    private String apiKey;

    @Value("${aserto.authorizer.token:}")
    private String token;

    @Value("${aserto.authorizer.insecure:false}")
    private Boolean insecure;

    @Value("${aserto.authorizer.grpc.caCertPath:}")
    private String caCertPath;

    @Override
    public Config loadConfig() {
        Config cfg = new Config();

        cfg.setTenantId(tenantId);
        cfg.setHost(host);
        cfg.setPort(port);
        cfg.setApiKey(apiKey);
        cfg.setToken(token);
        cfg.setInsecure(insecure);
        cfg.setCaCertPath(caCertPath);

        return cfg;
    }
}
