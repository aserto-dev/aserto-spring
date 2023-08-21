package com.aserto.authroizer.mapper.policy;

import javax.servlet.http.HttpServletRequest;

public interface PolicyMapper {
    String policyPath(HttpServletRequest request);
}
