package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.abstractions.Adaptor;
import com.perceptnet.commons.json.JsonService;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;
import com.perceptnet.restclient.dto.ModuleRestRegistryDto;
import com.perceptnet.restclient.dto.RestMethodDescription;
import com.perceptnet.restclient.dto.ServiceRestRegistryDto;
import com.perceptnet.tools.codegen.JavaSrcGenerationHelper;
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
    private GenerationOptions options;
    private RestGenerationHelper restGenerationHelper;
    private JavaSrcGenerationHelper javaSrcGenerationHelper;

    public static void main(String[] args) {
        Collection<ClassDocInfo> controllerInfos = null;
        Collection<ClassDocInfo> serviceInfos = null;
        String outputFileName = null;
        PersistenceService p = new PersistenceService();

        //Parse options
        Map<String, List<String>> options = OptionUtils.parseOptions(args);
        GenerationOptions go = new GenerationOptions();
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            String optionName = entry.getKey();
            List<String> optionArgs = entry.getValue();
            if ("-c".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -c option is not specified");
                }
                controllerInfos = p.loadClassInfos(optionArgs.get(0));
            } else if ("-s".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -s option is not specified");
                }
                serviceInfos = p.loadClassInfos(optionArgs.get(0));
            } else if ("-adapter".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -adapter option is not specified");
                }
                go.installAdaptor(optionArgs.get(0));
            } else if ("-f".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -f option is not specified");
                }
                go.setRestRegistryOutputJsonFileName(optionArgs.get(0));
            } else if ("-autoDiscovery".equalsIgnoreCase(optionName)) {
                go.setRestRegistryAutoDiscoveryInResources(true);
            } else if ("-srcOutDir".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -srcOutDir option is not specified");
                }
                go.setSrcOutDir(optionArgs.get(0));
            } else if ("-signInBasic".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -srcOutDir option is not specified");
                }
                go.setBasicSignInRequestPath(optionArgs.get(0));
            } else if ("-signOut".equalsIgnoreCase(optionName)) {
                if (optionArgs.isEmpty()) {
                    throw new IllegalArgumentException("Required value of -srcOutDir option is not specified");
                }
                go.setSignOutRequestPath(optionArgs.get(0));
            }
        }

        go.validate();
        SvrGenerationManager gm = new SvrGenerationManager(go);
        controllerInfos = gm.filterControllersInfo(options.get("-inc"), options.get("-exl"), options.containsKey("-reg"), controllerInfos);
        gm.generate(controllerInfos, serviceInfos);
    }


    public SvrGenerationManager(GenerationOptions options) {
        this.options = options == null ? new GenerationOptions() : options;
        this.restGenerationHelper = new RestGenerationHelper();
        this.javaSrcGenerationHelper = new JavaSrcGenerationHelper();
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
        GenerationContext ctx = new GenerationContext(d, options);

        ModuleRestRegistryDto registryDto = buildRegistryDto(d.getRestServicesByServiceName());

        JsonService js = new JsonService();
        if (options.getRestRegistryOutputJsonFileName() != null) {
            js.saveItem(options.getRestRegistryOutputJsonFileName(), registryDto);
        } else {
            js.saveItem(System.out, registryDto);
        }

        if (!registryDto.getServices().isEmpty()) {
            RestProviderGenerator rpg = new RestProviderGenerator(ctx);
            javaSrcGenerationHelper.generateJavaSrcFile(ctx, ctx.getServiceProviderQualifiedName(), rpg, null);
            rpg.generate();
        }

    }

    public ModuleRestRegistryDto buildRegistryDto(Map<String, RestServiceInfo> rsiData) {
        ModuleRestRegistryDto registryDto = new ModuleRestRegistryDto();
        for (RestServiceInfo rsi : rsiData.values()) {
            ServiceRestRegistryDto serviceDto = new ServiceRestRegistryDto();
            registryDto.getServices().put(rsi.getServiceDoc().getQualifiedName(), serviceDto);
            for (RestMethodInfo m : rsi.getRestMethods()) {
                if (m.getServiceMethodDoc() != null) {
                    String methodKey = restGenerationHelper.buildServiceMethodQualifiedSignature(m.getServiceMethodDoc());
                    RestMethodDescription rmd = restGenerationHelper.createRestDescription(rsi, m);
                    serviceDto.getMethods().put(methodKey, rmd);
                }
            }
        }
        return registryDto;
    }
}
