package com.aserto.authroizer.mapper.check.object;

import jakarta.servlet.http.HttpServletRequest;

public class StaticObjectIdMapper implements ObjectIdMapper {
    private String objectId;

    public StaticObjectIdMapper(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        return objectId;
    }
}
