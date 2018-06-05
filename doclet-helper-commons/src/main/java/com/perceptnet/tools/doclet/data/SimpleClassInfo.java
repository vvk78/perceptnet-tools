package com.perceptnet.tools.doclet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by vkorovkin on 05.06.2018
 */
public class SimpleClassInfo<SELF extends SimpleClassInfo> extends BaseItemInfo<SELF> {
    private static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList(
            new String[]{"byte", "short", "int", "long", "float", "double", "char", "boolean", "void"}));

    private String qualifiedName;

    private Map<String, String> imports = new HashMap<>(50);
    private List<SimpleMethodInfo> methods = new ArrayList<>();

    public SimpleClassInfo() {
    }

    public SimpleClassInfo(String name, String rawComment, String qualifiedName) {
        super(name, rawComment);
        this.qualifiedName = qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public SELF setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        return (SELF) this;
    }

    public Map<String, String> getImports() {
        return imports;
    }

    public List<SimpleMethodInfo> getMethods() {
        return methods;
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
}
