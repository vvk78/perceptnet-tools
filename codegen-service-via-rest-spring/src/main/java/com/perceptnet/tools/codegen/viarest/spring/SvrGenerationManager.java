package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.abstractions.Adaptor;
import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.PersistenceService;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.perceptnet.commons.utils.StringUtils.unquote;

/**
 * created by vkorovkin on 15.06.2018
 */
public class SvrGenerationManager {

    public static void main(String[] args) {
        Map<String, List<String>> options = OptionUtils.parseOptions(args);

        Collection<ClassDocInfo> controllerInfos = null;
        Collection<ClassDocInfo> serviceInfos = null;
        PersistenceService p = new PersistenceService();

        GenerationOptions go = new GenerationOptions();
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            @Nullable String optionName = entry.getKey();
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
            }
        }

        GenerationData d = new GenerationData(controllerInfos, serviceInfos);
        SvrGenerationManager gm = new SvrGenerationManager(go.getAdaptor());
        controllerInfos = gm.filterControllersInfo(options.get("-inc"), options.get("-exl"), options.containsKey("-reg"), controllerInfos);
        gm.generate(controllerInfos, serviceInfos);
    }

    private SvrGenerationAdaptor adaptor;

    public SvrGenerationManager(SvrGenerationAdaptor adaptor) {
        this.adaptor = adaptor;
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
        GenerationData data = new GenerationDataBuilder().build(controllers, services);
        for (RestServiceInfo rsi : data.getRestServices().values()) {

        }
    }
}