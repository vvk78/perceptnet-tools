package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.api.ItemsLoadService;
import com.perceptnet.api.Services;
import com.perceptnet.commons.utils.ClassUtils;
import com.perceptnet.restclient.BaseRestMethodRegistry;
import com.perceptnet.restclient.BaseRestServiceProvider;
import com.perceptnet.restclient.MessageConverter;
import com.perceptnet.restclient.dto.ModuleRestRegistryDto;
import com.perceptnet.tools.ImportsHelper;
import com.perceptnet.tools.codegen.BaseGenerator;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public class RestProviderGenerator extends BaseGenerator<Object> {
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


        generateImports(BaseRestServiceProvider.class, MessageConverter.class, ModuleRestRegistryDto.class);

        String restRegistryResourceName = null;
        if (ctx.getOptions().isRestRegistryAutoDiscoveryInResources()) {
            generateImports(ItemsLoadService.class, Services.class);
            restRegistryResourceName = new File(ctx.getOptions().getRestRegistryOutputJsonFileName()).getName();
        }


        ImportsHelper imports = new ImportsHelper();
        Collection<ClassDocInfo<?>> services = ctx.getData().getServicesByControllers().values();
        for (ClassDocInfo sd : services) {
            print("import ");
            print(sd.getQualifiedName());
            println(";");

            imports.addImport(sd.getQualifiedName());
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
        if (ctx.getOptions().isRestRegistryAutoDiscoveryInResources()) {
            println("(String baseUrl) {");
            pushIndentation("    ");
            println("this(baseUrl,");
            println(" (ModuleRestRegistryDto) Services.getService(ItemsLoadService.class, \"json\").loadItem(\"classpath:" +
                    restRegistryResourceName + "\"), null);");
            popIndentation();
            println("}");
            println();
        } else {
            println("(String baseUrl, ModuleRestRegistryDto registryDto) {");
            pushIndentation("    ");
            println("this(baseUrl, registryDto, null);");
            popIndentation();
            println("}");
            println();
        }

        //Most full constructor with baseUrl, rest registry and message converter
        print("public ");
        print(ctx.getServiceProviderSimpleName());
        println("(String baseUrl, ModuleRestRegistryDto registryDto, MessageConverter messageConverter) {");
        pushIndentation("    ");
        print("super(baseUrl, new ");
        print(BaseRestMethodRegistry.class.getSimpleName());
        print("(registryDto), ");
        print("messageConverter, ");
        println("new Class[] {");
        pushIndentation("    ");

        for (Iterator<ClassDocInfo<?>> iter = services.iterator(); iter.hasNext();) {
            ClassDocInfo<?> s = iter.next();
            print(imports.actualName(s.getQualifiedName()));
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

        for (ClassDocInfo<?> s : services) {
            println();
            print("public static ");
            String actualName = imports.actualName(s.getQualifiedName());
            print(actualName);
            print(" get");
            String simpleName = ClassUtils.simpleName(s.getQualifiedName());
            print(simpleName);
            println("() {");
            pushIndentation("    ");
            print("return INSTANCE.getRestService(");
            print(actualName);
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
