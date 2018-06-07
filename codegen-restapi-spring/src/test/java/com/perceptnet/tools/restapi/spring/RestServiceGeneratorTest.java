package com.perceptnet.tools.restapi.spring;

import com.perceptnet.commons.tests.TestGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 04.12.2017
 */
public class RestServiceGeneratorTest {
    private Map<String, ClassInfo> controllers;
    private GenerationContext ctx;
    private RestRegistryGenerator registryGenerator;
    private RestServiceGenerator serviceGenerator;
    private RestProviderGenerator providerGenerator;
    private ClassInfo crmControllerInfo;

    @BeforeClass(groups = {TestGroups.UNIT})
    public void beforeClass() throws Exception {
        Collection<ClassInfo> controllersInfo = new Persistence().loadClassInfos("classpath:TestControllers.doc.json");
        controllers = new HashMap<>();
        for (ClassInfo classInfo : controllersInfo) {
            controllers.put(classInfo.getName(), classInfo);
        }
        crmControllerInfo = controllers.get("CrmController");
    }

    @BeforeMethod(groups = {TestGroups.UNIT})
    public void beforeMethod() {
        ctx = new GenerationContext();
        serviceGenerator = new RestServiceGenerator(ctx);
        registryGenerator = new RestRegistryGenerator(ctx);
        providerGenerator = new RestProviderGenerator(ctx);
    }

    @Test(groups = {TestGroups.UNIT})
    public void testGenerateService() {
        serviceGenerator.generate(crmControllerInfo);
    }

    @Test(groups = {TestGroups.UNIT})
    public void testGenerateRestRegistry() {
        serviceGenerator.generate(crmControllerInfo);
        registryGenerator.generate(controllers.values());
        System.out.println();
        providerGenerator.generate();
    }

    @Test(groups = {TestGroups.UNIT})
    public void testGenerateFiles() {
        GenerationManager gm = new GenerationManager();
        gm.setCtx(gm.createContext(controllers.values()));
        gm.getCtx().setBaseOutputDir("target/generated-sources/java");

        gm.generate(controllers.values());

    }



}