package com.aserto.authroizer.mapper.extractor;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderExtractor implements Extractor {
    private String headerName;

    public HeaderExtractor(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String extract(HttpServletRequest request) {
        return request.getHeader(headerName);
    }
}
