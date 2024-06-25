package com.aserto.authorizer.mapper.check.object;

import jakarta.servlet.http.HttpServletRequest;

public interface ObjectIdMapper {
    public String getValue(HttpServletRequest httpRequest);
}
