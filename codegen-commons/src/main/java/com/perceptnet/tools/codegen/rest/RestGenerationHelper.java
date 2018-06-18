package com.perceptnet.tools.codegen.rest;

import com.perceptnet.restclient.dto.HttpMethod;
import com.perceptnet.restclient.dto.RestMethodDescription;
import com.perceptnet.tools.doclet.data.AnnotationInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.MethodDocInfo;
import com.perceptnet.tools.doclet.data.ParamDocInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 29.11.2017
 */
public class RestGenerationHelper {
    private final Logger log = LoggerFactory.getLogger(RestGenerationHelper.class);

    public RestServiceInfo extractRestServiceInfoFromController(ClassDocInfo<?> c) {
        RestServiceInfo rsi = new RestServiceInfo(c);
        for (AnnotationInfo<?> a : c.getAnnotations()) {
            if (a.getType().isClass(RequestMapping.class) && a.getValue() != null) {
                rsi.setControllerBaseRestMapping(a.getValue());
                break;
            }
        }

        for (MethodDocInfo<?> m : c.getMethods()) {
            RestMethodInfo rmi = extractRestMethodInfo(m);
            if (rmi != null) {
                rsi.getRestMethods().add(rmi);
            }
        }

        System.out.println(" " + rsi.getRestMethods().size() + " rest methods in " + c.getQualifiedName());
        return rsi;
    }

    private RestMethodInfo extractRestMethodInfo(MethodDocInfo<?> m) {
        RestMethodAndMapping rMM = null;
        for (AnnotationInfo<?> a : m.getAnnotations()) {
            rMM = extractRestMethodMappingFromShortcuts(a);
            if (rMM == null) {
                rMM = extractRestMethodMappingBasic(a);
            }

            if (rMM != null) {
                break;
            }
        }

        if (rMM == null) {
            return null;
        }

        RestMethodInfo rmi = new RestMethodInfo(m, rMM.getHttpMethod(), parseRequestMappingStr(rMM.getRequestMapping()));

        //collect rest parameter descriptors:
        for (ParamDocInfo<?> p : m.getParams()) {
            RestMethodParamInfo rmp = extractRestMethodParamInfo(p);
            if (rmp != null) {
                rmi.getParams().add(rmp);
            }
        }
        return rmi;
    }

    private RestMethodParamInfo extractRestMethodParamInfo(ParamDocInfo<?> p) {
        for (AnnotationInfo<?> a : p.getAnnotations()) {
            if (a.getType().isClass(RequestParam.class)) {
                String value = a.getValue();
                if (value != null) {
                    return RestMethodParamInfo.asRequestParam(p, value);
                }
            } else if (a.getType().isClass(PathVariable.class)) {
                String value = a.getValue();
                if (value != null) {
                    return RestMethodParamInfo.asPathVariable(p, value);
                }
            } else if (a.getType().isClass(RequestBody.class)) {
                return RestMethodParamInfo.asRequestBody(p);
            }
        }
        return null;
    }

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
    public List parseRequestMappingStr(String requestMappingPath) {
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

    /**
     * Spring version >5 brings new annotations to map request methods shortly like GetMapping, PostMapping etc., this
     * method extracts rest mapping info from these shortcuts if possible, or returns null otherwise
     */
    private RestMethodAndMapping extractRestMethodMappingFromShortcuts(AnnotationInfo<?> a) {
        if (a.getType().getQualifiedName().equals(GetMapping.class.getName())) {
            return new RestMethodAndMapping(HttpMethod.get, a.getValue());
        } else if (a.getType().getQualifiedName().equals(PostMapping.class.getName())) {
            return new RestMethodAndMapping(HttpMethod.post, a.getValue());
        } else if (a.getType().getQualifiedName().equals(PatchMapping.class.getName())) {
            return new RestMethodAndMapping(HttpMethod.patch, a.getValue());
        } else if (a.getType().getQualifiedName().equals(DeleteMapping.class.getName())) {
            return new RestMethodAndMapping(HttpMethod.delete, a.getValue());
        }

        return null;
    }

    private RestMethodAndMapping extractRestMethodMappingBasic(AnnotationInfo<?> a) {
        if (!a.getType().getQualifiedName().equals(RequestMapping.class.getName())) {
            return null;
        }
        HttpMethod requestMethod;
        String httpMethodStr = a.getParamAsStr("method");
        if (httpMethodStr != null) {
            requestMethod = parseHttpMethodSafely(httpMethodStr);
            if (requestMethod == null) {
                System.err.println("Not parsable request method: " + httpMethodStr);
                return null;
            }
        } else {
            return null;
        }

        String requestMappingStr = a.getValue();
        return new RestMethodAndMapping(requestMethod, requestMappingStr);
    }

    public RestMethodDescription createRestDescription(RestServiceInfo rsi, RestMethodInfo m) {
        String basePath = rsi.getControllerBaseRestMapping();
        if (basePath == null) {
            basePath = "";
        }
        int requestBodyIndex = -1;
        List<String> pathItems = new ArrayList<>();
        Map<String, Integer> pathArgumentIndexes = new LinkedHashMap<>();
        Object item = null;
        for (int i = 0; i < m.getRawRestMappingItems().size(); i++) {
            item = m.getRawRestMappingItems().get(i);
            if (item instanceof String) {
                String s = (String) item;
                if (i == 0) {
                    if (basePath.isEmpty() || basePath.endsWith("/") && s.startsWith("/")) {
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
            RestMethodParamInfo rmp = m.getParams().get(i);
            ParamDocInfo pd = rmp.getControllerParamDoc();
            if (rmp.getUrlPathVariable() != null) {
                if (!pathArgumentIndexes.containsKey(rmp.getUrlPathVariable())) {
                    throw new IllegalStateException("No path variable '" + rmp.getUrlPathVariable() + "' in request mapping of " +
                            rsi.getControllerDoc().getQualifiedName() + "." +
                            m.getControllerMethodDoc().getName() + "(" +  m.getControllerMethodDoc().getFlatSignature() + ")");
                }
                pathArgumentIndexes.put(rmp.getUrlPathVariable(), i);
            } else if (rmp.getUrlParamName() != null) {
                String s = rmp.getUrlParamName();
                pathArgumentIndexes.put(s, i);
                if (firstParam) {
                    s = "?" + s + "=";
                    firstParam = false;
                } else {
                    s = "&" + s + "=";
                }
                pathItems.add(s);
                pathItems.add(null);
            } else if (rmp.isRequestBody()
                    || (i == m.getParams().size() - 1
                    && (m.getHttpMethod() == HttpMethod.post || m.getHttpMethod() == HttpMethod.put
                                                                || m.getHttpMethod() == HttpMethod.patch))) {
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

    public String buildServiceMethodQualifiedSignature(MethodDocInfo<?> serviceMethodDoc) {
        StringBuilder b = new StringBuilder();
        b.append(serviceMethodDoc.getName());
        b.append("(");

        boolean first = true;
        for (ParamDocInfo p : serviceMethodDoc.getParams()) {
            if (!first) {
                b.append(",");
            }
            first = false;
            b.append(p.getType().getQualifiedName());
        }
        b.append(")");
        return b.toString();
    }


    public String packageToDir(String pkg) {
        return pkg.replace("\\.", "\\");
    }

}
