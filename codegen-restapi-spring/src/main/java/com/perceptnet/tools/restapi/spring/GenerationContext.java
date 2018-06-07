package com.perceptnet.tools.restapi.spring;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 06.12.2017
 */
public class GenerationContext {
    /**
     * Encoding sources are generated in.
     * todo To be made a setting.
     */
    private String encoding = "UTF-8";

    private GenerationAdaptor generationAdaptor;
    private GenerationHelper helper = GenerationHelper.I;
    private int totalMethodsCount;
    private Map<ClassInfo, RestServiceInfo> restServices = new IdentityHashMap<>(100);
    private String restServiceProviderQualifiedName;
    private String baseOutputDir;

    public GenerationContext() {
        this(new DefaultGenerationAdaptor());
    }

    public GenerationContext(GenerationAdaptor generationAdaptor) {
        this.generationAdaptor = generationAdaptor;
    }

    public void incMethodsCount() {
        totalMethodsCount++;
    }

    public int getTotalMethodsCount() {
        return totalMethodsCount;
    }

    public Map<ClassInfo, RestServiceInfo> getRestServices() {
        return restServices;
    }

    public String getServiceProviderQualifiedName() {
        if (restServiceProviderQualifiedName == null) {
            String result = System.getProperty("restapigen.RestServiceProviderQualifiedName");
            if (result != null) {
                restServiceProviderQualifiedName = result;
            } else {
                String pkg = helper.getMostGeneralPackage(restServices.values());
                if (pkg != null) {
                    restServiceProviderQualifiedName = pkg + ".RestServiceProvider";
                } else {
                    restServiceProviderQualifiedName = "RestServiceProvider";
                }
            }
        }
        return restServiceProviderQualifiedName;
    }

    public String getServiceProviderPackage() {
        String s = getServiceProviderQualifiedName();
        int idx = s.lastIndexOf(".");
        if (idx == -1) {
            return "";
        }
        return s.substring(0, idx);
    }

    public String getRegistrySimpleName() {
        return "RestMethodRegistry";
    }

    public String getRegistryQualifiedName() {
        String pkg = getServiceProviderPackage();
        if (!pkg.isEmpty()) {
            return pkg +"." + getRegistrySimpleName();
        }
        return getRegistrySimpleName();
    }


    public String getServiceProviderSimpleName() {
        String s = getServiceProviderQualifiedName();
        int idx = s.lastIndexOf(".");
        if (idx == -1) {
            return s;
        }
        return s.substring(idx + 1);
    }

    public String getEncoding() {
        return encoding;
    }

    public String getBaseOutputDir() {
        if (baseOutputDir == null) {
            String str = System.getProperty("restapigen.BaseOutputDir");
            if (str != null) {
                baseOutputDir = str;
            }
        }
        return baseOutputDir != null ? baseOutputDir : "";
    }

    public void setBaseOutputDir(String baseOutputDir) {
        this.baseOutputDir = baseOutputDir;
    }

    GenerationAdaptor getGenerationAdaptor() {
        return generationAdaptor;
    }
}
