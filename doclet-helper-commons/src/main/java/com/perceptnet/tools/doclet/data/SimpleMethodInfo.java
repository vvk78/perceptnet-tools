package com.perceptnet.tools.doclet.data;

import java.util.ArrayList;
import java.util.List;

/**
 * created by vkorovkin on 05.06.2018
 */
public class SimpleMethodInfo<SELF extends SimpleMethodInfo> extends BaseItemInfo<SELF> {
    private String flatSignature;
    private String returnType;
    private String actualReturnType;

    private final List<SimpleParamInfo> params = new ArrayList<>();

    public SimpleMethodInfo() {
    }

    public SimpleMethodInfo(String name, String rawComment, String flatSignature, String returnType, String actualReturnType) {
        super(name, rawComment);
        this.flatSignature = flatSignature;
        this.returnType = returnType;
        this.actualReturnType = actualReturnType;
    }

    public String getFlatSignature() {
        return flatSignature;
    }

    public SELF setFlatSignature(String flatSignature) {
        this.flatSignature = flatSignature;
        return (SELF) this;
    }

    public String getReturnType() {
        return returnType;
    }

    public SELF setReturnType(String returnType) {
        this.returnType = returnType;
        return (SELF) this;
    }

    public String getActualReturnType() {
        return actualReturnType;
    }

    public SELF setActualReturnType(String actualReturnType) {
        this.actualReturnType = actualReturnType;
        return (SELF) this;
    }

    public List<SimpleParamInfo> getParams() {
        return params;
    }
}
