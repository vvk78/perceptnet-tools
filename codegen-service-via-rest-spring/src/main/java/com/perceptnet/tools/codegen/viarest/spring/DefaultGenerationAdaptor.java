package com.perceptnet.tools.codegen.viarest.spring;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public class DefaultGenerationAdaptor implements GenerationAdaptor {
    @Override
    public String getServiceQualifiedNameFromController(String controllerQualifiedName) {
        String result = controllerQualifiedName.replace(".rest.", ".restservice.");
        result = result.replace("Controller", "RestService");
        return result;
    }

    @Override
    public String getRestServiceProviderQualifiedName() {
        return null;
    }
}
