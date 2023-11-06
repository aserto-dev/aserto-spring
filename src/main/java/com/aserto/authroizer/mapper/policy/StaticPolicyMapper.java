package com.aserto.authroizer.mapper.policy;


import javax.servlet.http.HttpServletRequest;

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
