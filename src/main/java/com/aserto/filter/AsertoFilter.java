package com.aserto.filter;

import com.aserto.AuthorizerClient;
import com.aserto.authorizer.v2.Decision;
import com.aserto.model.IdentityCtx;
import com.aserto.model.PolicyCtx;
import com.aserto.authroizer.mapper.identity.IdentityMapper;
import com.aserto.authroizer.mapper.identity.InvalidIdentity;
import com.aserto.authroizer.mapper.policy.PolicyMapper;
import com.aserto.authroizer.mapper.resource.ResourceMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Component
public class AsertoFilter extends GenericFilterBean {
    @Value("${aserto.authorizer.decision}")
    private String authorizerDecision;

    @Value("${aserto.authorizer.policyName}")
    private String policyName;

    @Value("${aserto.authorizer.policyLabel}")
    private String policyLabel;

    @Value("${aserto.authorization.enabled}")
    boolean authorizerEnabled;

    private final Logger log = LoggerFactory.getLogger(AsertoFilter.class);
    private IdentityMapper identityMapper;
    private PolicyMapper policyMapper;
    private ResourceMapper resourceMapper;
    private AuthorizerClient authzClient;

    public AsertoFilter(IdentityMapper identityMapper, PolicyMapper policyMapper, ResourceMapper resourceMapper,
                        AuthorizerClient authzClient) {
        this.identityMapper = identityMapper;
        this.policyMapper = policyMapper;
        this.resourceMapper = resourceMapper;
        this.authzClient = authzClient;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (!authorizerEnabled) {
            log.debug("Aserto authorization is disabled");
            chain.doFilter(request, response);
        }

        IdentityCtx identityCtx;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            identityCtx = identityMapper.getIdentity(httpRequest);
            log.debug("Identity is: [{}] of type: [{}]",  identityCtx.getIdentity(), identityCtx.getIdentityType());
        } catch (InvalidIdentity e) {
            unauthorized(response, e.getMessage());
            return;
        }

        String policyPath = policyMapper.policyPath(httpRequest);
        log.debug("Policy path is [{}]", policyPath);
        PolicyCtx policyCtx = new PolicyCtx(policyName, policyLabel, policyPath, new String[]{ authorizerDecision });
        Map<String, com.google.protobuf.Value> resourceCtx = resourceMapper.getResource(httpRequest);;

        boolean isAllowed = false;
        try {
            List<Decision> decisions = authzClient.is(identityCtx, policyCtx, resourceCtx);
            isAllowed = isAllowed(decisions);
        } catch (Exception e) {
            throw new ServletException(e);
        }


        if (isAllowed) {
            chain.doFilter(request, response);
        }
        else {
            unauthorized(response, "Access denied");
        }
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

    private void unauthorized(ServletResponse response, String message) throws IOException {
        HttpServletResponse httpResponse =(HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        httpResponse.setContentType("text/html");
        PrintWriter writer = httpResponse.getWriter();
        writer.printf("<h1>401 - Unauthorized - %s </h1>", message);
    }
}