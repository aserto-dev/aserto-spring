package com.aserto.example;

import com.aserto.authroizer.mapper.check.object.ObjectIdMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;


@Component("objIdMapper")
public class PathObjectIdMapper implements ObjectIdMapper {
    private String attributeName;

    public PathObjectIdMapper fromAttribute(String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        Map<String, String> pathAttributes = (Map<String, String>)httpRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathAttributes.get(attributeName);
    }
}
