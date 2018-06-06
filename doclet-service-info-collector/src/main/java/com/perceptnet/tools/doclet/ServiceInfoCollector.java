package com.perceptnet.tools.doclet;

import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;
import com.perceptnet.tools.doclet.data.ParamDocInfo;
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
            int initCapacity = classDoc.methods() != null && classDoc.methods().length > 0 ? classDoc.methods().length : 0;

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
            info.getParams().addAll(collectParametersInfo(ci, methodDoc));
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
            paramInfo.setType(pDoc.type().qualifiedTypeName());
            paramInfo.setActualTypeName(ci.addImport(pDoc.type().qualifiedTypeName()));
            result.add(paramInfo);
        }
        return result;
    }
}
