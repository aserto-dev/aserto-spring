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

public class CheckConfig {
    private AuthzConfig authzCfg;

    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;
	private PolicyMapper policyMapper;

    public CheckConfig(AuthzConfig authzCfg) {
        // Clone the authz config because we will change it
        this.authzCfg = new AuthzConfig(authzCfg);
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, String objectID, String relation) {
		this(filterConfig, objectType, objectID, relation, "rebac.check");
    }

    public CheckConfig(AuthzConfig filterConfig, String objectType, String objectID, String relation, String policy) {
		this(filterConfig, new StaticObjectTypeMapper(objectType), new StaticObjectIdMapper(objectID), new StaticRelationMapper(relation), new StaticPolicyMapper(policy));
	}

    public CheckConfig(AuthzConfig filterConfig, ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
		this(filterConfig, objectTypeMapper, objectIdMapper, relationMapper, new StaticPolicyMapper("rebac.check"));
	}

    public CheckConfig(AuthzConfig filterConfig, ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper, PolicyMapper policyMapper) {
        this.authzCfg = new AuthzConfig(filterConfig);
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
		this.policyMapper = policyMapper;
    }

    public AsertoAuthorizationManager getAuthManager() {
        return new AsertoAuthorizationManager(getConfig());
    }

    public AuthzConfig getConfig() {
        authzCfg.setPolicyMapper(policyMapper);
        authzCfg.setResourceMapper(new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper));

        return authzCfg;
    }
}
