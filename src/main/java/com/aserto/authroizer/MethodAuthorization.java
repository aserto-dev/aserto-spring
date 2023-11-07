package com.aserto.authroizer;

import com.aserto.authroizer.mapper.object.ObjectIdMapper;
import com.aserto.authroizer.mapper.object.ObjectTypeMapper;
import com.aserto.authroizer.mapper.relation.RelationMapper;
import com.aserto.authroizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authroizer.mapper.resource.CheckResourceMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;

@Component("aserto")
class MethodAuthorization {
    private AsertoAuthorizationManager asertoAuthzManager;
    private HttpServletRequest httpRequest;

    public MethodAuthorization(AuthzConfig authzCfg, HttpServletRequest httpRequest) {
        asertoAuthzManager = new AsertoAuthorizationManager(authzCfg);
        this.httpRequest = httpRequest;

    }

    public boolean check(String objectType, String objectId, String relation) {
        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectType, objectId, relation);

        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
        return decision.isGranted();
    }

    public boolean check(ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper);

        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
        return decision.isGranted();
    }

    public boolean check() {
        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest);
        return decision.isGranted();

    }
}
