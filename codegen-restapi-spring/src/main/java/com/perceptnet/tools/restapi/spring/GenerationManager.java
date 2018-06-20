package com.perceptnet.tools.restapi.spring;

import com.perceptnet.abstractions.Adaptor;
import com.perceptnet.commons.utils.FileUtils;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.perceptnet.commons.utils.StringUtils.unquote;
import static com.perceptnet.tools.restapi.spring.GenerationUtils.*;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public class GenerationManager {
    private GenerationAdaptor adaptor;
    private GenerationContext ctx;

    private RestServiceGenerator serviceGenerator;
    private RestRegistryGenerator registryGenerator;
    private RestProviderGenerator providerGenerator;

    public static void main(String[] args) {
        Map<String, List<String>> options = OptionUtils.parseOptions(args);

        Collection<ClassInfo> controllerInfos = null;
        GenerationOptions go = new GenerationOptions();
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            @Nullable String optionName = entry.getKey();
            List<String> optionArgs = entry.getValue();
            if ("-f".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -f option is not specified");
                }
                Persistence p = new Persistence();
                controllerInfos = p.loadClassInfos(optionArgs.get(0));
            } else if ("-adapter".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -adapter option is not specified");
                }
                go.installAdaptor(optionArgs.get(0));
            }
        }

        GenerationManager gm = new GenerationManager(go.getAdaptor());
        controllerInfos = gm.filterControllersInfo(options.get("-inc"), options.get("-exl"), options.containsKey("-reg"), controllerInfos);
        gm.generate(controllerInfos);
    }

    GenerationManager() {
        this(null);
    }

    GenerationManager(GenerationAdaptor adaptor) {
        this.adaptor = adaptor != null ? adaptor : new DefaultGenerationAdaptor();
    }

    Collection<ClassInfo> filterControllersInfo(List<String> incNameMasks, List<String> exlNameMasks, boolean regexMasks, Collection<ClassInfo> c) {
        if ((incNameMasks == null || incNameMasks.isEmpty()) && (exlNameMasks == null || exlNameMasks.isEmpty())) {
            return c;
        }
        incNameMasks = unquote(incNameMasks);
        exlNameMasks = unquote(exlNameMasks);
        IncExlRegexFilter filter = new IncExlRegexFilter();
        if (regexMasks) {
            filter.setIncFiltersStr(incNameMasks);
            filter.setExlFiltersStr(exlNameMasks);
        } else {
            filter.setIncFiltersSimpleWildcards(incNameMasks);
            filter.setExlFiltersSimpleWildcards(exlNameMasks);
        }
        filter.setIncludeNulls(false);
        filter.setItemAdaptor(new Adaptor<ClassInfo, String>() {
            @Override
            public String adapt(ClassInfo classInfo) {
                return classInfo == null ? null : classInfo.getQualifiedName();
            }
        });
        return filter.addIncluded(c, new ArrayList<>(50));
    }

    void generate(Collection<ClassInfo> controllers) {
        if (ctx == null) {
            ctx = createContext(controllers);
        }

        //Rest services:
        serviceGenerator = new RestServiceGenerator(ctx);
        for (ClassInfo c : controllers) {
            RestServiceInfo s = ctx.getRestServices().get(c);
            generateSourceFile(getSourceFileNameForClass(s.getServiceQualifiedName()), serviceGenerator, c);
        }

        //Rest methods registry and provider:
        generateSourceFile(getSourceFileNameForClass(ctx.getRegistryQualifiedName()), new RestRegistryGenerator(ctx), controllers);
        generateSourceFile(getSourceFileNameForClass(ctx.getServiceProviderQualifiedName()), new RestProviderGenerator(ctx), null);
    }

    void generateViaRest(Collection<ClassInfo> controllers) {
        if (ctx == null) {
            ctx = createContext(controllers);
        }

        for (ClassInfo c : controllers) {
            RestServiceInfo s = ctx.getRestServices().get(c);
            generateSourceFile(getSourceFileNameForClass(s.getServiceQualifiedName()), serviceGenerator, c);
        }

        //Rest methods registry and provider:
        generateSourceFile(getSourceFileNameForClass(ctx.getRegistryQualifiedName()), new RestRegistryGenerator(ctx), controllers);
        generateSourceFile(getSourceFileNameForClass(ctx.getServiceProviderQualifiedName()), new RestProviderGenerator(ctx), null);
    }

    GenerationContext getCtx() {
        return ctx;
    }

    void setCtx(GenerationContext ctx) {
        this.ctx = ctx;
    }

    GenerationContext createContext(Collection<ClassInfo> controllers) {
        GenerationContext ctx = new GenerationContext(adaptor);
        for (ClassInfo c : controllers) {
            String serviceQualifiedName = this.adaptor.getServiceQualifiedNameFromController(c.getQualifiedName());
            RestServiceInfo serviceInfo = new RestServiceInfo(serviceQualifiedName);
            ctx.getRestServices().put(c, serviceInfo);
        }
        return ctx;
    }

    private <P> void generateSourceFile(String fileNameToBeGenerated, BaseGenerator<P> generator, P generationParams) {
        FileUtils.prepareFileForReCreation(fileNameToBeGenerated);
        try (PrintStream ps = new PrintStream(new FileOutputStream(fileNameToBeGenerated), true, ctx.getEncoding())) {
            generator.setOut(ps);
            generator.generate(generationParams);
            ps.flush();
        } catch (IOException e) {
            throw new RuntimeException("Cannot generate '" + fileNameToBeGenerated + "' due to " + e, e);
        }
    }



    private String getSourceFileNameForClass(String qualifiedClassName) {
        String result = qualifiedClassName.replace(".", "/") + ".java";
        String baseOutputDir = ctx.getBaseOutputDir();
        if (baseOutputDir != null && !baseOutputDir.isEmpty()) {
            if (!baseOutputDir.endsWith("/") && !baseOutputDir.endsWith("\\") && !baseOutputDir.endsWith(File.separator)) {
                result = baseOutputDir + File.separator + result;
            } else {
                result = baseOutputDir + result;
            }
        }

        return result;
    }

}
