package com.aserto.authroizer.mapper.resource;

import com.google.protobuf.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class StaticResourceMapper implements ResourceMapper {

    private Map<String, Value> resources;

    public StaticResourceMapper(Map<String, Value> resources) {
        this.resources = resources;
    }

    @Override
    public Map<String, Value> getResource(HttpServletRequest request) throws ResourceMapperError {
        return resources;
    }
}
