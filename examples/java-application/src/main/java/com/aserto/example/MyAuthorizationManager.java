package com.aserto.example;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.MethodInvocationResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class MyAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation invocation) {
        // ... authorization logic
        return new AuthorizationDecision(true);
    }
}
