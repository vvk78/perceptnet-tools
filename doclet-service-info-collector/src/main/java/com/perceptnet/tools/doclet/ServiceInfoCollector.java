package com.perceptnet.tools.doclet;

import com.perceptnet.commons.utils.MiscUtils;
import com.perceptnet.tools.doclet.data.SimpleClassInfo;
import com.perceptnet.tools.doclet.data.SimpleMethodInfo;
import com.perceptnet.tools.doclet.data.SimpleParamInfo;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;

import java.util.ArrayList;
import java.util.Collection;
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
    public Map<String, SimpleClassInfo> collectServicesInfo(RootDoc rootDoc) {
        Map<String, SimpleClassInfo> result = new HashMap<>(50); //50 is typical number of services in one micro service project
        Stream<ClassDoc> stream = Stream.of(rootDoc.classes());
        stream.filter(classDoc -> classDoc.isInterface() && classDoc.name().endsWith("Service")).forEach(classDoc -> {
            SimpleClassInfo ci = new SimpleClassInfo();
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
    private List<SimpleMethodInfo> collectMethodInfo(SimpleClassInfo ci, ClassDoc classDoc) {
        if (classDoc == null || classDoc.methods() == null) {
            return new ArrayList<>(0);
        }
        List<SimpleMethodInfo> result = new ArrayList<>(classDoc.methods().length);
        for (MethodDoc methodDoc : classDoc.methods()) {
            SimpleMethodInfo info = new SimpleMethodInfo();
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
    private List<SimpleParamInfo> collectParametersInfo(SimpleClassInfo ci, MethodDoc methodDoc) {
        if (methodDoc == null || methodDoc.parameters() == null) {
            return new ArrayList<>(0);
        }
        List<SimpleParamInfo> result = new ArrayList<>(methodDoc.parameters().length);
        for (Parameter pDoc : methodDoc.parameters()) {
            SimpleParamInfo paramInfo = new SimpleParamInfo();
            paramInfo.setName(pDoc.name());
            paramInfo.setType(pDoc.typeName());
            paramInfo.setActualTypeName(ci.addImport(pDoc.type().qualifiedTypeName()));
            result.add(paramInfo);
        }
        return result;
    }
}
