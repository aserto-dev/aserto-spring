package com.aserto.authroizer.mapper.relation;

import jakarta.servlet.http.HttpServletRequest;

public interface RelationMapper {
    public String getValue(HttpServletRequest httpRequest);
}
