package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.commons.utils.MapUtilsJ18;
import com.perceptnet.tools.codegen.rest.RestGenerationHelper;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.perceptnet.commons.utils.CollectionUtilsJ18.*;
import static com.perceptnet.tools.codegen.viarest.spring.SvrUtils.*;

/**
 * created by vkorovkin on 13.06.2018
 */
public class GenerationDataBuilder {
    private GenerationData data;

    private Map<String, ClassDocInfo> servicesOnNames;


    public GenerationDataBuilder(GenerationData data) {
        this.data = data;
    }

    public void build() {
        RestGenerationHelper h = new RestGenerationHelper();
        for (ClassDocInfo<?> cd : data.getControllers()) {
            RestServiceInfo rsi = h.extractRestServiceInfoFromController(cd);
            if (rsi.getRestMethods().isEmpty()) {
                continue; //no rest methods
            }

            MethodDocInfo<?> serviceSetter = extractServiceSetterFromController(cd);
            if (serviceSetter == null) {
                continue; //not a service via rest controller
            }

            String serviceClassName = serviceSetter.getParams().get(0).getType().getQualifiedName();
            obtainServiceDoc(serviceClassName);
        }
    }

    private ClassDocInfo obtainServiceDoc(String serviceClassName) {
        ClassDocInfo serviceDoc = findServiceDoc(serviceClassName);
        if (serviceDoc == null) {
            throw new IllegalStateException("No service doc found for " + serviceClassName);
        }
        return serviceDoc;
    }

    private Map<String, ClassDocInfo> getServicesOnNames() {
        if (servicesOnNames == null) {
            servicesOnNames = MapUtilsJ18.map(data.getServices(), ClassDocInfo::getQualifiedName);
        }
        return servicesOnNames;
    }

    private ClassDocInfo findServiceDoc(String qualifiedName) {
        return getServicesOnNames().get(qualifiedName);
    }

    /**
     * Extracts single service setter method from controller, return null if nothing found or throws
     * {@link CodeConventionException} if there are multiple service setters
     */
    private MethodDocInfo<?> extractServiceSetterFromController(ClassDocInfo<?> cd) {
        MethodDocInfo<?> serviceSetter = null;
        for (MethodDocInfo<?> m : cd.getMethods()) {
            if (m.getName().startsWith("set") && m.getName().contains("Service") && m.hasAnnotation(Autowired.class)) {
                if (serviceSetter != null) {
                    throw svrViolation("Multiple @Autowired service setters in controller " + cd.getQualifiedName());
                }
                serviceSetter = m;
            }
        }
        return serviceSetter;
    }
}
