package com.aserto.example;

import com.aserto.authroizer.mapper.object.ObjectIdMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;


@Component("objIdMapper")
public class PathObjectIdMapper implements ObjectIdMapper {
//    private String id;
//
//    public PathObjectIdMapper setId(String id) {
//        this.id = id;
//        return this;
//    }

    @Override
    public String getValue(HttpServletRequest httpRequest) {
        Map<String, String> pathAttributes = (Map<String, String>)httpRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathAttributes.get("id");
//        return id;
    }
}
