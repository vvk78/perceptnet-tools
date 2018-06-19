package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.commons.utils.ClassUtils;
import com.perceptnet.tools.codegen.rest.RestGenerationHelper;
import com.perceptnet.tools.doclet.DocInfoUtils;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.util.Collection;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 06.12.2017
 */
public class GenerationContext {

    private SvrGenerationAdaptor generationAdaptor = new DefaultSvrGenerationAdaptor();
    private RestGenerationHelper helper = new RestGenerationHelper();

    private GenerationData data;
    private String restServiceProviderQualifiedName;
    private String baseOutputDir;


    GenerationContext(GenerationData data, SvrGenerationAdaptor generationAdaptor) {
        if (data == null) {
            throw new NullPointerException("Data is null");
        }
        this.generationAdaptor = generationAdaptor == null ? new DefaultSvrGenerationAdaptor() : generationAdaptor;
        this.data = data;
    }



    public String getServiceProviderQualifiedName() {
        if (restServiceProviderQualifiedName == null) {
            String result = System.getProperty("restapigen.RestServiceProviderQualifiedName");
            if (result != null) {
                restServiceProviderQualifiedName = result;
            } else {
                String pkg = DocInfoUtils.getMostGeneralPackage(data.getServicesByControllers().values());
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
        return ClassUtils.simpleName(getServiceProviderQualifiedName());
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

    public GenerationData getData() {
        return data;
    }

    SvrGenerationAdaptor getGenerationAdaptor() {
        return generationAdaptor;
    }
}
