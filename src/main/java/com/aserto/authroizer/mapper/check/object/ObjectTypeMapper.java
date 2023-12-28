package com.aserto.authroizer.mapper.check.object;

import jakarta.servlet.http.HttpServletRequest;

public interface ObjectTypeMapper {
    public String getValue(HttpServletRequest httpRequest);
}
