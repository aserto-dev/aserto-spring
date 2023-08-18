package com.aserto.authroizer.mapper.identity;

import com.aserto.model.IdentityCtx;
import jakarta.servlet.http.HttpServletRequest;

public interface IdentityMapper {
    IdentityCtx getIdentity(HttpServletRequest request) throws InvalidIdentity;
}
