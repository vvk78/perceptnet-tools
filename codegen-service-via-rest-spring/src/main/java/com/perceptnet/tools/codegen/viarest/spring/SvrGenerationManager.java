package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.abstractions.Adaptor;
import com.perceptnet.commons.json.JsonService;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;
import com.perceptnet.restclient.BaseRestMethodRegistry;
import com.perceptnet.restclient.dto.ModuleRestRegistryDto;
import com.perceptnet.restclient.dto.RestMethodDescription;
import com.perceptnet.restclient.dto.ServiceRestRegistryDto;
import com.perceptnet.tools.codegen.rest.RestGenerationHelper;
import com.perceptnet.tools.codegen.rest.RestMethodInfo;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.PersistenceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.perceptnet.commons.utils.StringUtils.unquote;

/**
 * created by vkorovkin on 15.06.2018
 */
public class SvrGenerationManager {
    private RestGenerationHelper helper;
    private String outputFileName;

    public static void main(String[] args) {
        Map<String, List<String>> options = OptionUtils.parseOptions(args);

        Collection<ClassDocInfo> controllerInfos = null;
        Collection<ClassDocInfo> serviceInfos = null;
        String outputFileName = null;
        PersistenceService p = new PersistenceService();

        GenerationOptions go = new GenerationOptions();
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            String optionName = entry.getKey();
            List<String> optionArgs = entry.getValue();
            if ("-c".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -c option is not specified");
                }
                controllerInfos = p.loadClassInfos(optionArgs.get(0));
            } else if ("-s".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -s option is not specified");
                }
                serviceInfos = p.loadClassInfos(optionArgs.get(0));
            } else if ("-adapter".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -adapter option is not specified");
                }
                go.installAdaptor(optionArgs.get(0));
            } else if ("-f".equals(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -f option is not specified");
                }
                outputFileName = optionArgs.get(0);
            }
        }

        SvrGenerationManager gm = new SvrGenerationManager(go.getAdaptor());
        gm.outputFileName = outputFileName;
        controllerInfos = gm.filterControllersInfo(options.get("-inc"), options.get("-exl"), options.containsKey("-reg"), controllerInfos);
        gm.generate(controllerInfos, serviceInfos);
    }

    private SvrGenerationAdaptor adaptor;

    public SvrGenerationManager(SvrGenerationAdaptor adaptor) {
        this.adaptor = adaptor;
        this.helper = new RestGenerationHelper();
    }

    Collection<ClassDocInfo> filterControllersInfo(List<String> incNameMasks,
                                                   List<String> exlNameMasks, boolean regexMasks, Collection<ClassDocInfo> c) {
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
            public String adapt(ClassDocInfo classInfo) {
                return classInfo == null ? null : classInfo.getQualifiedName();
            }
        });
        return filter.addIncluded(c, new ArrayList<>(50));
    }

    public void generate(Collection<ClassDocInfo> controllers, Collection<ClassDocInfo> services) {
        GenerationDataBuilder b = new GenerationDataBuilder();

        GenerationData d = b.build(controllers, services);
        GenerationContext ctx = new GenerationContext(d, adaptor);

        ModuleRestRegistryDto registryDto = buildRegistryDto(d.getRestServicesByServiceName());

        JsonService js = new JsonService();
        if (outputFileName != null) {
            js.saveItem(outputFileName, registryDto);
        } else {
            js.saveItem(System.out, registryDto);
        }

        RestProviderGenerator rpg = new RestProviderGenerator(ctx);
        rpg.setOut(System.out);
        rpg.generate();

    }

    public ModuleRestRegistryDto buildRegistryDto(Map<String, RestServiceInfo> rsiData) {
        ModuleRestRegistryDto registryDto = new ModuleRestRegistryDto();
        for (RestServiceInfo rsi : rsiData.values()) {
            ServiceRestRegistryDto serviceDto = new ServiceRestRegistryDto();
            registryDto.getServices().put(rsi.getServiceDoc().getQualifiedName(), serviceDto);
            for (RestMethodInfo m : rsi.getRestMethods()) {
                if (m.getServiceMethodDoc() != null) {
                    String methodKey = helper.buildServiceMethodQualifiedSignature(m.getServiceMethodDoc());
                    RestMethodDescription rmd = helper.createRestDescription(rsi, m);
                    serviceDto.getMethods().put(methodKey, rmd);
                }
            }
        }
        return registryDto;
    }
}
