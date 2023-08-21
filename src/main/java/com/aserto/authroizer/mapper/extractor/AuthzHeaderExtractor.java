package com.aserto.authroizer.mapper.extractor;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;

public class AuthzHeaderExtractor implements Extractor {
    @Override
    public String extract(HttpServletRequest request) {
        String jwtString = request.getHeader("Authorization");
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

        return jwtClaimSet.getClaim("sub").toString();
    }
}
