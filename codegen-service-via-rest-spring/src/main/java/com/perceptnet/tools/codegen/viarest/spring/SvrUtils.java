package com.perceptnet.tools.codegen.viarest.spring;

/**
 * created by vkorovkin on 14.06.2018
 */
class SvrUtils {
    /**
     * Synonym for shorteness
     */
    static CodeConventionException svrViolation(String msg) {
        return new CodeConventionException(msg);
    }
}
