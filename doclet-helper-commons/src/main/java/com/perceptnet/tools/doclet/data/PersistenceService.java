package com.perceptnet.tools.doclet.data;


import com.perceptnet.commons.json.JsonService;

import java.util.Collection;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
public class PersistenceService {

    public void saveClassInfos(String fileName, Collection<ClassDocInfo> classesInfo) {
        new JsonService().saveItem(fileName, classesInfo);
    }

    public Collection<ClassDocInfo> loadClassInfos(String fileName) {
        return (Collection<ClassDocInfo>) (new JsonService().loadItem(fileName));
    }

}
