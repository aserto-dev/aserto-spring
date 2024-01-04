package com.aserto.authroizer.mapper.check.relation;

import jakarta.servlet.http.HttpServletRequest;

public class StaticRelationMapper implements RelationMapper {
    private String relation;

    public StaticRelationMapper(String relation) {
        this.relation = relation;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        return relation;
    }
}
