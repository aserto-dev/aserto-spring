package com.aserto.authorizer.mapper.policy;


import jakarta.servlet.http.HttpServletRequest;

public interface PolicyMapper {
    String policyPath(HttpServletRequest request);
}
