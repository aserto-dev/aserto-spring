package com.aserto.authorizer.mapper.check.subject;

import jakarta.servlet.http.HttpServletRequest;

public class StaticSubjectIdMapper implements SubjectIdMapper {
    private String subjectId;

    public StaticSubjectIdMapper(String subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        return subjectId;
    }
}
