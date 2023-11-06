package com.aserto.authroizer.mapper.object;

import jakarta.servlet.http.HttpServletRequest;

public interface ObjectTypeMapper {
    public String getValue(HttpServletRequest httpRequest);
}
