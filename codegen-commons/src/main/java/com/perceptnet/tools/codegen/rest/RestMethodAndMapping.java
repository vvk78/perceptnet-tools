package com.perceptnet.tools.codegen.rest;


import com.perceptnet.restclient.dto.HttpMethod;

/**
 * created by vkorovkin on 14.06.2018
 */
public class RestMethodAndMapping {
    private HttpMethod httpMethod;
    private String requestMapping;

    public RestMethodAndMapping(HttpMethod httpMethod, String requestMapping) {
        this.httpMethod = httpMethod;
        this.requestMapping = requestMapping;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }
}
