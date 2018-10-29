package com.perceptnet.tools.restapi.spring;



/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
class ParamInfo implements DocInfo {
    private String name;
    private String type;
    private String qualifiedTypeName;
    private String actualTypeName;
    private String urlPathVariable;
    private String urlParamName;
    private boolean requestBody;

    public ParamInfo() {
    }

    public ParamInfo(String name, String type, String qualifiedTypeName, String actualTypeName) {
        this.actualTypeName = actualTypeName;
        this.qualifiedTypeName = qualifiedTypeName;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActualTypeName() {
        return actualTypeName;
    }

    public void setActualTypeName(String actualTypeName) {
        this.actualTypeName = actualTypeName;
    }

    public String getQualifiedTypeName() {
        return qualifiedTypeName;
    }

    public void setQualifiedTypeName(String qualifiedTypeName) {
        this.qualifiedTypeName = qualifiedTypeName;
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
}
