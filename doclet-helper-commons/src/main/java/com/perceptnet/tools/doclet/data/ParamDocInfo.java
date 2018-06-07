package com.perceptnet.tools.doclet.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple param info, does not extends BaseItemDocInfo (todo -- may it worth doing that?)
 *
 * created by vkorovkin on 05.06.2018
 */
public class ParamDocInfo<SELF extends ParamDocInfo> implements DocInfo {
    private String name;
    private TypeInfo type;
    private List<AnnotationInfo> annotations = new ArrayList<>(0);

    public ParamDocInfo() {
    }

    public ParamDocInfo(String name, TypeInfo typeInfo) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SELF setName(String name) {
        this.name = name;
        return (SELF) this;
    }

    public TypeInfo getType() {
        return type;
    }

    public SELF setType(TypeInfo type) {
        this.type = type;
        return (SELF) this;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }
}
