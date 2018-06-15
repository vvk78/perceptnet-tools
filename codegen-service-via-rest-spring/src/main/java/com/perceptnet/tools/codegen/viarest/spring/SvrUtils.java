package com.perceptnet.tools.codegen.viarest.spring;

/**
 * SVR stands for Service Via Rest
 * created by vkorovkin on 14.06.2018
 */
class SvrUtils {
    /**
     * Synonym for shorteness
     */
    static SvrCodeConventionException svrViolation(String msg) {
        return new SvrCodeConventionException(msg);
    }
}
