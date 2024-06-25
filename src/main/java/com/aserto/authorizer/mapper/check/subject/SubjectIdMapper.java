package com.aserto.authorizer.mapper.check.subject;

import jakarta.servlet.http.HttpServletRequest;

public interface SubjectIdMapper {
    public String getValue(HttpServletRequest httpRequest);
}
