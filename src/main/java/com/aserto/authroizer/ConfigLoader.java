package com.aserto.authroizer;

import com.aserto.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigLoader {
    @Value("${aserto.authorizer.host:localhost}")
    private String authorizerHost;

    @Value("${aserto.authorizer.port:8282}")
    private int authorizerPort;

    @Value("${aserto.authorizer.insecure:false}")
    private boolean insecureAuthorizer;

    @Value("${aserto.authorizer.cert.path:}")
    private String authorizerCertPath;

    @Value("${aserto.authorizer.apiKey:}")
    private String authorizerApiKey;

    @Value("${aserto.tenantId:}")
    private String tenantId;

    @Value("${aserto.authorizer.token:}")
    private String authorizerToken;

    public Config getAuthzCfg() {
        return new Config(
                authorizerHost,
                authorizerPort,
                authorizerApiKey,
                tenantId,
                authorizerToken,
                insecureAuthorizer,
                expandPath(authorizerCertPath)
        );
    }

    private String expandPath(String path) {
        return path.replace("$HOME", System.getProperty("user.home"));
    }

}
