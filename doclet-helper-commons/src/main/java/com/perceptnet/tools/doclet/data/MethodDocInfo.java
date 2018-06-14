package com.perceptnet.tools.doclet.data;

import com.perceptnet.tools.doclet.DocInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by vkorovkin on 05.06.2018
 */
public class MethodDocInfo<SELF extends MethodDocInfo> extends BaseItemDocInfo<SELF> {
    private String flatSignature;
    private TypeInfo returnType;

    private final List<ParamDocInfo> params = new ArrayList<>(5);
    private final List<AnnotationInfo> annotations = new ArrayList<>(5);

    public MethodDocInfo() {
    }

    public MethodDocInfo(String name, String rawComment, String flatSignature, TypeInfo returnType) {
        super(name, rawComment);
        this.flatSignature = flatSignature;
        this.returnType = returnType;
    }

    public String getFlatSignature() {
        return flatSignature;
    }

    public SELF setFlatSignature(String flatSignature) {
        this.flatSignature = flatSignature;
        return (SELF) this;
    }

    public TypeInfo getReturnType() {
        return returnType;
    }

    public SELF setReturnType(TypeInfo returnType) {
        this.returnType = returnType;
        return (SELF) this;
    }

    public List<ParamDocInfo> getParams() {
        return params;
    }

    @Override
    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public AnnotationInfo<?> findAnnotation(Class annotationClass) {
        return DocInfoUtils.firstOfType(annotations, annotationClass);
    }

    public boolean hasAnnotation(Class annotationClass) {
        return findAnnotation(annotationClass) != null;
    }
}
