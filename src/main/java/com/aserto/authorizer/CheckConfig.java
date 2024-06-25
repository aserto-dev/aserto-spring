package com.aserto.authorizer;

import com.aserto.authorizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authorizer.mapper.check.relation.RelationMapper;
import com.aserto.authorizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authorizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authorizer.mapper.resource.CheckResourceMapper;

public class CheckConfig {
    private AuthzConfig authzCfg;

    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;

    public CheckConfig(AuthzConfig authzCfg) {
        // Clone the authz config because we will change it
        this.authzCfg = new AuthzConfig(authzCfg);
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, String objectKey, String relation) {
        this.authzCfg = new AuthzConfig(filterConfig);

        this.objectTypeMapper = new StaticObjectTypeMapper(objectType);
        this.objectIdMapper = new StaticObjectIdMapper(objectKey);
        this.relationMapper = new StaticRelationMapper(relation);
    }

    public CheckConfig(AuthzConfig filterConfig, ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
        this.authzCfg = new AuthzConfig(filterConfig);
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
    }

    public CheckConfig setObjectType(String objectType) {
        this.objectTypeMapper = new StaticObjectTypeMapper(objectType);
        return this;
    }

    public CheckConfig setObjectTypeMapper(ObjectTypeMapper objectTypeMapper) {
        this.objectTypeMapper = objectTypeMapper;
        return this;
    }

    public CheckConfig setObjectKey(String objectKey) {
        this.objectIdMapper = new StaticObjectIdMapper(objectKey);
        return this;
    }

    public CheckConfig setObjectIdMapper(ObjectIdMapper objectIdMapper) {
        this.objectIdMapper = objectIdMapper;
        return this;
    }

    public CheckConfig setRelation(String relation) {
        this.relationMapper = new StaticRelationMapper(relation);
        return this;
    }

    public CheckConfig setRelationMapper(RelationMapper relationMapper) {
        this.relationMapper = relationMapper;
        return this;
    }

    public AsertoAuthorizationManager getAuthManager() {
        return new AsertoAuthorizationManager(getConfig());
    }

    public AuthzConfig getConfig() {
        authzCfg.setPolicyMapper(new StaticPolicyMapper("rebac.check"));
        authzCfg.setResourceMapper(new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper));

        return authzCfg;
    }
}
