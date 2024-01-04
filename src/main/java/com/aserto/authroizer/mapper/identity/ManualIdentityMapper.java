package com.aserto.authroizer.mapper.identity;

import com.aserto.authorizer.v2.api.IdentityType;
import com.aserto.model.IdentityCtx;
import jakarta.servlet.http.HttpServletRequest;

public class ManualIdentityMapper implements IdentityMapper {
    private final String identity;
    public ManualIdentityMapper(String identity) {
        this.identity = identity;
    }

    @Override
    public IdentityCtx getIdentity(HttpServletRequest request) throws InvalidIdentity {
        return new IdentityCtx(identity, IdentityType.IDENTITY_TYPE_MANUAL);
    }
}
