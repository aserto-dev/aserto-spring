package com.aserto.authroizer.mapper.policy;

public class NoMatchingMappingException extends Exception {
    public NoMatchingMappingException(String errorMessage) {
        super(errorMessage);
    }
}
