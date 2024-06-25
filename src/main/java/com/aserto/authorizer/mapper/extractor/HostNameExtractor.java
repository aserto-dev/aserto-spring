package com.aserto.authorizer.mapper.extractor;

import jakarta.servlet.http.HttpServletRequest;

public class HostNameExtractor implements Extractor{

    private int segment;

    public HostNameExtractor(int segment) {
        this.segment = segment;
    }

    @Override
    public String extract(HttpServletRequest request) throws ExtractionError {
        String hostname = request.getServerName();
        String[] hostnameTokens = hostname.split("\\.");

        // index from right to left
        if (segment < 0) {
            if (hostnameTokens.length + segment < 0) {
                throw new ExtractionError("Hostname does not contain enough segments for negative index");
            }
            return hostnameTokens[hostnameTokens.length + segment];
        }

        if (hostnameTokens.length <= segment) {
            throw new ExtractionError("Hostname does not contain enough segments");
        } else {
            // index from left to right
            return hostnameTokens[hostnameTokens.length - segment - 1];
        }
    }
}
