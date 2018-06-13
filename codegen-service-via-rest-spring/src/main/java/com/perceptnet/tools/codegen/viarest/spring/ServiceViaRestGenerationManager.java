package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.abstractions.Adaptor;
import com.perceptnet.commons.utils.FileUtils;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;
import com.perceptnet.tools.codegen.BaseGenerator;
import com.perceptnet.tools.doclet.data.ClassDocInfo;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.perceptnet.commons.utils.StringUtils.*;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 12.12.2017
 */
public class ServiceViaRestGenerationManager {
    private GenerationAdaptor adaptor;
    private GenerationContext ctx;

    private RestRegistryGenerator registryGenerator;
    private RestProviderGenerator providerGenerator;

    public static void main(String[] args) {
        Map<String, List<String>> options = OptionUtils.parseOptions(args);

        Collection<ClassDocInfo> controllerInfos = null;
        GenerationOptions go = new GenerationOptions();
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            @Nullable String optionName = entry.getKey();
            List<String> optionArgs = entry.getValue();
            if ("-f".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -f option is not specified");
                }
                Persistence p = new Persistence();
                controllerInfos = p.loadClassDocInfos(optionArgs.get(0));
            } else if ("-adapter".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -adapter option is not specified");
                }
                go.installAdaptor(optionArgs.get(0));
            }
        }

        ServiceViaRestGenerationManager gm = new ServiceViaRestGenerationManager(go.getAdaptor());
        controllerInfos = gm.filterControllersInfo(options.get("-inc"), options.get("-exl"), options.containsKey("-reg"), controllerInfos);
        gm.generate(controllerInfos);
    }

    ServiceViaRestGenerationManager() {
        this(null);
    }

    ServiceViaRestGenerationManager(GenerationAdaptor adaptor) {
        this.adaptor = adaptor != null ? adaptor : new DefaultGenerationAdaptor();
    }

    Collection<ClassDocInfo> filterControllersInfo(List<String> incNameMasks, List<String> exlNameMasks, boolean regexMasks, Collection<ClassDocInfo> c) {
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
        filter.setItemAdaptor(new Adaptor<ClassDocInfo, String>() {
            @Override
            public String adapt(ClassDocInfo ClassDocInfo) {
                return ClassDocInfo == null ? null : ClassDocInfo.getQualifiedName();
            }
        });
        return filter.addIncluded(c, new ArrayList<>(50));
    }

    void generate(Collection<ClassDocInfo> controllers) {
        if (ctx == null) {
            ctx = createContext(controllers);
        }

        //Rest services:
        serviceGenerator = new RestServiceGenerator(ctx);
        for (ClassDocInfo c : controllers) {
            OldRestServiceClientInfo s = ctx.getRestServices().get(c);
            generateSourceFile(getSourceFileNameForClass(s.getServiceQualifiedName()), serviceGenerator, c);
        }

        //Rest methods registry and provider:
        generateSourceFile(getSourceFileNameForClass(ctx.getRegistryQualifiedName()), new RestRegistryGenerator(ctx), controllers);
        generateSourceFile(getSourceFileNameForClass(ctx.getServiceProviderQualifiedName()), new RestProviderGenerator(ctx), null);
    }

    void generateViaRest(Collection<ClassDocInfo> controllers) {
        if (ctx == null) {
            ctx = createContext(controllers);
        }

        for (ClassDocInfo c : controllers) {
            OldRestServiceClientInfo s = ctx.getRestServices().get(c);
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

    GenerationContext createContext(Collection<ClassDocInfo> controllers) {
        GenerationContext ctx = new GenerationContext(adaptor);
        for (ClassDocInfo c : controllers) {
            String serviceQualifiedName = this.adaptor.getServiceQualifiedNameFromController(c.getQualifiedName());
            OldRestServiceClientInfo serviceInfo = new OldRestServiceClientInfo(serviceQualifiedName);
            ctx.getRestServices().put(c, serviceInfo);
        }
        return ctx;
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
