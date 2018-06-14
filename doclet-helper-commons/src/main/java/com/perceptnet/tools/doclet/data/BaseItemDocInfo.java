package com.perceptnet.tools.doclet.data;

import com.perceptnet.tools.doclet.DocInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for program items (classes, interfaces, methods and instance and class fields)
 *
 * created by vkorovkin on 05.06.2018
 */
public class BaseItemDocInfo<SELF extends BaseItemDocInfo> implements DocInfo {
    private String name;
    private String rawComment;
    private List<AnnotationInfo> annotations = new ArrayList<>(3);

    public BaseItemDocInfo() {
    }

    public BaseItemDocInfo(String name, String rawComment) {
        this.name = name;
        this.rawComment = rawComment;
    }

    public String getRawComment() {
        return rawComment;
    }

    public SELF setRawComment(String rawComment) {
        this.rawComment = rawComment;
        return (SELF) this;
    }

    public String getName() {
        return name;
    }

    public SELF setName(String name) {
        this.name = name;
        return (SELF) this;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public AnnotationInfo<?> findAnnotation(Class annotationClass) {
        return DocInfoUtils.firstOfType(annotations, annotationClass);
    }
}
