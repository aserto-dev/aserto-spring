package com.aserto.authorizer.mapper.check.subject;

import jakarta.servlet.http.HttpServletRequest;

public class StaticSubjectTypeMapper implements SubjectTypeMapper {
    private String subjectType;

    public StaticSubjectTypeMapper(String subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        return subjectType;
    }
}
