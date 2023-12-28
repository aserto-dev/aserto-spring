package com.aserto.authroizer;

import com.aserto.authroizer.mapper.object.ObjectIdMapper;
import com.aserto.authroizer.mapper.object.ObjectTypeMapper;
import com.aserto.authroizer.mapper.object.StaticObjectIdMapper;
import com.aserto.authroizer.mapper.object.StaticObjectTypeMapper;
import com.aserto.authroizer.mapper.relation.RelationMapper;
import com.aserto.authroizer.mapper.policy.StaticPolicyMapper;
import com.aserto.authroizer.mapper.relation.StaticRelationMapper;
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

    public MethodAuthorization(AuthzConfig authzCfg, HttpServletRequest httpRequest) {
        asertoAuthzManager = new AsertoAuthorizationManager(authzCfg);
        this.httpRequest = httpRequest;
    }


//    /*
//    * Check if the current user is authorized to perform the action defined in the policy.
//    * @return true if the user is authorized, false otherwise
//     */
//    public boolean check() {
//        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest);
//        return decision.isGranted();
//    }
//
//    /*
//    * Check if the current user has a relation to the object defined by ObjectType, ObjectId and Relation.
//    * If the relation exists, the user is authorized.
//    *
//    * @param objectType The type of the object
//    * @param objectId The id of the object
//    * @param relation The relation to check
//    * @return true if the user is authorized, false otherwise
//     */
//    public boolean check(String objectType, String objectId, String relation) {
//        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
//        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectType, objectId, relation);
//
//        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
//        return decision.isGranted();
//    }
//
//    /*
//     * Check if the current user has a relation to the object defined by objectTypeMapper, ObjectIdMapper and RelationMapper.
//     * If the relation exists, the user is authorized.
//     *
//     * @param objectTypeMapper The mapper for the type of the object
//     * @param objectIdMapper The mapper for the id of the object
//     * @param relationMapper The mapper for the relation to check
//     * @return true if the user is authorized, false otherwise
//     */
//    public boolean check(ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
//        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
//        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper);
//
//        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
//        return decision.isGranted();
//    }

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

    public boolean allowed() {
        StaticPolicyMapper policyMapper = new StaticPolicyMapper("rebac.check");
        CheckResourceMapper checkResourceMapper = new CheckResourceMapper(objectTypeMapper, objectIdMapper, relationMapper);

        AuthorizationDecision decision = asertoAuthzManager.check(httpRequest, policyMapper, checkResourceMapper);
        return decision.isGranted();
    }
}
