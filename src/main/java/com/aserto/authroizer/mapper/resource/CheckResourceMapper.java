package com.aserto.authroizer.mapper.resource;

import com.aserto.authroizer.mapper.object.ObjectIdMapper;
import com.aserto.authroizer.mapper.object.ObjectTypeMapper;
import com.aserto.authroizer.mapper.object.StaticObjectIdMapper;
import com.aserto.authroizer.mapper.object.StaticObjectTypeMapper;
import com.aserto.authroizer.mapper.relation.RelationMapper;
import com.aserto.authroizer.mapper.relation.StaticRelationMapper;
import com.google.protobuf.Value;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckResourceMapper implements ResourceMapper {
    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;

//    public CheckResourceMapper() {
//    }

    public CheckResourceMapper(String objectType, String objectId, String relation) {
        this.objectTypeMapper = new StaticObjectTypeMapper(objectType);
        this.objectIdMapper = new StaticObjectIdMapper(objectId);
        this.relationMapper = new StaticRelationMapper(relation);
    }

    public CheckResourceMapper(ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
    }
//
//    public void setObjectTypeMapper(ObjectTypeMapper objectTypeMapper) {
//        this.objectTypeMapper = objectTypeMapper;
//    }
//
//    public void setObjectType(String objectType) {
//        this.objectTypeMapper = new StaticObjectTypeMapper(objectType);
//    }
//
//    public void setObjectIdMapper(ObjectIdMapper objectIdMapper) {
//        this.objectIdMapper = objectIdMapper;
//    }
//
//    public void setObjectId(String objectId) {
//        this.objectIdMapper = new StaticObjectIdMapper(objectId);
//    }
//
//    public void setRelationMapper(RelationMapper relationMapper) {
//        this.relationMapper = relationMapper;
//    }
//
//    public void setRelation(String relation) {
//        this.relationMapper = new StaticRelationMapper(relation);
//    }


    @Override
    public Map<String, Value> getResource(HttpServletRequest request) throws ResourceMapperError {
        Map<String, Value> resourceCtx = new HashMap<>();
        resourceCtx.put("object_type", Value.newBuilder().setStringValue(objectTypeMapper.getValue(request)).build());
        resourceCtx.put("object_id", Value.newBuilder().setStringValue(objectIdMapper.getValue(request)).build());
        resourceCtx.put("relation", Value.newBuilder().setStringValue(relationMapper.getValue(request)).build());

        return resourceCtx;
    }
}
