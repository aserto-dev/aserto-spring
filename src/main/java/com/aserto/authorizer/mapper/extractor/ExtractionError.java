package com.aserto.authorizer.mapper.extractor;

public class ExtractionError extends Error {
    public ExtractionError(String s) {
        super(s);
    }

    public ExtractionError(Throwable throwable) {
        super(throwable);
    }
}
