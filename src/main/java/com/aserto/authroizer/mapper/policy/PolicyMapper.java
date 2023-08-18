package com.aserto.authroizer.mapper.policy;

import jakarta.servlet.http.HttpServletRequest;

public interface PolicyMapper {
    String policyPath(HttpServletRequest request);
}
