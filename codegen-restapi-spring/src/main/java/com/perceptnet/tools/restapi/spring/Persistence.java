package com.perceptnet.tools.restapi.spring;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.perceptnet.commons.json.JsonService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
class Persistence {

    void saveClassInfos(String fileName, Collection<ClassInfo> classesInfo) {
        new JsonService().saveItem(fileName, classesInfo);
    }

    Collection<ClassInfo> loadClassInfos(String fileName) {
        return (Collection<ClassInfo>) (new JsonService().loadItem(fileName));
    }
}
