package com.perceptnet.tools.codegen;

/**
 * created by vkorovkin on 20.06.2018
 */
public interface BaseGenerationContext {

    default String getEncoding()  {
        return "UTF-8";
    }

    String getBaseOutputDir();


}
