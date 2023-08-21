package com.aserto.authroizer.mapper.identity;

import com.aserto.authorizer.v2.api.IdentityType;
import com.aserto.model.IdentityCtx;
import javax.servlet.http.HttpServletRequest;

public class NoneIdentityMapper implements IdentityMapper {
    @Override
    public IdentityCtx getIdentity(HttpServletRequest request) {
        return new IdentityCtx("", IdentityType.IDENTITY_TYPE_NONE);
    }
}
