package com.perceptnet.tools.doclet.data;

/**
 * created by vkorovkin on 06.06.2018
 */
public class TypeInfo<SELF extends TypeInfo> {
    /**
     * Qualified type name
     */
    private String qualifiedName;
    /**
     * Comes from class context, may be short, may be long -- i.e. it depends on imports;
     */
    private String actualName;

    public TypeInfo(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public TypeInfo(String qualifiedName, String actualName) {
        this.qualifiedName = qualifiedName;
        this.actualName = actualName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public SELF setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        return (SELF) this;
    }

    public String getActualName() {
        return actualName;
    }

    public SELF setActualName(String actualName) {
        this.actualName = actualName;
        return (SELF) this;
    }

    @Override
    public String toString() {
        return actualName;
    }

    public boolean isClass(Class c) {
        return qualifiedName.equals(c.getName());
    }
}
