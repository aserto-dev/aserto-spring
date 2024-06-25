package com.aserto.authorizer.mapper.resource;

import com.aserto.authorizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authorizer.mapper.check.relation.RelationMapper;
import com.aserto.authorizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authorizer.mapper.check.subject.SubjectTypeMapper;
import com.google.protobuf.Value;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckResourceMapper implements ResourceMapper {
    private ObjectTypeMapper objectTypeMapper;
    private ObjectIdMapper objectIdMapper;
    private RelationMapper relationMapper;

    private SubjectTypeMapper subjectTypeMapper;

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

    public CheckResourceMapper(ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper, SubjectTypeMapper subjectTypeMapper) {
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
        this.subjectTypeMapper = subjectTypeMapper;
    }

    @Override
    public Map<String, Value> getResource(HttpServletRequest request) throws ResourceMapperError {
        Map<String, Value> resourceCtx = new HashMap<>();
        resourceCtx.put("object_type", Value.newBuilder().setStringValue(objectTypeMapper.getValue(request)).build());
        resourceCtx.put("object_id", Value.newBuilder().setStringValue(objectIdMapper.getValue(request)).build());
        resourceCtx.put("relation", Value.newBuilder().setStringValue(relationMapper.getValue(request)).build());

        // Subject type is optional and is used when identity Manual is provided
        if (subjectTypeMapper != null) {
            resourceCtx.put("subject_type", Value.newBuilder().setStringValue(subjectTypeMapper.getValue(request)).build());
        }

        return resourceCtx;
    }
}
