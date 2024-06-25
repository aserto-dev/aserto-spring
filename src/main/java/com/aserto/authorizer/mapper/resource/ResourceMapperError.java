package com.aserto.authorizer.mapper.resource;

public class ResourceMapperError extends Error {
    public ResourceMapperError(String errorMessage) {
        super(errorMessage);
    }

    public ResourceMapperError(Throwable err) {
        super(err);
    }
}
