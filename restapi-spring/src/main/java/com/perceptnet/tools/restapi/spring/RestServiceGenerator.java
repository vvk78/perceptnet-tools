package com.perceptnet.tools.restapi.spring;

import com.google.common.base.Joiner;

import static com.perceptnet.tools.restapi.spring.GenerationUtils.*;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 05.12.2017
 */
public class RestServiceGenerator extends BaseGenerator<ClassInfo> {
    private GenerationContext ctx;

    private RestServiceInfo serviceInfo;

    public RestServiceGenerator(GenerationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void generate(ClassInfo c) {
        serviceInfo = ctx.getRestServices().get(c);
        if (serviceInfo == null) {
            String serviceQualifiedName = ctx.getGenerationAdaptor().getServiceQualifiedNameFromController(c.getQualifiedName());
            serviceInfo = new RestServiceInfo(serviceQualifiedName);
            ctx.getRestServices().put(c, serviceInfo);
        }

        if (!serviceInfo.getServicePackage().isEmpty()) {
            print("package ");
            print(serviceInfo.getServicePackage());
            println(";\n");
        }

        if (!c.getImports().isEmpty()) {
            print("import ");
            print(Joiner.on(";\nimport ").join(c.getImports().values()));
            println(";\n");
        }

        if (c.getRawComment() != null) {
            println("/** ");
            pushIndentation(" *");
            print(c.getRawComment());
            clearIndentation();
            println(" */");
        }
        print("public interface ");
        print(serviceInfo.getServiceSimpleName());
        println(" {");
        pushIndentation("    ");
        generateMethods(c);
        clearIndentation();
        println("}");
    }

    private void generateMethods(ClassInfo c) {
        for (MethodInfo m : c.getMethods()) {
            ctx.incMethodsCount();
            serviceInfo.registerMethod(m);
            println();
            if (m.getRawComment() != null && !m.getRawComment().trim().isEmpty()) {
                println("/**");
                pushIndentation(" *");
                print(m.getRawComment());
                popIndentation();
                println(" */");
            }
            print(m.getActualReturnType());
            print(" ");
            print(m.getName());
            print("(");
            boolean first = true;
            for (ParamInfo p : m.getParams()) {
                if (!first) {
                    print(", ");
                }
                print(p.getActualTypeName());
                print(" ");
                print(p.getName());
                first = false;
            }
            println(");");
        }
        println();
    }

}
