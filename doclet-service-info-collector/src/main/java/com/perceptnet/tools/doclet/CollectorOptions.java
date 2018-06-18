package com.perceptnet.tools.doclet;


import com.perceptnet.commons.utils.IncExlRegexFilter;
import com.perceptnet.commons.utils.OptionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by vkorovkin on 08.06.2018
 */
public class CollectorOptions<SELF extends CollectorOptions> {
    private Set<ItemType> collectedItemTypes;
    private IncExlRegexFilter itemNamesFilter;
    private boolean collectParamAnnotations;
    private boolean collectClassAnnotations;
    private String outputFileName;

    public CollectorOptions() {
    }

    /**
     * Constructs collection options from command args
     */
    public CollectorOptions(String[] args) {
        Map<String, List<String>> ro = OptionUtils.parseOptions(args);
        if (ro.containsKey("-inc") || ro.containsKey("-exl") || ro.containsKey("-reg")) {
            itemNamesFilter = new IncExlRegexFilter();
        }
    }

    public boolean isCollectParamAnnotations() {
        return collectParamAnnotations;
    }

    public SELF setCollectParamAnnotations(boolean collectParamAnnotations) {
        this.collectParamAnnotations = collectParamAnnotations;
        return (SELF) this;
    }

    public boolean isCollectClassAnnotations() {
        return collectClassAnnotations;
    }

    public SELF setCollectClassAnnotations(boolean collectClassAnnotations) {
        this.collectClassAnnotations = collectClassAnnotations;
        return (SELF) this;
    }

    public Set<ItemType> getCollectedItemTypes() {
        return collectedItemTypes;
    }

    public SELF addCollectedItemTypes(ItemType ... types) {
        if (collectedItemTypes == null) {
            collectedItemTypes = new HashSet<>();
        }
        collectedItemTypes.addAll(Arrays.asList(types));
        return (SELF) this;
    }

    public void setCollectedItemTypes(Set<ItemType> collectedItemTypes) {
        this.collectedItemTypes = collectedItemTypes;
    }

    public IncExlRegexFilter getItemNamesFilter() {
        return itemNamesFilter;
    }

    public SELF setItemNamesFilter(IncExlRegexFilter itemNamesFilter) {
        this.itemNamesFilter = itemNamesFilter;
        return (SELF) this;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}
