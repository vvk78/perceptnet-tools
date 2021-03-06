package com.perceptnet.tools.restapi.spring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 06.12.2017
 */
class GenerationUtils {
    static String simpleName(String qualifiedName) {
        if (qualifiedName == null) {
            return null;
        }
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1, qualifiedName.length());
    }

    static String indent(String str, String indentation) {
        if (indentation == null || indentation.isEmpty() || str == null || str.isEmpty()) {
            return str;
        }

        if (str.endsWith("\r\n")) {
            return str.substring(0, str.length() - 2).replace("\n", "\n" + indentation) + "\r\n";
        } else if (str.endsWith("\n")) {
            return str.substring(0, str.length() - 1).replace("\n", "\n" + indentation) + "\n";
        } else {
            return str.replace("\n", "\n" + indentation);
        }
    }

}
