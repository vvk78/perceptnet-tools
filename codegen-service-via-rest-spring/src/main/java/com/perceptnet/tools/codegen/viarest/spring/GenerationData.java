package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class keeps data about being processed dolcet descriptors.
 *
 * created by vkorovkin on 13.06.2018
 */
public class GenerationData {
    private List<ClassDocInfo> services;
    private List<ClassDocInfo> controllers;

    /**
     * Map of server side rest service descriptors on qualified service class name
     */
    private Map<String, RestServiceInfo> serverParts;

    /**
     * Constructs generation data model from raw doclet data
     *
     * @param services list of service doclet descriptors
     * @param controllers list of controller doclet decriptos
     */
    public GenerationData(List<ClassDocInfo> services, List<ClassDocInfo> controllers) {
        if (services == null) {
            throw new NullPointerException("Services is null");
        }
        if (controllers == null) {
            throw new NullPointerException("Controllers is null");
        }
        this.services = services;
        this.controllers = controllers;

        serverParts = new HashMap<>(Math.max(10, controllers.size()));
    }

    public List<ClassDocInfo> getServices() {
        return services;
    }

    public List<ClassDocInfo> getControllers() {
        return controllers;
    }

    public Map<String, RestServiceInfo> getServerParts() {
        return serverParts;
    }
}
