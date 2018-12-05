package com.perceptnet.tools.doclet.data;


import com.perceptnet.commons.utils.ImportsHelper;

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


    private String qualifiedName;
    private boolean isInterface;

    private ImportsHelper imports = new ImportsHelper();
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


    public List<MethodDocInfo> getMethods() {
        return methods;
    }


    public TypeInfo addImportType(String qualifiedTypeName) {
        return new TypeInfo(qualifiedTypeName, imports.addImport(qualifiedTypeName));
    }

    @Override
    public String toString() {
        return qualifiedName;
    }
}
