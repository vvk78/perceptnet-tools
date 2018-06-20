package com.perceptnet.tools.codegen.viarest.spring;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 20.12.2017
 */
class GenerationOptions {
    /**
     * Name of the file where to put
     */
    private String restRegistryOutputJsonFileName;

    /**
     * This option controls how we want to get rest registry dto in generated RestServiceProvider.
     * <p>
     * If set to true, generated RestServiceProvider tries to load it from <b>resource</b> with name deducted from
     * {@link #restRegistryOutputJsonFileName}.
     * <p>
     * Note that {@link #restRegistryOutputJsonFileName} is a required if this option is set.
     * <p>
     * Note also that if you use this mode, you will have to add dependencies on perceptnet commons components to your project:
     * <p>
     * <dependency>
     * <groupId>com.perceptnet.commons</groupId>
     * <artifactId>commons-api</artifactId>
     * <version>${perceptnet.commons.version}</version>
     * </dependency>
     * <p>
     * <dependency>
     * <groupId>com.perceptnet.commons</groupId>
     * <artifactId>json-io</artifactId>
     * <version>${perceptnet.commons.version}</version>
     * </dependency>
     */
    private boolean restRegistryAutoDiscoveryInResources;

    private boolean generate;
    private SvrGenerationAdaptor adaptor;

    private String srcOutDir;


    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public SvrGenerationAdaptor getAdaptor() {
        return adaptor;
    }

    public void installAdaptor(String adaptorQualifiedName) {
        if (adaptorQualifiedName == null || adaptorQualifiedName.trim().isEmpty()) {
            this.adaptor = null;
            return;
        }
        Class adaptorClass;
        try {
            adaptorClass = Class.forName(adaptorQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load specified generation adaptor class '" + adaptorQualifiedName + "' due to " + e, e);
        }
        try {
            this.adaptor = (SvrGenerationAdaptor) adaptorClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate generation adaptor '" + adaptorQualifiedName + "' due to " + e, e);
        }
    }

    public String getRestRegistryOutputJsonFileName() {
        return restRegistryOutputJsonFileName;
    }

    public void setRestRegistryOutputJsonFileName(String restRegistryOutputJsonFileName) {
        this.restRegistryOutputJsonFileName = restRegistryOutputJsonFileName;
    }

    public boolean isRestRegistryAutoDiscoveryInResources() {
        return restRegistryAutoDiscoveryInResources;
    }

    public void setRestRegistryAutoDiscoveryInResources(boolean restRegistryAutoDiscoveryInResources) {
        this.restRegistryAutoDiscoveryInResources = restRegistryAutoDiscoveryInResources;
    }

    public String getSrcOutDir() {
        return srcOutDir;
    }

    public void setSrcOutDir(String srcOutDir) {
        this.srcOutDir = srcOutDir;
    }

    void validate() {
        if (restRegistryAutoDiscoveryInResources) {
            if (restRegistryOutputJsonFileName == null || restRegistryOutputJsonFileName.trim().isEmpty()) {
                throw new IllegalArgumentException("restRegistryOutputJsonFileName (-f option) must be specified " +
                        "when restRegistryAutoDiscoveryInResources (-autoDiscovery option) is set");
            }
        }
    }
}
