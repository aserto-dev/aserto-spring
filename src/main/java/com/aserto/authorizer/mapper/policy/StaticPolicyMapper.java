package com.aserto.authorizer.mapper.policy;


import jakarta.servlet.http.HttpServletRequest;

public class StaticPolicyMapper implements PolicyMapper {

    private String policyPath;

    public StaticPolicyMapper(String policyPath) {
        this.policyPath = policyPath;
    }

    @Override
    public String policyPath(HttpServletRequest request) {
        return policyPath;
    }
}
