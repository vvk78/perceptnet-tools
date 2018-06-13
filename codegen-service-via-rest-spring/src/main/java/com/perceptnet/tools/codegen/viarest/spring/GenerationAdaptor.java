package com.perceptnet.tools.codegen.viarest.spring;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public interface GenerationAdaptor {
    /**
     * Gets qualified name of rest service reflecting given controller
     */
    String getServiceQualifiedNameFromController(String controllerQualifiedName);

    /**
     * Returns full qualified name of service provider to be generated. May return null and then the name will be detected by default.
     *
     * Default is
     * - the value of restapi.RestServiceProviderQualifiedName property, if property is defined, otherwise:
     * - the most common package of all rest services (defined with {@link #getServiceQualifiedNameFromController})
     * as package and 'RestServiceProvider' as simple class name.
     */
    String getRestServiceProviderQualifiedName();
}
