package com.aserto.example;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("authz")
class AuthorizationLogic {
    public boolean check(String objectKey, String ObjectId, String relation) {
        // ... authorization logic
        System.out.println("AuthorizationLogic.decide");
        return true;
    }
}
