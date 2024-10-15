package com.aserto.authorizer;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;

import com.aserto.authorizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authorizer.mapper.check.relation.RelationMapper;
import com.aserto.authorizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authorizer.mapper.check.subject.StaticSubjectIdMapper;
import com.aserto.authorizer.mapper.check.subject.StaticSubjectTypeMapper;
import com.aserto.authorizer.mapper.check.subject.SubjectIdMapper;
import com.aserto.authorizer.mapper.check.subject.SubjectTypeMapper;
import com.aserto.authorizer.mapper.identity.ManualIdentityMapper;
import com.aserto.authorizer.mapper.policy.PolicyMapper;
import com.aserto.authorizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authorizer.mapper.resource.CheckResourceMapper;
import com.aserto.authorizer.mapper.resource.EmptyResourceMapper;
import com.aserto.authorizer.mapper.resource.ResourceMapper;

import jakarta.servlet.http.HttpServletRequest;

/*
* This class provides methods to check if the current user is authorized to perform an action.
* It can be used for method level authorization.
 */
@Component("check")
class MethodAuthorization {
    private final AsertoAuthorizationManager asertoAuthzManager;
    private final HttpServletRequest httpRequest;
    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;
    private SubjectTypeMapper subjectTypeMapper;
    private SubjectIdMapper subjectIdMapper;
    private PolicyMapper policyMapper;
    private ResourceMapper baseResourceMapper;

    public MethodAuthorization(AuthzConfig authzCfg, HttpServletRequest httpRequest) {
        asertoAuthzManager = new AsertoAuthorizationManager(authzCfg);
        this.httpRequest = httpRequest;
        this.policyMapper = new StaticPolicyMapper("rebac.check");
        this.baseResourceMapper = new EmptyResourceMapper();
    }

    public MethodAuthorization objectType(String objectType) {
        objectTypeMapper = new StaticObjectTypeMapper(objectType);
        return this;
    }

    public MethodAuthorization objectType(ObjectTypeMapper objectTypeMapper) {
        this.objectTypeMapper = objectTypeMapper;
        return this;
    }

    public MethodAuthorization objectId(String objectId) {
        objectIdMapper = new StaticObjectIdMapper(objectId);
        return this;
    }

    public MethodAuthorization objectId(ObjectIdMapper objectId) {
        this.objectIdMapper = objectId;
        return this;
    }

    public MethodAuthorization relation(String relation) {
        relationMapper = new StaticRelationMapper(relation);
        return this;
    }

    public MethodAuthorization relation(RelationMapper relation) {
        this.relationMapper = relation;
        return this;
    }

    public MethodAuthorization subjectType(String subjectType) {
        subjectTypeMapper = new StaticSubjectTypeMapper(subjectType);
        return this;
    }

    public MethodAuthorization subjectType(SubjectTypeMapper subjectTypeMapper) {
        this.subjectTypeMapper = subjectTypeMapper;
        return this;
    }

    public MethodAuthorization subjectId(String subjectId) {
        subjectIdMapper = new StaticSubjectIdMapper(subjectId);
        return this;
    }

    public MethodAuthorization subjectId(SubjectIdMapper subjectIdMapper) {
        this.subjectIdMapper = subjectIdMapper;
        return this;
    }

    public MethodAuthorization policyPath(String policyPath) {
        this.policyMapper = new StaticPolicyMapper(policyPath);
        return this;
    }

    public MethodAuthorization policyMapper(PolicyMapper policyMapper) {
        this.policyMapper = policyMapper;
        return this;
    }

    public MethodAuthorization baseResourceMapper(ResourceMapper baseResourceMapper) {
        this.baseResourceMapper = baseResourceMapper;
        return this;
    }

    public boolean allowed() {
        validateFields();

        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(
            objectTypeMapper,
            objectIdMapper,
            relationMapper,
            subjectTypeMapper,
            baseResourceMapper
        );

        AuthorizationDecision decision;
        if (subjectIdMapper != null && subjectTypeMapper != null) {
            ManualIdentityMapper identityMapper = new ManualIdentityMapper(subjectIdMapper.getValue(httpRequest));
            decision = asertoAuthzManager.check(httpRequest, identityMapper, policyMapper, checkResourceMapper);
        } else {
            decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
        }

        return decision.isGranted();
    }

    private void validateFields() {
        if (objectTypeMapper == null) {
            throw new IllegalArgumentException("objectType must be set");
        }
        if (objectIdMapper == null) {
            throw new IllegalArgumentException("objectId must be set");
        }
        if (relationMapper == null) {
            throw new IllegalArgumentException("relation must be set");
        }
    }
}
