package com.perceptnet.tools.codegen.viarest.spring;

import com.google.common.base.Joiner;
import com.perceptnet.restclient.HttpMethod;
import com.perceptnet.restclient.RestMethodDescription;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;
import com.perceptnet.tools.doclet.data.ParamDocInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 29.11.2017
 */
public class GenerationHelper {
    private final Logger log = LoggerFactory.getLogger(GenerationHelper.class);

    public static final GenerationHelper I = new GenerationHelper(); private GenerationHelper() {};

    public HttpMethod parseHttpMethodSafely(String requestMethod) {
        if (requestMethod == null) {
            return null;
        }
        requestMethod = requestMethod.substring(requestMethod.lastIndexOf(".") + 1, requestMethod.length());
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(requestMethod)) {
                return httpMethod;
            }
        }
        return null;
    }

    /**
     * Parses request mapping and returns list of either String, or String[1] as items. String[1] indicates path variable.
     */
    public List parseRequestMapping(String requestMappingPath) {
        List result = new ArrayList();
        int curIdx = 0;
        while (curIdx < requestMappingPath.length()) {
            int openBraceIdx = requestMappingPath.indexOf("{", curIdx);
            int closeBraceIdx;
            if (openBraceIdx >= 0) {
                if (curIdx < openBraceIdx) {
                    result.add(requestMappingPath.substring(curIdx, openBraceIdx));
                }
                closeBraceIdx = requestMappingPath.indexOf("}", openBraceIdx);
                if (closeBraceIdx == -1) {
                    log.error("Request mapping {} seems to be invalid, method ignored", requestMappingPath);
                    return null;
                }
                result.add(new String[]{(requestMappingPath.substring(openBraceIdx + 1, closeBraceIdx))});
                curIdx = closeBraceIdx + 1;
            } else {
                result.add(requestMappingPath.substring(curIdx));
                break;
            }
        }
        return result;
    }

    public RestMethodDescription createRestDescription(ClassDocInfo c, MethodDocInfo m) {
        String basePath = c.getBaseRestMapping();
        int requestBodyIndex = -1;
        List<String> pathItems = new ArrayList<>();
        Map<String, Integer> pathArgumentIndexes = new LinkedHashMap<>();
        Object item = null;
        for (int i = 0; i < m.getBaseRestMappingItems().size(); i++) {
            item = m.getBaseRestMappingItems().get(i);
            if (item instanceof String) {
                String s = (String) item;
                if (i == 0) {
                    if (basePath.endsWith("/") && s.startsWith("/")) {
                        s = basePath + s.substring(1);
                    } else if (!s.startsWith("/") && !basePath.endsWith("/")) {
                        s = basePath + "/" + s;
                    } else {
                        s = basePath + s;
                    }
                }
                pathItems.add(s);
            } else {
                if (i == 0) {
                    pathItems.add(basePath);
                }
                pathArgumentIndexes.put(((String[]) item)[0], -1);
                pathItems.add(null);
            }
        }
        boolean firstParam = true;
        for (int i = 0; i < m.getParams().size(); i++) {
            ParamDocInfo paramDocInfo = m.getParams().get(i);
            if (paramDocInfo.getUrlPathVariable() != null) {
                if (!pathArgumentIndexes.containsKey(ParamDocInfo.getUrlPathVariable())) {
                    throw new IllegalStateException("No path variable '" + ParamDocInfo.getUrlPathVariable() + "' in request mapping of " +
                            c.getQualifiedName() + "." + m.getName() + "(" + m.getFlatSignature() + ")");
                }
                pathArgumentIndexes.put(ParamDocInfo.getUrlPathVariable(), i);
            } else if (ParamDocInfo.getUrlParamName() != null) {
                String s = ParamDocInfo.getUrlParamName();
                pathArgumentIndexes.put(s, i);
                if (firstParam) {
                    s = "?" + s + "=";
                    firstParam = false;
                } else {
                    s = "&" + s + "=";
                }
                pathItems.add(s);
                pathItems.add(null);
            } else if (ParamDocInfo.isRequestBody()
                    || (i == m.getParams().size() - 1
                    && (m.getHttpMethod() == HttpMethod.post || m.getHttpMethod() == HttpMethod.put || m.getHttpMethod() == HttpMethod.patch))) {
                requestBodyIndex = i;
            }
        }
        int[] pathArgumentIndices = null;
        if (!pathArgumentIndexes.isEmpty()) {
            pathArgumentIndices = new int[pathArgumentIndexes.size()];
            int idx = 0;
            for (Integer pathArgIndex : pathArgumentIndexes.values()) {
                pathArgumentIndices[idx++] = pathArgIndex;
            }
        }

        return new RestMethodDescription(m.getHttpMethod(), pathItems.toArray(new String[pathItems.size()]), pathArgumentIndices, requestBodyIndex);
    }

    public String getMostGeneralPackage(Collection<OldRestServiceClientInfo> restServices) {
        List<String> items = null;
        for (OldRestServiceClientInfo restService : restServices) {
            String sqn = restService.getServiceQualifiedName();
            String[] serviceItems = sqn.substring(0, Math.max(0, sqn.lastIndexOf("."))).split("\\.");
            if (items == null) {
                items = Arrays.asList(serviceItems);
            } else {
                int j = 0;
                for (int i = 0; i < Math.min(items.size(), serviceItems.length); i++) {
                    if (items.get(i).equals(serviceItems[i])) {
                        j = i;
                    } else {
                        break;
                    }
                }
                if (j == 0) {
                    return "";
                }
                items = items.subList(0, j + 1);
            }
        }
        if (items == null) {
            return "";
        }
        return Joiner.on(".").join(items);
    }

    public String packageToDir(String pkg) {
        return pkg.replace("\\.", "\\");
    }

}
