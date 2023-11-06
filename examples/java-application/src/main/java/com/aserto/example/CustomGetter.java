package com.aserto.example;

import org.springframework.stereotype.Component;

@Component
public class CustomGetter {
    public String getValue() {
        return "got_value";
    }
}
