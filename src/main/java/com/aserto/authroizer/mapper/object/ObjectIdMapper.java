package com.aserto.authroizer.mapper.object;

import jakarta.servlet.http.HttpServletRequest;

public interface ObjectIdMapper {
    public String getValue(HttpServletRequest httpRequest);
}
