package com.perceptnet.tools.doclet.data;

import java.util.HashMap;
import java.util.Map;

/**
 * created by vkorovkin on 06.06.2018
 */
public class AnnotationInfo<SELF extends AnnotationInfo> {
    private TypeInfo type;
    private String value;
    private Map<String, Object> params = new HashMap<>();

    public AnnotationInfo() {
    }

    public AnnotationInfo(TypeInfo type) {
        this.type = type;
    }

    public AnnotationInfo(TypeInfo type, String value) {
        this.type = type;
        this.value = value;
    }

    public TypeInfo getType() {
        return type;
    }

    public SELF setType(TypeInfo type) {
        this.type = type;
        return (SELF) this;
    }

    public String getValue() {
        return value;
    }

    public SELF setValue(String value) {
        this.value = value;
        return (SELF) this;
    }

    @Override
    public String toString() {
        if (value != null) {
            return "@" + type + "(value='" + value + '\'' + '}';
        } else {
            return "@" + type;
        }
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
