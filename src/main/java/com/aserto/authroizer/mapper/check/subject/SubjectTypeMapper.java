package com.aserto.authroizer.mapper.check.subject;

import jakarta.servlet.http.HttpServletRequest;

public interface SubjectTypeMapper {
    public String getValue(HttpServletRequest httpRequest);
}
