package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.doclet.data.ClassDocInfo;

/**
 * created by vkorovkin on 13.06.2018
 */
public class GenerationDataBuilder {
    private GenerationData data;

    public GenerationDataBuilder(GenerationData data) {
        this.data = data;
    }

    public void build() {

        for (ClassDocInfo controllerDoc : data.getControllers()) {

        }
    }
}
