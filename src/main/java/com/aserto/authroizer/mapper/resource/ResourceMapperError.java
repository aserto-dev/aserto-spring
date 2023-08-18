package com.aserto.authroizer.mapper.resource;

public class ResourceMapperError extends Error {
    public ResourceMapperError(String errorMessage) {
        super(errorMessage);
    }

    public ResourceMapperError(Throwable err) {
        super(err);
    }
}
