package com.aserto.authroizer.mapper.extractor;

import javax.servlet.http.HttpServletRequest;

public interface Extractor {
    public String extract(HttpServletRequest request) throws ExtractionError;
}
