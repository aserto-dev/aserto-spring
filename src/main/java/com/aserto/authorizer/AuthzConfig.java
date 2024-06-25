package com.aserto.authorizer;

import com.aserto.AuthorizerClient;
import com.aserto.authorizer.mapper.identity.IdentityMapper;
import com.aserto.authorizer.mapper.policy.PolicyMapper;
import com.aserto.authorizer.mapper.resource.ResourceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthzConfig {
    @Value("${aserto.authorizer.decision:allow}")
    private String authorizerDecision;

    @Value("${aserto.authorizer.policyName:}")
    private String policyName;

    @Value("${aserto.authorizer.policyLabel:}")
    private String policyLabel;

    @Value("${aserto.authorization.enabled:true}")
    boolean authorizerEnabled;

    private IdentityMapper identityMapper;
    private PolicyMapper policyMapper;
    private ResourceMapper resourceMapper;
    private AuthorizerClient authzClient;

    @Autowired(required=true)
    public AuthzConfig(IdentityMapper identityMapper, PolicyMapper policyMapper, ResourceMapper resourceMapper,
                       AuthorizerClient authzClient) {
        this.identityMapper = identityMapper;
        this.policyMapper = policyMapper;
        this.resourceMapper = resourceMapper;
        this.authzClient = authzClient;
    }

    // Used to make a copy of the filter
    public AuthzConfig(AuthzConfig authzConfig) {
        this.authorizerDecision = authzConfig.getAuthorizerDecision();
        this.policyName = authzConfig.getPolicyName();
        this.policyLabel = authzConfig.getPolicyLabel();
        this.authorizerEnabled = authzConfig.isAuthorizerEnabled();
        this.identityMapper = authzConfig.getIdentityMapper();
        this.policyMapper = authzConfig.getPolicyMapper();
        this.resourceMapper = authzConfig.getResourceMapper();
        this.authzClient = authzConfig.getAuthzClient();
    }

    public String getAuthorizerDecision() {
        return authorizerDecision;
    }

    public void setAuthorizerDecision(String authorizerDecision) {
        this.authorizerDecision = authorizerDecision;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyLabel() {
        return policyLabel;
    }

    public void setPolicyLabel(String policyLabel) {
        this.policyLabel = policyLabel;
    }

    public boolean isAuthorizerEnabled() {
        return authorizerEnabled;
    }

    public void setAuthorizerEnabled(boolean authorizerEnabled) {
        this.authorizerEnabled = authorizerEnabled;
    }

    public IdentityMapper getIdentityMapper() {
        return identityMapper;
    }

    public void setIdentityMapper(IdentityMapper identityMapper) {
        this.identityMapper = identityMapper;
    }

    public PolicyMapper getPolicyMapper() {
        return policyMapper;
    }

    public void setPolicyMapper(PolicyMapper policyMapper) {
        this.policyMapper = policyMapper;
    }

    public ResourceMapper getResourceMapper() {
        return resourceMapper;
    }

    public void setResourceMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public AuthorizerClient getAuthzClient() {
        return authzClient;
    }

    public void setAuthzClient(AuthorizerClient authzClient) {
        this.authzClient = authzClient;
    }
}
