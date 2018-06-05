package com.perceptnet.tools.doclet.data;

/**
 * Simple param info, does not extends BaseItemInfo (todo -- may it worth doing that?)
 *
 * created by vkorovkin on 05.06.2018
 */
public class SimpleParamInfo<SELF extends SimpleParamInfo> implements DocInfo {
    private String name;
    private String type;
    private String actualTypeName;

    public SimpleParamInfo() {
    }

    public SimpleParamInfo(String name, String type, String actualTypeName) {
        this.name = name;
        this.type = type;
        this.actualTypeName = actualTypeName;
    }

    public String getName() {
        return name;
    }

    public SELF setName(String name) {
        this.name = name;
        return (SELF) this;
    }

    public String getType() {
        return type;
    }

    public SELF setType(String type) {
        this.type = type;
        return (SELF) this;
    }

    public String getActualTypeName() {
        return actualTypeName;
    }

    public SELF setActualTypeName(String actualTypeName) {
        this.actualTypeName = actualTypeName;
        return (SELF) this;
    }
}
