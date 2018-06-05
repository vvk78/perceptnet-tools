package com.perceptnet.tools.restapi.spring;

import com.perceptnet.restclient.HttpMethod;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 23.11.2017
 */
public class RestServiceGenDoclet {
    private GenerationHelper helper = GenerationHelper.I;

    public static boolean start(RootDoc root) {
        RestServiceGenDoclet instance = new RestServiceGenDoclet();
        GenerationOptions options = instance.readOptions(root.options());
        Collection<ClassInfo> controllersInfo = instance.collectControllersInfo(root);
        if (options.getSaveControllersInfoAs() != null) {
            Persistence p = new Persistence();
            p.saveClassInfos(options.getSaveControllersInfoAs(), controllersInfo);
        }
        if (options.isGenerate() && !controllersInfo.isEmpty()) {
            GenerationManager gm = new GenerationManager(options.getAdaptor());
            gm.generate(controllersInfo);
        }
        return true;
    }

    public static int optionLength(String option) {
        if (option.equals("-saveInfoAs")) {
            return 2;
        } else if (option.equals("-generate")) {
            return 1;
        } else if (option.equals("-adapter")) {
            return 2;
        }
        return 0;
    }

    private GenerationOptions readOptions(String[][] options) {
        GenerationOptions result = new GenerationOptions();
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals("-saveInfoAs")) {
                result.setSaveControllersInfoAs(opt[1]);
            } else if (opt[0].equals("-generate")) {
                result.setGenerate(true);
            } else if (opt[0].equals("-adapter")) {
                result.installAdaptor(opt[1]);
            }
        }
        return result;
    }


    public Collection<ClassInfo> collectControllersInfo(RootDoc root) {
        Collection<ClassInfo> result = new ArrayList<>(30);
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassInfo controllerInfo = buildControllerInfo(classes[i]);
            if (controllerInfo != null) {
                result.add(controllerInfo);
            }
        }
        return result;
    }

    public void generate(Collection<ClassInfo> controllers) {
        //todo plug custom generation adapter here
        GenerationContext ctx = new GenerationContext(new DefaultGenerationAdaptor());
        RestServiceGenerator serviceGen = new RestServiceGenerator(ctx);


        RestRegistryGenerator registryGen = new RestRegistryGenerator(ctx);
        RestProviderGenerator providerGen = new RestProviderGenerator(ctx);
    }

    private ClassInfo buildControllerInfo(ClassDoc c) {
        //TODO move to generation adapter
        if (!c.typeName().endsWith("Controller")) {
            return null;
        }

        ClassInfo ci;
    cl:
        {
            for (AnnotationDesc ad : c.annotations()) {
                String requestMapping = extractAnnotationValue(RequestMapping.class, ad);
                if (requestMapping != null) {
                    System.out.println("   Request mapping: " + requestMapping);
                    ci = new ClassInfo(c.name(), c.qualifiedName(), requestMapping, c.getRawCommentText());
                    break cl;
                }
            }

            return null;
        }



    methods:
        for (MethodDoc m : c.methods()) {
            RestMethodInfo rmi = null;
            for (AnnotationDesc ad : m.annotations()) {
                rmi = extractRestMethodMappingFromShortcuts(ad);
                if (rmi == null) {
                    rmi = extractRestMethodMappingBasic(ad);
                }

                if (rmi != null) {
                    break;
                }
            }

            if (rmi == null) {
                continue methods;
            }

            MethodInfo mi = new MethodInfo(m.name(), m.flatSignature(), rmi.httpMethod,
                    helper.parseRequestMapping(rmi.requestMapping), m.getRawCommentText(), ci.addImport(m.returnType().qualifiedTypeName()));

            ci.getMethods().add(mi);

        params:
            for (Parameter p : m.parameters()) {
                ParamInfo pi = new ParamInfo(p.name(), p.typeName(), ci.addImport(p.type().qualifiedTypeName()));
                mi.getParams().add(pi);
                for (AnnotationDesc ad : p.annotations()) {
                    String requestParam = extractAnnotationValue(RequestParam.class, ad);
                    if (requestParam != null) {
                        pi.setUrlParamName(requestParam);
                        continue params;
                    }
                    String pathVariable = extractAnnotationValue(PathVariable.class, ad);
                    if (pathVariable != null) {
                        pi.setUrlPathVariable(pathVariable);
                        continue params;
                    }
                    String requestBody = extractAnnotationValue(RequestBody.class, ad);
                    pi.setRequestBody(requestBody != null);
                }
            }
        }

        System.out.println(" Imports: " + ci.getImports().size());
        System.out.println(" Methods: " + ci.getMethods().size());
        return ci;
    }

    /**
     * Spring version >5 brings new annotations to map request methods shortly like GetMapping, PostMapping etc., this
     * method extracts rest mapping info from these shortcuts if possible, or returns null otherwise
     */
    private RestMethodInfo extractRestMethodMappingFromShortcuts(AnnotationDesc ad) {
        String requestMapping = extractAnnotationValue(GetMapping.class, ad, "value");
        if ((requestMapping = extractAnnotationValue(GetMapping.class, ad, "value")) != null) {
            return new RestMethodInfo(HttpMethod.get, requestMapping);
        } else if ((requestMapping = extractAnnotationValue(PostMapping.class, ad, "value")) != null) {
            return new RestMethodInfo(HttpMethod.post, requestMapping);
        } else if ((requestMapping = extractAnnotationValue(PatchMapping.class, ad, "value")) != null) {
            return new RestMethodInfo(HttpMethod.patch, requestMapping);
        } else if ((requestMapping = extractAnnotationValue(DeleteMapping.class, ad, "value")) != null) {
            return new RestMethodInfo(HttpMethod.delete, requestMapping);
        }
        return null;
    }

    private RestMethodInfo extractRestMethodMappingBasic(AnnotationDesc ad) {
        HttpMethod requestMethod;
        String httpMethodStr = extractAnnotationValue(RequestMapping.class, ad, "method");
        if (httpMethodStr != null) {
            requestMethod = helper.parseHttpMethodSafely(httpMethodStr);
            if (requestMethod == null) {
                System.err.println("Not parsable request method: " + httpMethodStr);
                return null;
            }
        } else {
            return null;
        }

        String requestMappingStr = extractAnnotationValue(RequestMapping.class, ad, "value");
        return new RestMethodInfo(requestMethod, requestMappingStr);
    }

    private String extractAnnotationValue(Class ac, AnnotationDesc ad) {
        if (!ac.getName().equals(ad.annotationType().qualifiedTypeName())) {
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

    private String extractAnnotationValue(Class ac, AnnotationDesc ad, String paramName) {
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                                               I N N E R    C L A S S E S
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static final class RestMethodInfo {
        private HttpMethod httpMethod;
        private String requestMapping;

        public RestMethodInfo(HttpMethod httpMethod, String requestMapping) {
            this.httpMethod = httpMethod;
            this.requestMapping = requestMapping;
        }
    }


}
