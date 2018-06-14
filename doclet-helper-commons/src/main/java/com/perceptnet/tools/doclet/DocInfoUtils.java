package com.perceptnet.tools.doclet;

import com.google.common.base.Joiner;
import com.perceptnet.commons.utils.ClassUtilsJ18;
import com.perceptnet.tools.doclet.data.AnnotationInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * created by vkorovkin on 14.06.2018
 */
public class DocInfoUtils {
    public static String getMostGeneralPackage(Collection<ClassDocInfo<?>> classDocs) {
        return ClassUtilsJ18.mostGeneralPackage(classDocs, ClassDocInfo::getQualifiedName);
    }

    public static AnnotationInfo firstOfType(Collection<AnnotationInfo> annotationInfos, Class annotationClass) {
        for (AnnotationInfo a : annotationInfos) {
            if (a.getType().getQualifiedName().equals(annotationClass.getName())) {
                return a;
            }
        }
        return null;
    }
}
