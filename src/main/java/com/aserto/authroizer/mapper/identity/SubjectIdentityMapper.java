package com.aserto.authroizer.mapper.identity;

import com.aserto.authorizer.v2.api.IdentityType;
import com.aserto.authroizer.mapper.extractor.Extractor;
import com.aserto.model.IdentityCtx;
import javax.servlet.http.HttpServletRequest;

public class SubjectIdentityMapper implements IdentityMapper{
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
