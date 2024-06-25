package com.aserto.authorizer.mapper.check.relation;

import jakarta.servlet.http.HttpServletRequest;

public interface RelationMapper {
    public String getValue(HttpServletRequest httpRequest);
}
