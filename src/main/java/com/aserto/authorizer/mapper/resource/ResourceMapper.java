package com.aserto.authorizer.mapper.resource;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ResourceMapper {
    Map<String, com.google.protobuf.Value> getResource(HttpServletRequest request) throws ResourceMapperError;
}
