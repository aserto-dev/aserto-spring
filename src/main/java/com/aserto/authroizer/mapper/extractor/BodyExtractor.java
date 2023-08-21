package com.aserto.authroizer.mapper.extractor;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.stream.Collectors;

public class BodyExtractor implements Extractor {
    @Override
    public String extract(HttpServletRequest request) throws ExtractionError {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new ExtractionError(e);
        }
    }
}
