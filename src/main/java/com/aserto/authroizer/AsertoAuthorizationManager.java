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
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/*
 * Provides methods to check if the current user is authorized to perform an action.
 */
public final class AsertoAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final Logger log = LoggerFactory.getLogger(AsertoAuthorizationManager.class);
    private String authorizerDecision;
    private String policyName;
    private String policyLabel;
    boolean authorizerEnabled;
    private IdentityMapper configIdentityMapper;
    private PolicyMapper configPolicyMapper;
    private ResourceMapper configResourceMapper;
    private AuthorizerClient authzClient;

    public AsertoAuthorizationManager(AuthzConfig authzConfig) {
        this.configIdentityMapper = authzConfig.getIdentityMapper();
        this.configPolicyMapper = authzConfig.getPolicyMapper();
        this.configResourceMapper = authzConfig.getResourceMapper();
        this.authzClient = authzConfig.getAuthzClient();
        this.authorizerDecision = authzConfig.getAuthorizerDecision();
        this.policyName = authzConfig.getPolicyName();
        this.policyLabel = authzConfig.getPolicyLabel();
        this.authorizerEnabled = authzConfig.isAuthorizerEnabled();

        if (!authorizerEnabled) {
            log.debug("Aserto authorization is disabled");
        }
    }

    /*
    * Check if the current user is authorized to perform the action defined in the policy.
    * The resource mapper and policy mappers that were set in the constructor are used.
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        return this.check(context.getRequest());
    }

    /*
     * Check if the current user is authorized to perform the action defined in the policy.
     * The resource mapper and policy mappers that were set in the constructor are used.
     *
     * @param httpRequest The http request
     * @return true if the user is authorized, false otherwise
     */
    public AuthorizationDecision check(HttpServletRequest httpRequest) {
        return this.check(httpRequest, configPolicyMapper, configResourceMapper);
    }

    /*
     * Check if the current user is authorized to perform the action defined in the policy.
     *
     * @param httpRequest The http request
     * @param policyMapper The policy mapper
     * @param resourceMapper The resource mapper
     * @return true if the user is authorized, false otherwise
     */
    public AuthorizationDecision check(HttpServletRequest httpRequest, PolicyMapper policyMapper, ResourceMapper resourceMapper) {
        return this.check(httpRequest, configIdentityMapper, policyMapper, resourceMapper);
    }

    /*
     * Check if the current user is authorized to perform the action defined in the policy.
     *
     * @param httpRequest The http request
     * @param identityMapper The identity mapper
     * @param policyMapper The policy mapper
     * @param resourceMapper The resource mapper
     * @return true if the user is authorized, false otherwise
     */
    public AuthorizationDecision check(HttpServletRequest httpRequest, IdentityMapper identityMapper, PolicyMapper policyMapper, ResourceMapper resourceMapper) {
        if (!authorizerEnabled) {
            return new AuthorizationDecision(true);
        }

        IdentityCtx identityCtx;

        try {
            identityCtx = identityMapper.getIdentity(httpRequest);
            log.debug("Identity is: [{}] of type: [{}]",  identityCtx.getIdentity(), identityCtx.getIdentityType());
        } catch (InvalidIdentity e) {
            log.error("Invalid identity [{}]",  e.getMessage());
            return new AuthorizationDecision(false);
        }

        String policyPath = policyMapper.policyPath(httpRequest);
        log.debug("Policy path is [{}], policy name is [{}], policy label is [{}] and decision is [{}]", policyPath, policyName, policyLabel , authorizerDecision);
        PolicyCtx policyCtx = new PolicyCtx(policyName, policyLabel, policyPath, new String[]{ authorizerDecision });
        Map<String, Value> resourceCtx = resourceMapper.getResource(httpRequest);
        log.debug("Resource context: [{}]", toResourceContextString(resourceCtx));

        boolean isAllowed = false;
        try {
            List<Decision> decisions = authzClient.is(identityCtx, policyCtx, resourceCtx);
            isAllowed = isAllowed(decisions);
        } catch (Exception e) {
            log.error("Is call failed",  e);
            return new AuthorizationDecision(false);
        }


        return new AuthorizationDecision(isAllowed);
    }

    /*
    * Convert a resource context to a string.
    *
    * @param resourceCtx The resource context
    * @return The string representation of the resource context
     */
    private String toResourceContextString(Map<String, Value> resourceCtx) {
        List<String> resourceContextStrings = new LinkedList<>();
        for (Map.Entry<String, Value> entry : resourceCtx.entrySet()) {
            String keyValuePair = String.format("%s:%s", entry.getKey(), entry.getValue().getStringValue());
            resourceContextStrings.add(keyValuePair);
        }

        return String.join(", ", resourceContextStrings);
    }

    /*
    * Extract the first is result from a decision list.
    *
    * @param decisions The list of decisions
    * @return true if the decision is allowed, false otherwise
     */
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
