package com.perceptnet.tools.restapi.spring;

import com.perceptnet.restclient.HttpMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
class MethodInfo implements DocInfo {
    private String name;
    private String flatSignature;
    private String returnType;
    private HttpMethod httpMethod;
    /**
     * See {@link GenerationHelper#parseRequestMapping(String)}
     */
    private List baseRestMappingItems = new ArrayList<>();
    private String rawComment;
    private String actualReturnType;

    private final List<ParamInfo> params = new ArrayList<>();

    public MethodInfo() {
    }

    public MethodInfo(String name, String flatSignature, HttpMethod httpMethod, List baseRestMappingItems, String rawComment, String actualReturnType) {
        this.name = name;
        this.flatSignature = flatSignature;
        this.httpMethod = httpMethod;
        this.baseRestMappingItems = baseRestMappingItems;
        this.rawComment = rawComment;
        this.actualReturnType = actualReturnType;
    }

    public void setRawComment(String rawComment) {
        this.rawComment = rawComment;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRawComment() {
        return rawComment;
    }


    public String getName() {
        return name;
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getActualReturnType() {
        return actualReturnType;
    }

    public void setActualReturnType(String actualReturnType) {
        this.actualReturnType = actualReturnType;
    }

    public String getFlatSignature() {
        return flatSignature;
    }

    public void setFlatSignature(String flatSignature) {
        this.flatSignature = flatSignature;
    }
}
