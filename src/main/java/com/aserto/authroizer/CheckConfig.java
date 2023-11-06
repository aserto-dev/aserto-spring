package com.aserto.authroizer;

import com.aserto.authroizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authroizer.mapper.resource.StaticResourceMapper;
import com.aserto.filter.AsertoFilter;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.Map;

public class CheckConfig {
    private AuthzConfig authzCfg;
    private String objectKey;
    private String objectType;
    private String relation;

    public CheckConfig(AuthzConfig authzCfg) {
        // Clone the authz config because we will change it
        this.authzCfg = new AuthzConfig(authzCfg);
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, String objectKey, String relation) {
        this.authzCfg = new AuthzConfig(filterConfig);
        this.objectType = objectType;
        this.objectKey = objectKey;
        this.relation = relation;
    }

    public CheckConfig setObjectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public CheckConfig setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public CheckConfig setRelation(String relation) {
        this.relation = relation;
        return this;
    }

    public AsertoFilter getFilter() {
        return new AsertoFilter(getConfig());
    }

    public AsertoAuthorizationManager getAuthManager() {
        return new AsertoAuthorizationManager(getConfig());
    }

    public AuthzConfig getConfig() {
        authzCfg.setPolicyMapper(new StaticPolicyMapper("rebac.check"));
        authzCfg.setResourceMapper(new StaticResourceMapper(buildResources()));

        return authzCfg;
    }

    private Map<String, Value> buildResources() {
        Map<String, Value> resourceCtx = new HashMap<>();
        resourceCtx.put("object_type", Value.newBuilder().setStringValue(objectType).build());
        resourceCtx.put("object_key", Value.newBuilder().setStringValue(objectKey).build());
        resourceCtx.put("relation", Value.newBuilder().setStringValue(relation).build());

        return resourceCtx;
    }
}
