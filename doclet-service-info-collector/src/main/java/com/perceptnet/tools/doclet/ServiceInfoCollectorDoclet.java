package com.perceptnet.tools.doclet;

import com.perceptnet.tools.doclet.data.Persistence;
import com.perceptnet.tools.doclet.data.SimpleClassInfo;
import com.sun.javadoc.RootDoc;

import java.util.HashMap;
import java.util.Map;

/**
 * created by vkorovkin on 05.06.2018
 */
public class ServiceInfoCollectorDoclet {

    /**
     * Doclet start method
     *
     * @param rootDoc root of java docs representation
     * @return true if com.paragonsoftware.infocollector.doclet finished succsessful, false if  finished unsuccsessful
     */
    public static final boolean start(RootDoc rootDoc) {
        String fileName = getFileName(rootDoc);
        if (fileName != null) {
            ServiceInfoCollector serviceInfoCollector = new ServiceInfoCollector();
            Map<String, SimpleClassInfo> services = serviceInfoCollector.collectServicesInfo(rootDoc);
            System.out.println("Number of services collected: " + services.size());
            Persistence p = new Persistence();
            p.saveClassInfos(fileName, services.values());
            return true;
        }
        return false;
    }

    /**
     * Getting file name string to results output
     *
     * @param rootDoc root of java docs representation
     * @return file name string
     */
    private static String getFileName(final RootDoc rootDoc) {
        String[][] options = rootDoc.options();
        for (String[] ops : options) {
            if (ops != null && ops.length > 1) {
                if (ops[0].equals("-saveInfoAs")) {
                    return ops[1];
                }
            }
        }
        return null;
    }

    /**
     * Cheking options length
     *
     * @param option option name
     * @return number of options
     */
    public static int optionLength(String option) {
        if (option.equals("-saveInfoAs")) {
            return 2;
        }
        return 0;
    }

}
