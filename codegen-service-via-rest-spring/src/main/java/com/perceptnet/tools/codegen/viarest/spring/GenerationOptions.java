package com.perceptnet.tools.codegen.viarest.spring;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 20.12.2017
 */
class GenerationOptions {
    private boolean generate;
    private SvrGenerationAdaptor adaptor;



    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public SvrGenerationAdaptor getAdaptor() {
        return adaptor;
    }

    public void installAdaptor(String adaptorQualifiedName) {
        if (adaptorQualifiedName == null || adaptorQualifiedName.trim().isEmpty()) {
            this.adaptor = null;
            return;
        }
        Class adaptorClass;
        try {
            adaptorClass = Class.forName(adaptorQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load specified generation adaptor class '" + adaptorQualifiedName + "' due to " + e, e);
        }
        try {
            this.adaptor = (SvrGenerationAdaptor) adaptorClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate generation adaptor '" + adaptorQualifiedName + "' due to " + e, e);
        }
    }
}
