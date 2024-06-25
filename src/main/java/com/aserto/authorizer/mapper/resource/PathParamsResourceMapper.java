package com.aserto.authorizer.mapper.resource;

import com.google.protobuf.Value;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathParamsResourceMapper implements ResourceMapper {
    private final RequestMappingHandlerMapping handlerMapping;
    public PathParamsResourceMapper(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public Map<String, Value> getResource(HttpServletRequest request) throws ResourceMapperError {
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
                return extractParamsMap(pattern, uri, apm);
            }
        }

        throw new ResourceMapperError("Uri " + uri + " does not match any mapping");
    }

    private Map<String, Value> extractParamsMap(String pattern, String uri, AntPathMatcher apm) {
        Map<String, Value> paramsMapping = new HashMap<>();
        String[] pathParams = getPathParams(pattern);
        for (String param : pathParams) {
            String paramValue = apm.extractUriTemplateVariables(pattern, uri).get(param);
            paramsMapping.put(param, Value.newBuilder().setStringValue(paramValue).build());
        }

        return paramsMapping;
    }

    public String[] getPathParams(String uri) {
        List<String> params = new ArrayList<>();
        String[] tokens = uri.split("/");
        for (String token : tokens) {
            if (token.startsWith("{") && token.endsWith("}")) {
                params.add(token.substring(1, token.length() - 1));
            }
        }

        return params.toArray(new String[0]);
    }
}
