package com.aserto.authorizer;

import com.aserto.authorizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authorizer.mapper.check.relation.RelationMapper;
import com.aserto.authorizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authorizer.mapper.policy.PolicyMapper;
import com.aserto.authorizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authorizer.mapper.resource.CheckResourceMapper;
import com.aserto.authorizer.mapper.resource.EmptyResourceMapper;
import com.aserto.authorizer.mapper.resource.ResourceMapper;

public class CheckConfig {
    private final AuthzConfig authzCfg;

    private final ObjectTypeMapper objectTypeMapper;
    private final ObjectIdMapper objectIdMapper;
    private final RelationMapper relationMapper;
    private final PolicyMapper policyMapper;
    /**
     * ResourceMapper for additional fields to be included in the resource context.
     */
    private final ResourceMapper baseResourceMapper;

    static final String DEFAULT_POLICY = "rebac.check";

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        String objectID,
        String relation
    ) {
        this(filterConfig, objectType, objectID, relation, DEFAULT_POLICY);
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        String objectID,
        String relation,
        String policy
    ) {
        this(filterConfig, objectType, objectID, relation, policy, new EmptyResourceMapper());
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        String objectID,
        String relation,
        ResourceMapper baseResourceMapper
    ) {
        this(filterConfig, objectType, objectID, relation, DEFAULT_POLICY, baseResourceMapper);
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        String objectID,
        String relation,
        String policy,
        ResourceMapper baseResourceMapper
    ) {
        this(
            filterConfig,
            new StaticObjectTypeMapper(objectType),
            new StaticObjectIdMapper(objectID),
            new StaticRelationMapper(relation),
            new StaticPolicyMapper(policy),
            baseResourceMapper
        );
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, ObjectIdMapper objectIdMapper, String relation) {
        this(filterConfig, objectType, objectIdMapper, relation, DEFAULT_POLICY);
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, ObjectIdMapper objectIdMapper, String relation, String policy) {
        this(
            filterConfig,
            new StaticObjectTypeMapper(objectType),
            objectIdMapper,
            new StaticRelationMapper(relation),
            new StaticPolicyMapper(policy),
            new EmptyResourceMapper()
        );
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        ObjectIdMapper objectIdMapper,
        String relation,
        ResourceMapper baseResourceMapper
    ) {
        this(
            filterConfig,
            new StaticObjectTypeMapper(objectType),
            objectIdMapper,
            new StaticRelationMapper(relation),
            new StaticPolicyMapper(DEFAULT_POLICY),
            new EmptyResourceMapper()
        );
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        String objectType,
        ObjectIdMapper objectIdMapper,
        String relation,
        String policy,
        ResourceMapper baseResourceMapper
    ) {
        this(
            filterConfig,
            new StaticObjectTypeMapper(objectType),
            objectIdMapper,
            new StaticRelationMapper(relation),
            new StaticPolicyMapper(policy),
            baseResourceMapper
        );
    }

    public CheckConfig(AuthzConfig filterConfig, ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
        this(filterConfig, objectTypeMapper, objectIdMapper, relationMapper, new StaticPolicyMapper(DEFAULT_POLICY));
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        ObjectTypeMapper objectTypeMapper,
        ObjectIdMapper objectIdMapper,
        RelationMapper relationMapper,
        PolicyMapper policyMapper
    ) {
        this(filterConfig, objectTypeMapper, objectIdMapper, relationMapper, policyMapper, new EmptyResourceMapper());
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        ObjectTypeMapper objectTypeMapper,
        ObjectIdMapper objectIdMapper,
        RelationMapper relationMapper,
        ResourceMapper baseResourceMapper
    ) {
        this(filterConfig, objectTypeMapper, objectIdMapper, relationMapper, new StaticPolicyMapper(DEFAULT_POLICY), new EmptyResourceMapper());
    }

    public CheckConfig(
        AuthzConfig filterConfig,
        ObjectTypeMapper objectTypeMapper,
        ObjectIdMapper objectIdMapper,
        RelationMapper relationMapper,
        PolicyMapper policyMapper,
        ResourceMapper baseResourceMapper
    ) {
        this.authzCfg = new AuthzConfig(filterConfig);
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
        this.policyMapper = policyMapper;
        this.baseResourceMapper = baseResourceMapper != null ? baseResourceMapper : new EmptyResourceMapper();
    }

    public AsertoAuthorizationManager getAuthManager() {
        return new AsertoAuthorizationManager(getConfig());
    }

    public AuthzConfig getConfig() {
        authzCfg.setPolicyMapper(policyMapper);
        authzCfg.setResourceMapper(new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper, baseResourceMapper));

        return authzCfg;
    }
}
