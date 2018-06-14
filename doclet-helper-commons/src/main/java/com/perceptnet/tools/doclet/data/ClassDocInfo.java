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

    public boolean isInterface() {
        return isInterface;
    }

    public SELF setInterface(boolean anInterface) {
        isInterface = anInterface;
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
    private String addImport(String qualifiedTypeName) {
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

    public TypeInfo addImportType(String qualifiedTypeName) {
        return new TypeInfo(qualifiedTypeName, addImport(qualifiedTypeName));
    }

}
