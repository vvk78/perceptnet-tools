package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.restclient.HttpMethod;
import com.perceptnet.tools.codegen.rest.RestMethodParamInfo;
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
     * See {@link GenerationHelper#parseRequestMapping(String)}
     */
    private List baseRestMappingItems = new ArrayList<>();

    private List<RestMethodParamInfo> params = new ArrayList<>(5);

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

    public List getBaseRestMappingItems() {
        return baseRestMappingItems;
    }

    public void setBaseRestMappingItems(List baseRestMappingItems) {
        this.baseRestMappingItems = baseRestMappingItems;
    }

    public List<RestMethodParamInfo> getParams() {
        return params;
    }
}
