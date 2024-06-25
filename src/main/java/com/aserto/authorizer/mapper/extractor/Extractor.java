package com.aserto.authorizer.mapper.extractor;


import jakarta.servlet.http.HttpServletRequest;

public interface Extractor {
    public String extract(HttpServletRequest request) throws ExtractionError;
}
