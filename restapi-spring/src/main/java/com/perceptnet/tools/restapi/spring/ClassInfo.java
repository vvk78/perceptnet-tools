package com.perceptnet.tools.restapi.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
class ClassInfo implements DocInfo {
    private static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList(
            new String[]{"byte", "short", "int", "long", "float", "double", "char", "boolean", "void"}));

    private String name;
    private String qualifiedName;
    private String baseRestMapping;
    private String rawComment;
    private Map<String, String> imports = new HashMap<>(50);
    private List<MethodInfo> methods = new ArrayList<>();

    public ClassInfo() {
    }

    public ClassInfo(String name, String qualifiedName, String baseRestMapping, String rawComment) {
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.baseRestMapping = baseRestMapping;
        this.rawComment = rawComment;
    }

    /**
     * Adds full type name to imports and returns actual type name to be used in code (it may be short, or may be full if there are synonyms).
     */
    public String addImport(String typeName) {
        String simpleName = typeName.substring(typeName.lastIndexOf(".") + 1, typeName.length());
        if (typeName.startsWith("java.lang.") || PRIMITIVES.contains(typeName)) {
            return simpleName;
        }

        String result = imports.get(simpleName);
        if (result == null) {
            imports.put(simpleName, typeName);
            return simpleName;
        } else {
            if (result.equals(typeName)) {
                return simpleName;
            } else {
                imports.put(typeName, typeName);
                return typeName;
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getBaseRestMapping() {
        return baseRestMapping;
    }

    public String getRawComment() {
        return rawComment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseRestMapping(String baseRestMapping) {
        this.baseRestMapping = baseRestMapping;
    }

    public void setRawComment(String rawComment) {
        this.rawComment = rawComment;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public Map<String, String> getImports() {
        return imports;
    }
}
