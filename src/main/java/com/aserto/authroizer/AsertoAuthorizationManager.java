package com.aserto.authroizer;

import com.aserto.AuthorizerClient;
import com.aserto.authorizer.v2.Decision;
import com.aserto.authroizer.mapper.identity.IdentityMapper;
import com.aserto.authroizer.mapper.identity.InvalidIdentity;
import com.aserto.authroizer.mapper.policy.PolicyMapper;
import com.aserto.authroizer.mapper.resource.ResourceMapper;
import com.aserto.model.IdentityCtx;
import com.aserto.model.PolicyCtx;
import com.google.protobuf.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class AsertoAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final Logger log = LoggerFactory.getLogger(AsertoAuthorizationManager.class);
    private String authorizerDecision;
    private String policyName;
    private String policyLabel;
    boolean authorizerEnabled;
    private IdentityMapper identityMapper;
    private PolicyMapper policyMapper;
    private ResourceMapper resourceMapper;
    private AuthorizerClient authzClient;

    public AsertoAuthorizationManager(AuthzConfig authzConfig) {
        this.identityMapper = authzConfig.getIdentityMapper();
        this.policyMapper = authzConfig.getPolicyMapper();
        this.resourceMapper = authzConfig.getResourceMapper();
        this.authzClient = authzConfig.getAuthzClient();
        this.authorizerDecision = authzConfig.getAuthorizerDecision();
        this.policyName = authzConfig.getPolicyName();
        this.policyLabel = authzConfig.getPolicyLabel();
        this.authorizerEnabled = authzConfig.isAuthorizerEnabled();

        if (!authorizerEnabled) {
            log.debug("Aserto authorization is disabled");
        }
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        if (!authorizerEnabled) {
            return new AuthorizationDecision(true);
        }

        IdentityCtx identityCtx;
        HttpServletRequest httpRequest = context.getRequest();

        try {
            identityCtx = identityMapper.getIdentity(httpRequest);
            log.debug("Identity is: [{}] of type: [{}]",  identityCtx.getIdentity(), identityCtx.getIdentityType());
        } catch (InvalidIdentity e) {
            log.error("Invalid identity [{}]",  e.getMessage());
            return new AuthorizationDecision(false);
        }

        String policyPath = policyMapper.policyPath(httpRequest);
        log.debug("Policy path is [{}]", policyPath);
        PolicyCtx policyCtx = new PolicyCtx(policyName, policyLabel, policyPath, new String[]{ authorizerDecision });
        Map<String, Value> resourceCtx = resourceMapper.getResource(httpRequest);

        boolean isAllowed = false;
        try {
            List<Decision> decisions = authzClient.is(identityCtx, policyCtx, resourceCtx);
            isAllowed = isAllowed(decisions);
        } catch (Exception e) {
            log.error("Is call failed [{}]",  e.getMessage());
            return new AuthorizationDecision(false);
        }


        return new AuthorizationDecision(isAllowed);
    }

    private boolean isAllowed(List<Decision> decisions) {
        for (Decision decision: decisions) {
            String dec = decision.getDecision();
            log.debug("For decision [{}] the answer was [{}]", dec, decision.getIs());
            if (dec.equals(authorizerDecision)) {
                return decision.getIs();
            }
        }

        return false;
    }
}
