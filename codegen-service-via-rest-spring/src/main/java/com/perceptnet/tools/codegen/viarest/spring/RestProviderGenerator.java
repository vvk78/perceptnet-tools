package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.restclient.BaseRestServiceProvider;
import com.perceptnet.restclient.MessageConverter;
import com.perceptnet.tools.codegen.BaseGenerator;

import java.util.Collection;
import java.util.Iterator;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public class RestProviderGenerator extends BaseGenerator<Object> {

    private GenerationHelper helper = GenerationHelper.I;
    private GenerationContext ctx;

    public RestProviderGenerator(GenerationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void generate(Object params) {
        generate();
    }

    public void generate() {
        print("package ");
        print(ctx.getServiceProviderPackage());
        println(";");

        println();

        generateImports(BaseRestServiceProvider.class, MessageConverter.class);

        Collection<OldRestServiceClientInfo> restServices = ctx.getRestServices().values();
        for (OldRestServiceClientInfo restServiceClientInfo : restServices) {
            print("import ");
            print(restServiceClientInfo.getServiceQualifiedName());
            println(";");
        }

        println();
        print("public class ");
        print(ctx.getServiceProviderSimpleName());
        print(" extends ");
        print(BaseRestServiceProvider.class.getName());
        println(" {");
        pushIndentation("    ");
        println();

        //
        println("//not declared volatile as the variable is set once at startup and thus the chance of collision is minimal");
        print("public static ");
        print(ctx.getServiceProviderSimpleName());
        print(" ");
        println("INSTANCE;");

        //Simple constructor
        print("public ");
        print(ctx.getServiceProviderSimpleName());
        println("(String baseUrl) {");
        pushIndentation("    ");
        println("this(baseUrl, null);");
        popIndentation();
        println("}");
        println();

        //Constructor with message converter
        print("public ");
        print(ctx.getServiceProviderSimpleName());
        println("(String baseUrl, MessageConverter messageConverter) {");
        pushIndentation("    ");
        print("super(baseUrl, new ");
        print(ctx.getRegistrySimpleName());
        print("(), ");
        print("messageConverter, ");
        println("new Class[] {");
        pushIndentation("    ");

        for (Iterator<OldRestServiceClientInfo> iter = restServices.iterator(); iter.hasNext();) {
            OldRestServiceClientInfo s = iter.next();
            print(s.getServiceSimpleName());
            if (iter.hasNext()) {
                println(".class,");
            } else {
                println(".class");
            }
        }
        popIndentation();
        println("});");
        popIndentation();
        println("}");

        for (OldRestServiceClientInfo restService : restServices) {
            println();
            print("public static ");
            print(restService.getServiceSimpleName());
            print(" get");
            print(restService.getServiceSimpleName());
            println("() {");
            pushIndentation("    ");
            print("return INSTANCE.getRestService(");
            print(restService.getServiceSimpleName());
            println(".class);");
            popIndentation();
            println("}");
        }

        println();

        popIndentation();
        println("}");
    }

    private void generateImports(Class ... classes) {
        for (Class aClass : classes) {
            print("import ");
            print(aClass.getName());
            println(";");
        }
    }



}
