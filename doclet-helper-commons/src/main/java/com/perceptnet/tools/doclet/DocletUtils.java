package com.perceptnet.tools.doclet;

import com.perceptnet.tools.doclet.data.AnnotationInfo;
import com.sun.javadoc.AnnotationDesc;

import java.util.HashMap;
import java.util.Map;

/**
 * created by vkorovkin on 07.06.2018
 */
public class DocletUtils {
    public static String extractAnnotationValue(Class ac, AnnotationDesc ad) {
        if (ac != null && !ac.getName().equals(ad.annotationType().qualifiedTypeName())) {
            return null;
        }
        for (AnnotationDesc.ElementValuePair evp : ad.elementValues()) {
            String valStr = evp.value().toString();
            if (valStr.startsWith("\"") && valStr.endsWith("\"")) {
                valStr = valStr.substring(1, valStr.length() - 1);
            }
            return valStr;
        }
        return null;
    }

    public static String extractAnnotationValue(Class ac, AnnotationDesc ad, String paramName) {
        if (!ac.getName().equals(ad.annotationType().qualifiedTypeName())) {
            return null;
        }
        for (AnnotationDesc.ElementValuePair evp : ad.elementValues()) {
            if (evp.element().name().equalsIgnoreCase(paramName)) {
                String valStr = evp.value().toString();
                if (valStr.startsWith("\"") && valStr.endsWith("\"")) {
                    valStr = valStr.substring(1, valStr.length() - 1);
                }
                return valStr;
            }
        }
        return "";
    }

    public static boolean matches(Class ac, AnnotationDesc ad) {
        return ac.getName().equals(ad.annotationType().qualifiedTypeName());
    }

    public static Map<String, Object> extractAnnotationParams(AnnotationDesc ad) {
        Map<String, Object> result = new HashMap<>(ad.elementValues().length);
        for (AnnotationDesc.ElementValuePair evp : ad.elementValues()) {
            String elName = evp.element().name();
            String valStr = evp.value().toString();
            if (valStr.startsWith("\"") && valStr.endsWith("\"")) {
                valStr = valStr.substring(1, valStr.length() - 1);
                result.put(elName, valStr);
            } else if (valStr.startsWith("{") && valStr.endsWith("}")) {
                String[] items = valStr.substring(1, valStr.length() - 1).split(",");
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    if (item.startsWith("\"") && item.endsWith("\"")) {
                        item = item.substring(1, item.length() - 1);
                        items[i] = item;
                    }
                }
                result.put(elName, items);
            } else {
                result.put(elName, valStr);
            }
        }
        return result;
    }

}
