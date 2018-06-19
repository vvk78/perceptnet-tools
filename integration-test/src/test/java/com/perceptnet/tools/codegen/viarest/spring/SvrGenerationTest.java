package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.restclient.BaseRestMethodRegistry;
import com.perceptnet.restclient.dto.ModuleRestRegistryDto;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.PersistenceService;
import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;
import java.util.Collection;

import static com.perceptnet.commons.tests.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 18.06.2018
 */
public class SvrGenerationTest {

    @Test(groups = {INTEGRATION})
    public void testSvrDataBuilder() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        GenerationData d = b.build(controllers, services);

        System.out.println("Controllers: " + controllers.size());
        System.out.println("Services: " + services.size());

        System.out.println("Rest services generated: " + d.getRestServicesByServiceName().size());
    }

    @Test(groups = {INTEGRATION})
    public void testRestServiceProviderGenerator() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        SvrGenerationManager gm = new SvrGenerationManager(null);
        gm.generate(controllers, services);
    }

    @Test(groups = {INTEGRATION})
    public void testBaseRestMethodRegistry() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();
        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");
        GenerationData d = b.build(controllers, services);
        SvrGenerationManager manager = new SvrGenerationManager(null);
        ModuleRestRegistryDto registryDto = manager.buildRegistryDto(d.getRestServicesByServiceName());

        BaseRestMethodRegistry brmr = new BaseRestMethodRegistry(registryDto);

        assertTrue(brmr != null);
    }
}
