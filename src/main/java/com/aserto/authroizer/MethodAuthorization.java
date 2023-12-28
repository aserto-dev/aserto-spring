package com.aserto.authroizer;

import com.aserto.authroizer.mapper.check.subject.StaticSubjectIdMapper;
import com.aserto.authroizer.mapper.check.subject.StaticSubjectTypeMapper;
import com.aserto.authroizer.mapper.check.subject.SubjectIdMapper;
import com.aserto.authroizer.mapper.check.subject.SubjectTypeMapper;
import com.aserto.authroizer.mapper.identity.ManualIdentityMapper;
import com.aserto.authroizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authroizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authroizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authroizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authroizer.mapper.check.relation.RelationMapper;
import com.aserto.authroizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authroizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authroizer.mapper.resource.CheckResourceMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;

/*
* This class provides methods to check if the current user is authorized to perform an action.
* It can be used for method level authorization.
 */
@Component("check")
class MethodAuthorization {
    private AsertoAuthorizationManager asertoAuthzManager;
    private HttpServletRequest httpRequest;
    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;
    private SubjectTypeMapper subjectTypeMapper;
    private SubjectIdMapper subjectIdMapper;

    public MethodAuthorization(AuthzConfig authzCfg, HttpServletRequest httpRequest) {
        asertoAuthzManager = new AsertoAuthorizationManager(authzCfg);
        this.httpRequest = httpRequest;
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

    public boolean allowed() {
        validateFields();

        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper, subjectTypeMapper);

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
