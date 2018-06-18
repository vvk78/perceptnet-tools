package com.perceptnet.tools.doclet;

import com.perceptnet.commons.utils.EnumUtils;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.tools.doclet.data.PersistenceService;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.sun.javadoc.RootDoc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public static boolean start(RootDoc rootDoc) {
        CollectorOptions options = readOptions(rootDoc.options());
        String fileName = options.getOutputFileName();
        if (fileName != null) {
            ServiceInfoCollector serviceInfoCollector = new ServiceInfoCollector(options);
            Map<String, ClassDocInfo> services = serviceInfoCollector.collectServicesInfo(rootDoc);
            System.out.println("Number of services collected: " + services.size());
            PersistenceService p = new PersistenceService();
            p.saveClassInfos(fileName, services.values());
            return true;
        }
        return false;
    }

    public static int optionLength(String option) {
        if (option.equals("-saveInfoAs")) {
            return 2;
        } else if (option.equals("-inc")) {
            return 2;
        } else if (option.equals("-exl")) {
            return 2;
        } else if (option.equals("-collectedItemTypes")) {
            return 2;
        }
        return 0;
    }

    private static CollectorOptions readOptions(String[][] options) {
//        System.out.println("--------Options: " + options.length);
//        System.out.println(options);
//        System.out.println("--------");
        CollectorOptions result = new CollectorOptions();
        Set<ItemType> collectedItemTypes = null;
        IncExlRegexFilter itemNamesFilter = null;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals("-saveInfoAs")) {
                result.setOutputFileName(opt[1]);
            } else if (opt[0].equals("-inc")) {
                if (itemNamesFilter == null) {
                    itemNamesFilter = new IncExlRegexFilter();
                }
                itemNamesFilter.setIncFiltersSimpleWildcards(Collections.singletonList(opt[1]));
            } else if (opt[0].equals("-exl")) {
                if (itemNamesFilter == null) {
                    itemNamesFilter = new IncExlRegexFilter();
                }
                itemNamesFilter.setExlFiltersSimpleWildcards(Collections.singletonList(opt[1]));
            } else if (opt[0].equals("-collectedItemTypes")) {
                String[] itemTypesStr = opt[1].split(",");
                collectedItemTypes = new HashSet<>();
                for (String s : itemTypesStr) {
                    ItemType t = EnumUtils.parseSafely(ItemType.class, s.toUpperCase());
                    if (t != null) {
                        collectedItemTypes.add(t);
                    }
                }
            }
        }
        if (itemNamesFilter != null) {
            result.setItemNamesFilter(itemNamesFilter);
        }
        result.setCollectedItemTypes(collectedItemTypes);
        return result;
    }

}
