package com.aserto.authroizer.config.loader.spring;

public class Address {
    private String host;
    private Integer port;

    public Address(String serviceUrl) {
        if (serviceUrl.contains(":")) {
            String[] tokens = serviceUrl.split(":");
            this.host = tokens[0];
            this.port = Integer.parseInt(tokens[1]);
        } else {
            this.host = serviceUrl;
            this.port = 9292;
        }
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

}
