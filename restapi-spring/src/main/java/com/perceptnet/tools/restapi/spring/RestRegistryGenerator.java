package com.perceptnet.tools.restapi.spring;

import com.perceptnet.restclient.BaseRestMethodRegistry;
import com.perceptnet.restclient.HttpMethod;
import com.perceptnet.restclient.MessageConverter;
import com.perceptnet.restclient.RestMethodDescription;
import com.perceptnet.restclient.ServiceMethodsRegistry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 06.12.2017
 */
public class RestRegistryGenerator extends BaseGenerator<Collection<ClassInfo>> {
    private GenerationHelper helper = GenerationHelper.I;
    private GenerationContext ctx;
    private String basePath;
    private RestServiceInfo s;

    public RestRegistryGenerator(GenerationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void generate(Collection<ClassInfo> controllers) {
        String registryPackage = ctx.getServiceProviderPackage();
        if (registryPackage == null) {
            throw new IllegalStateException("Rest registry package is not set and not defined by default");
        }
        print("package ");
        print(registryPackage);
        println(";");

        println();
        generateImports(BaseRestMethodRegistry.class, ServiceMethodsRegistry.class, RestMethodDescription.class, ConcurrentHashMap.class, HttpMethod.class);
        println();

        String registryClassName = ctx.getRegistrySimpleName();
        print("class " + registryClassName + " extends ");
        print(BaseRestMethodRegistry.class.getName());
        println(" {");
        pushIndentation("    ");
        println();
        //print("public ");
        print(registryClassName);
        println("() {");
        print("    super(");
        print(ctx.getTotalMethodsCount());
        print(", ");
        println("buildRegistryMap());");
        println("}");
        println();
        generateRestRegistryMap(controllers);
    }

    private void generateImports(Class ... classes) {
        for (Class aClass : classes) {
            print("import ");
            print(aClass.getName());
            println(";");
        }
    }

    private void generateRestRegistryMap(Collection<ClassInfo> controllers) {
        ClassInfo tmp = new ClassInfo();

        println("private static ConcurrentHashMap<String, ServiceMethodsRegistry> buildRegistryMap() {");
        pushIndentation("    ");
        print("ConcurrentHashMap<String, ServiceMethodsRegistry> result = new ConcurrentHashMap<>(");
        print(controllers.size());
        println(");");
        //map for overloaded methods
        println("ConcurrentHashMap<String, RestMethodDescription> om;");
        //map for not overloaded methods
        println("ConcurrentHashMap<String, RestMethodDescription> m;");

        for (ClassInfo c : controllers) {
            basePath = c.getBaseRestMapping();
            s = ctx.getRestServices().get(c);
            println();
            print("// ");
            println(s.getServiceQualifiedName());
            if (s.getSingleMethods() > 0) {
                print("m = new ConcurrentHashMap<>(");
                print(s.getSingleMethods());
                println(");");
            } else {
                println("m = null;");
            }
            if (s.getOverloadedMethods() > 0) {
                print("om = new ConcurrentHashMap<>(");
                print(s.getOverloadedMethods());
                println(");");
            } else {
                println("om = null;");
            }

            print("result.put(\"");
            print(s.getServiceQualifiedName());
            println("\", new ServiceMethodsRegistry(om, m));");

            for (List<MethodInfo> methods : s.getMethodsOnName().values()) {
                for (MethodInfo method : methods) {
                    RestMethodDescription d = helper.createRestDescription(c, method);
                    if (methods.size() == 1) {
                        if (s.getSingleMethods() <= 0) {
                            throw new IllegalStateException("Encountered single method " + method.getName() + "; but single methods count is 0");
                        }
                        print("m.put(\"");
                        print(method.getName());
                        println("\", ");
                    } else {
                        if (s.getOverloadedMethods() <= 0) {
                            throw new IllegalStateException("Encountered overloaded method " + method.getName() + "; but single methods count is 0");
                        }
                        print("om.put(\"");
                        print(method.getName());
                        print("(");
                        print(method.getFlatSignature());
                        println(")\", ");
                    }
                    pushIndentation("   ");
                    print("new RestMethodDescription(");
                    pushIndentation("   ");
                    print(HttpMethod.class.getSimpleName());
                    print(".");
                    print(d.getHttpMethod());
                    println(",");
                    print("new String[]{");
                    print(joinStrings(d.getPathPieces()));
                    println("},");
                    if (d.getPathArgumentIndices() == null) {
                        print("null, ");
                    } else {
                        print("new int[]{");
                        print(join(d.getPathArgumentIndices()));
                        print("}, ");
                    }
                    println(d.getBodyArgumentIndex());
                    popIndentation();
                    println("));");
                    popIndentation();
                }
            }
        }

        println("return result;");
        popIndentation();
        println("}");
        println();
        popIndentation();
        println("}");

    }

    private String joinStrings(String[] pieces) {
        StringBuilder buff = new StringBuilder(pieces.length * 15);
        for (String piece : pieces) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            if (piece == null) {
                buff.append("null");
            } else {
                buff.append('"');
                buff.append(piece);
                buff.append('"');
            }
        }
        return buff.toString();
    }

    private String join(int[] ints) {
        StringBuilder buff = new StringBuilder(ints.length * 3);
        for (int i : ints) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            buff.append(i);
        }
        return buff.toString();
    }





}
