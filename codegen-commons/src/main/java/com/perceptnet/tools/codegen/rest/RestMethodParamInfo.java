package com.perceptnet.tools.codegen.rest;

import com.perceptnet.tools.doclet.data.ParamDocInfo;

/**
 * created by vkorovkin on 13.06.2018
 */
public class RestMethodParamInfo {
    private ParamDocInfo paramDoc;

    private String urlPathVariable;
    private String urlParamName;
    private boolean requestBody;

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

    public ParamDocInfo getParamDoc() {
        return paramDoc;
    }

    public void setParamDoc(ParamDocInfo paramDoc) {
        this.paramDoc = paramDoc;
    }
}
