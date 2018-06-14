package com.perceptnet.tools.codegen.rest;

import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by vkorovkin on 13.06.2018
 */
public class RestServiceInfo {
    private ClassDocInfo serviceDoc;
    private ClassDocInfo controllerDoc;

    private String controllerBaseRestMapping;

    private List<RestMethodInfo> restMethods;

    public RestServiceInfo() {
    }

    public RestServiceInfo(ClassDocInfo controllerDoc) {
        this(controllerDoc, null);
    }

    public RestServiceInfo(ClassDocInfo controllerDoc, String controllerBaseRestMapping) {
        this.controllerDoc = controllerDoc;
        this.controllerBaseRestMapping = controllerBaseRestMapping;
        this.restMethods = new ArrayList<>(controllerDoc.getMethods().size());
    }

    public List<RestMethodInfo> getRestMethods() {
        return restMethods;
    }

    public String getControllerBaseRestMapping() {
        return controllerBaseRestMapping;
    }

    public void setControllerBaseRestMapping(String controllerBaseRestMapping) {
        this.controllerBaseRestMapping = controllerBaseRestMapping;
    }

    public ClassDocInfo getServiceDoc() {
        return serviceDoc;
    }

    public void setServiceDoc(ClassDocInfo serviceDoc) {
        this.serviceDoc = serviceDoc;
    }

    public ClassDocInfo getControllerDoc() {
        return controllerDoc;
    }

    public void setControllerDoc(ClassDocInfo controllerDoc) {
        this.controllerDoc = controllerDoc;
    }

}
