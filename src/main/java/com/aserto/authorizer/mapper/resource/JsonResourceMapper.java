package com.aserto.authorizer.mapper.resource;

import com.aserto.authorizer.mapper.extractor.Extractor;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonResourceMapper implements ResourceMapper {
    private Extractor extractor;
    private String[] jsonElements;

    public JsonResourceMapper(Extractor extractor, String[] jsonElements) {
        this.extractor = extractor;
        this.jsonElements = jsonElements;
    }

    @Override
    public Map<String, com.google.protobuf.Value> getResource(HttpServletRequest request) throws ResourceMapperError {
        Map<String, com.google.protobuf.Value> policyCtx;

        try {
            String jsonBody = extractor.extract(request);

            Struct.Builder structBuilder = Struct.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(jsonBody, structBuilder);
            policyCtx = structBuilder.build().getFieldsMap();
        } catch (IOException e) {
            throw new ResourceMapperError(e);
        }

        Map<String, com.google.protobuf.Value> filteredPolicyCtx = new HashMap<>();
        for (String element : jsonElements) {
            filteredPolicyCtx.put(element, policyCtx.get(element));
        }

        return filteredPolicyCtx;
    }


}
