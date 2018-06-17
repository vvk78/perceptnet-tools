package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.codegen.rest.RestGenerationHelper;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.DocInfoUtils;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

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

    private SvrGenerationAdaptor generationAdaptor;
    private RestGenerationHelper helper = new RestGenerationHelper();

    private final GenerationData generationData;
    private int totalMethodsCount;
    private String restServiceProviderQualifiedName;
    private String baseOutputDir;

    public GenerationContext(GenerationData generationData) {
        this(generationData, new DefaultGenerationAdaptor());
    }

    public GenerationContext(SvrGenerationAdaptor generationAdaptor, GenerationData generationData) {
        this.generationAdaptor = generationAdaptor;
        this.generationData = generationData;
    }

    public void incMethodsCount() {
        totalMethodsCount++;
    }

    public int getTotalMethodsCount() {
        return totalMethodsCount;
    }

    public Map<ClassDocInfo, RestServiceInfo> getRestServices() {
        return restServices;
    }

    public String getServiceProviderQualifiedName() {
        if (restServiceProviderQualifiedName == null) {
            String result = System.getProperty("restapigen.RestServiceProviderQualifiedName");
            if (result != null) {
                restServiceProviderQualifiedName = result;
            } else {
                String pkg = DocInfoUtils.getMostGeneralPackage(restServices.values());
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

    SvrGenerationAdaptor getGenerationAdaptor() {
        return generationAdaptor;
    }
}
