package com.aserto.authroizer.config.loader.spring;

import com.aserto.authroizer.config.loader.ConfigLoader;
import com.aserto.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryLoader implements ConfigLoader {
    @Value("${aserto.tenantId:}")
    private String tenantId;

    @Value("${aserto.directory.host:localhost}")
    private String host;

    @Value("${aserto.directory.port:9292}")
    private Integer port;

    @Value("${aserto.directory.apiKey:}")
    private String apiKey;

    @Value("${aserto.directory.token:}")
    private String token;

    @Value("${aserto.directory.insecure:false}")
    private Boolean insecure;

    @Value("${aserto.directory.grpc.caCertPath:}")
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
