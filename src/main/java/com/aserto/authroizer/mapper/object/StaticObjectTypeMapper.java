package com.aserto.authroizer.mapper.object;

import jakarta.servlet.http.HttpServletRequest;

public class StaticObjectTypeMapper implements ObjectTypeMapper {
    private String objectType;

    public StaticObjectTypeMapper(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        return objectType;
    }
}
