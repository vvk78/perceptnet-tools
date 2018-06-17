package com.perceptnet.tools.doclet.data;


import com.perceptnet.commons.json.JsonPersistenceService;

import java.util.Collection;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
public class PersistenceService {

    public void saveClassInfos(String fileName, Collection<ClassDocInfo> classesInfo) {
        new JsonPersistenceService().saveItems(fileName, classesInfo);
    }

    public Collection<ClassDocInfo> loadClassInfos(String fileName) {
        return (Collection<ClassDocInfo>) (new JsonPersistenceService().loadItems(fileName));
    }

}
