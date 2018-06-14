package com.perceptnet.tools.codegen.rest;

import com.perceptnet.tools.doclet.data.ParamDocInfo;

/**
 * created by vkorovkin on 13.06.2018
 */
public class RestMethodParamInfo {
    private ParamDocInfo controllerParamDoc;
    //private ParamDocInfo serviceParamDoc;

    // Only one of these 3 is defined at a time:
    private String urlPathVariable;
    private String urlParamName;
    private boolean requestBody;

    public static RestMethodParamInfo asRequestParam(ParamDocInfo controllerMethodParamDoc, String paramName) {
        RestMethodParamInfo result = new RestMethodParamInfo(controllerMethodParamDoc);
        result.setUrlParamName(paramName);
        return result;
    }

    public static RestMethodParamInfo asPathVariable(ParamDocInfo controllerMethodParamDoc, String pathVariableName) {
        RestMethodParamInfo result = new RestMethodParamInfo(controllerMethodParamDoc);
        result.setUrlPathVariable(pathVariableName);
        return result;
    }

    public static RestMethodParamInfo asRequestBody(ParamDocInfo controllerMethodParamDoc) {
        RestMethodParamInfo result = new RestMethodParamInfo(controllerMethodParamDoc);
        result.setRequestBody(true);
        return result;
    }

    private RestMethodParamInfo(ParamDocInfo controllerParamDoc) {
        this.controllerParamDoc = controllerParamDoc;
    }

    public String getUrlPathVariable() {
        return urlPathVariable;
    }

    public void setUrlPathVariable(String urlPathVariable) {
        this.urlPathVariable = urlPathVariable;
    }

    public String getUrlParamName() {
        return urlParamName;
    }

    public void setUrlParamName(String urlParamName) {
        this.urlParamName = urlParamName;
    }

    public boolean isRequestBody() {
        return requestBody;
    }

    public void setRequestBody(boolean requestBody) {
        this.requestBody = requestBody;
    }

    public ParamDocInfo getControllerParamDoc() {
        return controllerParamDoc;
    }

    public void setControllerParamDoc(ParamDocInfo controllerParamDoc) {
        this.controllerParamDoc = controllerParamDoc;
    }
}
