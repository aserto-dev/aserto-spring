package com.aserto.authorizer.mapper.policy;

public class NoMatchingMappingException extends Exception {
    public NoMatchingMappingException(String errorMessage) {
        super(errorMessage);
    }
}
