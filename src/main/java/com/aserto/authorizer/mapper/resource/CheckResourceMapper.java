package com.aserto.authorizer.mapper.resource;

import java.util.Map;

import com.aserto.authorizer.mapper.check.object.ObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.ObjectTypeMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectIdMapper;
import com.aserto.authorizer.mapper.check.object.StaticObjectTypeMapper;
import com.aserto.authorizer.mapper.check.relation.RelationMapper;
import com.aserto.authorizer.mapper.check.relation.StaticRelationMapper;
import com.aserto.authorizer.mapper.check.subject.StaticSubjectTypeMapper;
import com.aserto.authorizer.mapper.check.subject.SubjectTypeMapper;
import com.google.protobuf.Value;

import jakarta.servlet.http.HttpServletRequest;

public class CheckResourceMapper implements ResourceMapper {
    private final ObjectTypeMapper objectTypeMapper;
    private final ObjectIdMapper objectIdMapper;
    private final RelationMapper relationMapper;
    private final SubjectTypeMapper subjectTypeMapper;
    private final ResourceMapper baseResourceMapper;

    public CheckResourceMapper(String objectType, String objectId, String relation) {
        this(new StaticObjectTypeMapper(objectType),new StaticObjectIdMapper(objectId), new StaticRelationMapper(relation));
    }

    public CheckResourceMapper(String objectType, String objectId, String relation, String subjectType) {
        this(
            new StaticObjectTypeMapper(objectType),
            new StaticObjectIdMapper(objectId),
            new StaticRelationMapper(relation),
            new StaticSubjectTypeMapper(subjectType),
            new EmptyResourceMapper()
        );
    }

    public CheckResourceMapper(ObjectTypeMapper objectTypeMapper, ObjectIdMapper objectIdMapper, RelationMapper relationMapper) {
        this(objectTypeMapper, objectIdMapper, relationMapper, null, new EmptyResourceMapper());
    }

    public CheckResourceMapper(
        ObjectTypeMapper objectTypeMapper,
        ObjectIdMapper objectIdMapper,
        RelationMapper relationMapper,
        ResourceMapper baseResourceMapper
    ) {
        this(objectTypeMapper, objectIdMapper, relationMapper, null, baseResourceMapper);
    }

    public CheckResourceMapper(
        ObjectTypeMapper objectTypeMapper,
        ObjectIdMapper objectIdMapper,
        RelationMapper relationMapper,
        SubjectTypeMapper subjectTypeMapper,
        ResourceMapper baseResourceMapper
    ) {
        this.objectTypeMapper = objectTypeMapper;
        this.objectIdMapper = objectIdMapper;
        this.relationMapper = relationMapper;
        this.subjectTypeMapper = subjectTypeMapper;
        this.baseResourceMapper = baseResourceMapper != null ? baseResourceMapper : new EmptyResourceMapper();
    }

    @Override
    public Map<String, Value> getResource(HttpServletRequest request) throws ResourceMapperError {
        Map<String, Value> resourceCtx = baseResourceMapper.getResource(request);
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
