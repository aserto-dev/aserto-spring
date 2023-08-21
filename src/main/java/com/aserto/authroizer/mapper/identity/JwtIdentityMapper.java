package com.aserto.authroizer.mapper.identity;

import com.aserto.authorizer.v2.api.IdentityType;
import com.aserto.authroizer.mapper.extractor.Extractor;
import com.aserto.model.IdentityCtx;
import javax.servlet.http.HttpServletRequest;

public class JwtIdentityMapper implements IdentityMapper {
    private Extractor extractor;
    public JwtIdentityMapper(Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public IdentityCtx getIdentity(HttpServletRequest request) throws InvalidIdentity {
        String authToken = extractor.extract(request);
        String[] authTokens = authToken.split(" ");

        if (authTokens.length != 2) {
            throw new InvalidIdentity("Token does not contain a valid format");
        }

        if (!authTokens[0].equals("Bearer")) {
            throw new InvalidIdentity("Not a Bearer token");
        }

        return new IdentityCtx(authTokens[1], IdentityType.IDENTITY_TYPE_JWT);
    }
}
