package com.perceptnet.tools.doclet.data;

/**
 * Base class for program items (classes, interfaces, methods and instance and class fields)
 *
 * created by vkorovkin on 05.06.2018
 */
public class BaseItemInfo<SELF extends BaseItemInfo> implements DocInfo {
    private String name;
    private String rawComment;

    public BaseItemInfo() {
    }

    public BaseItemInfo(String name, String rawComment) {
        this.name = name;
        this.rawComment = rawComment;
    }

    public String getRawComment() {
        return rawComment;
    }

    public SELF setRawComment(String rawComment) {
        this.rawComment = rawComment;
        return (SELF) this;
    }

    public String getName() {
        return name;
    }

    public SELF setName(String name) {
        this.name = name;
        return (SELF) this;
    }
}
