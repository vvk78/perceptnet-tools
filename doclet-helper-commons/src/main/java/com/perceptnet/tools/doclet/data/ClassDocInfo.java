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
public class ClassDocInfo<SELF extends ClassDocInfo> extends BaseItemDocInfo<SELF> {
    private static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList(
            new String[]{"byte", "short", "int", "long", "float", "double", "char", "boolean", "void"}));

    private String qualifiedName;
    private boolean isInterface;

    private Map<String, String> imports = new HashMap<>(50);
    private List<MethodDocInfo> methods = new ArrayList<>();

    public ClassDocInfo() {
    }

    public ClassDocInfo(String name, String rawComment, String qualifiedName, boolean isInterface) {
        super(name, rawComment);
        this.qualifiedName = qualifiedName;
        this.isInterface = isInterface;
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

    public List<MethodDocInfo> getMethods() {
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
