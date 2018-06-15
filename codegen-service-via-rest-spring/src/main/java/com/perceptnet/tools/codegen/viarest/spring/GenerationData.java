package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class keeps data about being processed dolcet descriptors.
 *
 * created by vkorovkin on 13.06.2018
 */
class GenerationData {
    private Collection<ClassDocInfo> services;
    private Collection<ClassDocInfo> controllers;

    /**
     * Map of server side rest service descriptors on qualified service class name
     */
    private Map<String, RestServiceInfo> restServices;

    /**
     * Constructs generation data model from raw doclet data
     *
     * @param services list of service doclet descriptors
     * @param controllers list of controller doclet decriptos
     */
    public GenerationData(Collection<ClassDocInfo> controllers, Collection<ClassDocInfo> services) {
        if (controllers == null) {
            throw new NullPointerException("Controllers is null");
        }
        if (services == null) {
            throw new NullPointerException("Services is null");
        }
        this.controllers = controllers;
        this.services = services;

        this.restServices = new HashMap<>(Math.max(10, controllers.size()));
    }

    public Collection<ClassDocInfo> getServices() {
        return services;
    }

    public Collection<ClassDocInfo> getControllers() {
        return controllers;
    }

    public Map<String, RestServiceInfo> getRestServices() {
        return restServices;
    }
}
