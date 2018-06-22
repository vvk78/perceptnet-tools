package com.perceptnet.tools.codegen.viarest.spring;

import com.perceptnet.commons.utils.MapUtilsJ18;
import com.perceptnet.restclient.BaseRestMethodRegistry;
import com.perceptnet.restclient.dto.ModuleRestRegistryDto;
import com.perceptnet.restclient.dto.RestMethodDescription;
import com.perceptnet.tools.codegen.rest.RestGenerationHelper;
import com.perceptnet.tools.codegen.rest.RestMethodInfo;
import com.perceptnet.tools.codegen.rest.RestServiceInfo;
import com.perceptnet.tools.doclet.data.ClassDocInfo;
import com.perceptnet.tools.doclet.data.PersistenceService;
import hello.app.person.PersonService;
import org.testng.annotations.Test;

import java.util.Collection;


import static com.perceptnet.commons.tests.TestGroups.INTEGRATION;
import static org.testng.Assert.*;

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
    public void testCreatePersonMapping() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        GenerationData d = b.build(controllers, services);

        RestGenerationHelper restGenerationHelper = new RestGenerationHelper();
        RestServiceInfo rsi = d.getRestServicesByServiceName().get(PersonService.class.getName());
        RestMethodInfo rmi = rsi.getRestMethods().stream().filter(
                m -> {return m.getServiceMethodDoc().getName().contains("create");}).findFirst().get();

        RestMethodDescription rmd = restGenerationHelper.createRestDescription(rsi, rmi);

        assertTrue(rmd.getPathPieces().length > 0, "Controller level RestMapping path is not accounted");
        assertTrue(rmd.getPathPieces()[0].contains("person"), "Controller level RestMapping path is not accounted");
    }

    @Test(groups = {INTEGRATION})
    public void testUpdatePersonMapping() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        ClassDocInfo<?> pc = MapUtilsJ18.map(controllers, ClassDocInfo::getQualifiedName).get("hello.app.controllers.PersonController");

        RestGenerationHelper h = new RestGenerationHelper();
        RestServiceInfo rsi = h.extractRestServiceInfoFromController(pc);


        RestMethodInfo rmi = rsi.getRestMethods().stream().filter(m -> {return m.getControllerMethodDoc().getName().contains("update");}).findFirst().get();
        assertNotNull(rmi, "Update method is not found");

        RestMethodDescription rmd = h.createRestDescription(rsi, rmi);

        assertTrue(rmd.getPathPieces().length > 0, "Controller level RestMapping path is not accounted");
        assertTrue(rmd.getPathPieces()[0].contains("person"), "Controller level RestMapping path is not accounted");
    }

    @Test(groups = {INTEGRATION})
    public void testDeletePersonMapping() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        ClassDocInfo<?> pc = MapUtilsJ18.map(controllers, ClassDocInfo::getQualifiedName).get("hello.app.controllers.PersonController");

        RestGenerationHelper h = new RestGenerationHelper();
        RestServiceInfo rsi = h.extractRestServiceInfoFromController(pc);


        RestMethodInfo rmi = rsi.getRestMethods().stream().filter(m -> {return m.getControllerMethodDoc().getName().contains("delete");}).findFirst().get();
        assertNotNull(rmi, "Update method is not found");

        RestMethodDescription rmd = h.createRestDescription(rsi, rmi);

        assertTrue(rmd.getPathPieces().length > 0, "Controller level RestMapping path is not accounted");
        assertTrue(rmd.getPathPieces()[0].contains("person"), "Controller level RestMapping path is not accounted");
    }

    @Test(groups = {INTEGRATION})
    public void testRestServiceProviderGenerator() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        GenerationOptions go = new GenerationOptions();
        go.setBasicSignInRequestPath("auth/sign-in/basic");
        go.setSignOutRequestPath("auth/sign-out");
        SvrGenerationManager gm = new SvrGenerationManager(go);
        gm.generate(controllers, services);
    }

    @Test(groups = {INTEGRATION})
    public void testRestServiceProviderGeneratorWithRegistryAutodiscovery() {
        GenerationDataBuilder b = new GenerationDataBuilder();
        PersistenceService p = new PersistenceService();

        Collection<ClassDocInfo> controllers = p.loadClassInfos("classpath:example_controllers.json");
        Collection<ClassDocInfo> services = p.loadClassInfos("classpath:example_services.json");

        GenerationOptions o = new GenerationOptions();
        o.setRestRegistryOutputJsonFileName("temp/ExampleRestRegistry.json");
        o.setRestRegistryAutoDiscoveryInResources(true);
        SvrGenerationManager gm = new SvrGenerationManager(o);
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
