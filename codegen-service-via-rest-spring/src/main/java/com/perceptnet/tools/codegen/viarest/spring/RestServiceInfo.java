package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by vkorovkin on 13.06.2018
 */
public class RestServiceInfo {
    private ClassDocInfo serviceDoc;
    private ClassDocInfo controllerDoc;

    private Map<String, List<MethodDocInfo>> methodsOnName = new HashMap<>(20);
    private int overloadedMethods;
    private int totalMethods;

    public RestServiceInfo() {
    }
}
