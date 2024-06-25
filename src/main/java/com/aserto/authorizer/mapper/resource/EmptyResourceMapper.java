package com.aserto.authorizer.mapper.resource;


import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class EmptyResourceMapper implements ResourceMapper {
    @Override
    public Map<String, com.google.protobuf.Value> getResource(HttpServletRequest request) {
        return new HashMap<>();
    }
}
