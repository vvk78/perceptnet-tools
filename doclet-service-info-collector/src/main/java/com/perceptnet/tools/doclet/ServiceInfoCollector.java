package com.perceptnet.tools.doclet;

import com.perceptnet.tools.doclet.data.AnnotationInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;
import com.perceptnet.tools.doclet.data.ParamDocInfo;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * created by vkorovkin on 05.06.2018
 */
public class ServiceInfoCollector {
    private boolean paramAnnotations;
    private boolean classAnnotations;

    /**
     * Collected information about service interfaces
     *
     * @param rootDoc doclet representation of module
     */
    public Map<String, ClassDocInfo> collectServicesInfo(RootDoc rootDoc) {
        Map<String, ClassDocInfo> result = new HashMap<>(50); //50 is typical number of services in one micro service project
        Stream<ClassDoc> stream = Stream.of(rootDoc.classes());
        stream.filter(classDoc -> classDoc.isInterface() && classDoc.name().endsWith("Service")).forEach(classDoc -> {
            ClassDocInfo ci = new ClassDocInfo();
            ci.setRawComment(classDoc.getRawCommentText());
            ci.setQualifiedName(classDoc.qualifiedName());
            ci.setName(classDoc.name());
            ci.getMethods().addAll(collectMethodInfo(ci, classDoc));
            ci.setInterface(classDoc.isInterface());
            result.put(ci.getQualifiedName(), ci);
        });
        return result;
    }

    /**
     * Collected information about methods of current class
     *
     * @param classDoc doclet representation of class
     */
    private List<MethodDocInfo> collectMethodInfo(ClassDocInfo ci, ClassDoc classDoc) {
        if (classDoc == null || classDoc.methods() == null) {
            return new ArrayList<>(0);
        }
        List<MethodDocInfo> result = new ArrayList<>(classDoc.methods().length);
        for (MethodDoc methodDoc : classDoc.methods()) {
            MethodDocInfo info = new MethodDocInfo();
            info.setRawComment(methodDoc.getRawCommentText());
            info.setName(methodDoc.name());
            info.setFlatSignature(methodDoc.flatSignature());
            info.getParams().addAll(collectParametersInfo(ci, methodDoc));
            info.getAnnotations().addAll(collectAnnotationsInfo(ci, methodDoc.annotations()));
            result.add(info);
        }
        return result;
    }

    /**
     * Collect methods parameters types
     *
     * @param methodDoc       doclet representation of method
     */
    private List<ParamDocInfo> collectParametersInfo(ClassDocInfo ci, MethodDoc methodDoc) {
        if (methodDoc == null || methodDoc.parameters() == null) {
            return new ArrayList<>(0);
        }
        List<ParamDocInfo> result = new ArrayList<>(methodDoc.parameters().length);
        for (Parameter pDoc : methodDoc.parameters()) {
            ParamDocInfo paramInfo = new ParamDocInfo();
            paramInfo.setName(pDoc.name());
            paramInfo.setType(ci.addImportType(pDoc.type().qualifiedTypeName()));
            paramInfo.getAnnotations().addAll(collectAnnotationsInfo(ci, pDoc.annotations()));
            result.add(paramInfo);
        }
        return result;
    }

    /**
     * Collect methods parameters types
     *
     */
    private List<AnnotationInfo> collectAnnotationsInfo(ClassDocInfo ci, AnnotationDesc[] annotations) {
        if (annotations == null) {
            return new ArrayList<>(0);
        }
        List<AnnotationInfo> result = new ArrayList<>(annotations.length);
        for (AnnotationDesc ad : annotations) {
            AnnotationInfo ai = new AnnotationInfo();
            ai.setType(ci.addImportType(ad.annotationType().qualifiedTypeName()));
            ai.setValue(DocletUtils.extractAnnotationValue(null, ad));
            ai.getParams().clear();
            ai.getParams().putAll(DocletUtils.extractAnnotationParams(ad));
            result.add(ai);
        }
        return result;
    }
}
