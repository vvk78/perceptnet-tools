package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.commons.utils.MapUtils;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.util.Map;

/**
 * created by vkorovkin on 18.06.2018
 */
class GenerationData {
    private Map<ClassDocInfo<?>, ClassDocInfo<?>> servicesByControllers;
    private Map<ClassDocInfo<?>, ClassDocInfo<?>> controllersByServices;

    private Map<String, ClassDocInfo> servicesOnNames;

    private Map<String, RestServiceInfo> restServicesByServiceName;

    public GenerationData(Map<ClassDocInfo<?>, ClassDocInfo<?>> servicesByControllers,
                                Map<String, ClassDocInfo> servicesByNames,
                                    Map<String, RestServiceInfo> restServicesByServiceName) {
        this.servicesByControllers = servicesByControllers;
        this.controllersByServices = MapUtils.changeKeyAndValues(servicesByControllers);
        this.servicesOnNames = servicesByNames;
        this.restServicesByServiceName = restServicesByServiceName;
    }

    public Map<ClassDocInfo<?>, ClassDocInfo<?>> getServicesByControllers() {
        return servicesByControllers;
    }

    public Map<ClassDocInfo<?>, ClassDocInfo<?>> getControllersByServices() {
        return controllersByServices;
    }

    public Map<String, ClassDocInfo> getServicesOnNames() {
        return servicesOnNames;
    }

    public Map<String, RestServiceInfo> getRestServicesByServiceName() {
        return restServicesByServiceName;
    }
}
