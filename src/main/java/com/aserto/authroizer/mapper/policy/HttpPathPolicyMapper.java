package com.aserto.authroizer.mapper.policy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Map;

public class HttpPathPolicyMapper implements PolicyMapper {
    private RequestMappingHandlerMapping handlerMapping;

    @Value("${aserto.authorizer.policyRoot}")
    private String policyRoot;

    public HttpPathPolicyMapper(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public String policyPath(HttpServletRequest request) {
        String method = request.getMethod();
        String pattern = "";

        try {
            pattern = extractPattern(request);
        }
        catch (NoMatchingMappingException e) {
            return null;
        }

        String[] tokens = pattern.split("/");
        tokens = Arrays.stream(tokens).filter(token -> !token.isEmpty()).toArray(String[]::new);
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].startsWith("{") && tokens[i].endsWith("}")) {
                tokens[i] = "__" + tokens[i].substring(1, tokens[i].length() - 1);
            }
            tokens[i] = tokens[i].replaceAll("-", "_");
        }

        return policyRoot + "." + method.toUpperCase()  + "." + String.join(".", tokens);
    }

    public String extractPattern(HttpServletRequest request) throws NoMatchingMappingException {
        String uri = request.getRequestURI();
        AntPathMatcher apm = new AntPathMatcher();
        String pattern = "";
        for (Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfo : handlerMapping.getHandlerMethods().entrySet()) {
            PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getKey().getPathPatternsCondition();
            if (pathPatternsCondition == null) {
                continue;
            }

            pattern = pathPatternsCondition.getPatterns().iterator().next().getPatternString();
            if (apm.match(pattern, uri)) {
                return pattern;
            }
        }

        throw new NoMatchingMappingException("Uri" + uri + "does not match any mapping");
    }
}
