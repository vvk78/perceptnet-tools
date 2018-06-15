package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.doclet.data.MethodDocInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.perceptnet.tools.codegen.GenerationUtils.simpleName;


/**
 * This class keeps info about being generated rest service.
 *
 * created by vkorovkin (vkorovkin@gmail.com) on 06.12.2017
 *
 * @deprecated remove after refactoring
 */
public class OldRestServiceClientInfo {
    private final String servicePackage;
    private final String serviceQualifiedName;
    private final String serviceSimpleName;

    private Map<String, List<MethodDocInfo>> methodsOnName;
    private int overloadedMethods;
    private int totalMethods;

    public OldRestServiceClientInfo(String serviceQualifiedName) {
        if (serviceQualifiedName == null) {
            throw new NullPointerException("ServiceQualifiedName is null");
        }
        this.serviceQualifiedName = serviceQualifiedName;
        this.servicePackage = serviceQualifiedName.substring(0, Math.max(0, serviceQualifiedName.lastIndexOf(".")));
        this.serviceSimpleName = simpleName(serviceQualifiedName);
        this.methodsOnName = new HashMap<>();
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public String getServiceQualifiedName() {
        return serviceQualifiedName;
    }

    public String getServiceSimpleName() {
        return serviceSimpleName;
    }

    public Map<String, List<MethodDocInfo>> getMethodsOnName() {
        return methodsOnName;
    }

    public void registerMethod(MethodDocInfo m) {
        List<MethodDocInfo> l = methodsOnName.get(m.getName());
        if (l == null) {
            l = new ArrayList<>(5);
            methodsOnName.put(m.getName(), l);
        }
        l.add(m);
        if (l.size() == 2) {
            //the second occurrence of the name makes the first occurrence add 1 to number of overloaded methods
            overloadedMethods = overloadedMethods + 2;
        } else if (l.size() > 2) {
            overloadedMethods++;
        }
        totalMethods++;
    }

    public boolean isHasOverloadedMethods() {
        return overloadedMethods > 0;
    }

    public int getOverloadedMethods() {
        return overloadedMethods;
    }

    public int getSingleMethods() {
        return totalMethods - overloadedMethods;
    }

}
