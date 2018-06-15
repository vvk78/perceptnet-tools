package com.perceptnet.tools.codegen.rest;

import com.perceptnet.restclient.HttpMethod;
import com.perceptnet.tools.doclet.data.MethodDocInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * created by vkorovkin on 13.06.2018
 */
public class RestMethodInfo {
    private MethodDocInfo serviceMethodDoc;
    private MethodDocInfo controllerMethodDoc;

    private HttpMethod httpMethod;
    /**
     * See {@link RestGenerationHelper#parseRequestMappingStr(String)}
     */
    private List rawRestMappingItems = new ArrayList<>();

    private final List<RestMethodParamInfo> params;

    public RestMethodInfo(MethodDocInfo controllerMethodDoc, HttpMethod httpMethod, List rawRestMappingItems) {
        this.controllerMethodDoc = controllerMethodDoc;
        this.httpMethod = httpMethod;
        this.rawRestMappingItems = rawRestMappingItems;
        this.params = new ArrayList<>(rawRestMappingItems.size() + 3);
    }

    public MethodDocInfo getServiceMethodDoc() {
        return serviceMethodDoc;
    }

    public void setServiceMethodDoc(MethodDocInfo serviceMethodDoc) {
        this.serviceMethodDoc = serviceMethodDoc;
    }

    public MethodDocInfo getControllerMethodDoc() {
        return controllerMethodDoc;
    }

    public void setControllerMethodDoc(MethodDocInfo controllerMethodDoc) {
        this.controllerMethodDoc = controllerMethodDoc;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List getRawRestMappingItems() {
        return rawRestMappingItems;
    }

    public void setRawRestMappingItems(List rawRestMappingItems) {
        this.rawRestMappingItems = rawRestMappingItems;
    }

    public List<RestMethodParamInfo> getParams() {
        return params;
    }

}
