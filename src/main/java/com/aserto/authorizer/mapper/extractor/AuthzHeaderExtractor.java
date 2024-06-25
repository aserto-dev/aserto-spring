package com.aserto.authorizer.mapper.extractor;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public class AuthzHeaderExtractor implements Extractor {
    private String headerName;
    private String claimName;

    public AuthzHeaderExtractor(String headerName, String claimName) {
        this.headerName = headerName;
        this.claimName = claimName;
    }

    @Override
    public String extract(HttpServletRequest request) {
        String jwtString = request.getHeader(headerName);
        String[] authTokens = jwtString.split(" ");

        if (authTokens.length != 2) {
            throw new ExtractionError("Token does not contain a valid format");
        }

        if (!authTokens[0].equals("Bearer")) {
            throw new ExtractionError("Not a Bearer token");
        }

        JWTClaimsSet jwtClaimSet = null;
        try {
            JWT jwt = JWTParser.parse(authTokens[1]);
            jwtClaimSet = jwt.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new ExtractionError(e);
        }

        return jwtClaimSet.getClaim(claimName).toString();
    }
}
