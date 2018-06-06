package com.perceptnet.tools.doclet.data;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.perceptnet.commons.utils.FileUtils;

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
public class PersistenceService {

    private OutputStream os;
    private JsonWriter jsonWriter;

    public void saveClassInfos(String fileName, Collection<ClassDocInfo> classesInfo) {
        prepareFos(fileName);
        try {
            jsonWriter.write(classesInfo);
            this.os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (this.os != null) {
                try {
                    this.os.close();
                    this.os = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            jsonWriter = null;
        }
    }

    public Collection<ClassDocInfo> loadClassInfos(String fileName) {
        try (InputStream is = getInput(fileName)) {
            if (is instanceof ZipInputStream) {
                ZipEntry entry = ((ZipInputStream)is).getNextEntry();
            }
            Collection<ClassDocInfo> result = (Collection<ClassDocInfo>) JsonReader.jsonToJava(is, null);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Cannot load class information from " + fileName + " due to " + e, e);
        }
    }

    private InputStream getInput(String fileName) throws FileNotFoundException {
        InputStream result;
        if (fileName.startsWith("classpath:")) {
            String resName = fileName.substring("classpath:".length());
            result = getClass().getClassLoader().getResourceAsStream(resName);
            if (result == null) {
                throw new FileNotFoundException("Resource " + resName + " is not found on classpath");
            }
        } else {
            result = new FileInputStream(fileName);
        }
        if (fileName.endsWith(".zip")) {
            result = new ZipInputStream(result);
        }
        return result;
    }

    private void prepareFos(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "RestControllers.doc.json"; // + ".zip";
        }
        FileUtils.prepareFileForReCreation(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            if (fileName.endsWith(".zip")) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                zos.setLevel(9);
                int zipIndex = fileName.lastIndexOf(".zip");
                int simpleNameIndex = fileName.lastIndexOf(".", Math.max(0, zipIndex - 1));
                if (simpleNameIndex == -1) {
                    simpleNameIndex = 0;
                }
                zos.putNextEntry(new ZipEntry(fileName.substring(simpleNameIndex, zipIndex)));
                this.os = zos;
            } else {
                this.os = fos;
            }

            this.jsonWriter = new JsonWriter(this.os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
