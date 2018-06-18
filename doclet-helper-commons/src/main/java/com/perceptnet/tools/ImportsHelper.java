package com.perceptnet.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * created by vkorovkin on 18.06.2018
 */
public class ImportsHelper {
    private static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList(
            new String[]{"byte", "short", "int", "long", "float", "double", "char", "boolean", "void"}));

    private Map<String, String> imports = new HashMap<>(50);

    /**
     * Adds full type name to imports and returns actual type name to be used in code (it may be short, or may be full if there are synonyms).
     */
    public String addImport(String qualifiedTypeName) {
        String simpleName = qualifiedTypeName.substring(qualifiedTypeName.lastIndexOf(".") + 1, qualifiedTypeName.length());
        if (qualifiedTypeName.startsWith("java.lang.") || PRIMITIVES.contains(qualifiedTypeName)) {
            return simpleName;
        }

        String result = imports.get(simpleName);
        if (result == null) {
            imports.put(simpleName, qualifiedTypeName);
            return simpleName;
        } else {
            if (result.equals(qualifiedTypeName)) {
                return simpleName;
            } else {
                imports.put(qualifiedTypeName, qualifiedTypeName);
                return qualifiedTypeName;
            }
        }
    }

    public Map<String, String> getImports() {
        return imports;
    }

    public String actualName(String qualifiedName) {
        String result = imports.get(qualifiedName);
        if (result == null) {
            throw new IllegalStateException("Class " + qualifiedName + " is not imported");
        }
        return result;
    }
}
