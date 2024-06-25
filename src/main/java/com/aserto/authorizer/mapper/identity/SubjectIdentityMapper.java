package com.aserto.authorizer.mapper.identity;

import com.aserto.authorizer.mapper.extractor.Extractor;
import com.aserto.authorizer.v2.api.IdentityType;
import com.aserto.model.IdentityCtx;
import jakarta.servlet.http.HttpServletRequest;

public class SubjectIdentityMapper implements IdentityMapper {
    private Extractor extractor;

    public SubjectIdentityMapper(Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public IdentityCtx getIdentity(HttpServletRequest request) {
        String sub = extractor.extract(request);

        return new IdentityCtx(sub, IdentityType.IDENTITY_TYPE_SUB);
    }
}
