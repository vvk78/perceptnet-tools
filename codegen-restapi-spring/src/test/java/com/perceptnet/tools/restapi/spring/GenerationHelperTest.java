package com.perceptnet.tools.restapi.spring;

import com.perceptnet.commons.tests.TestGroups;
import com.perceptnet.restclient.dto.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

/**
 * created by vkorovkin (vkorovkin@gmail.com) on 30.11.2017
 */
public class GenerationHelperTest {
    private GenerationHelper helper = GenerationHelper.I;


    @Test(groups = {TestGroups.UNIT})
    public void testParseHttpMethodSafely() throws Exception {
        assertEquals(helper.parseHttpMethodSafely("GET"), HttpMethod.get);
        assertEquals(helper.parseHttpMethodSafely("GeT"), HttpMethod.get);
        assertEquals(helper.parseHttpMethodSafely("get"), HttpMethod.get);
        assertEquals(helper.parseHttpMethodSafely("POST"), HttpMethod.post);
    }

    @Test(groups = {TestGroups.UNIT})
    public void testParseRequestMapping1() throws Exception {
        List result = helper.parseRequestMapping("/crm/some/{var1}/increase");
        assertEquals(result.size(), 3, "Wrong size");
        assertEquals(result.get(0), "/crm/some/", "Wrong item1");
        assertEquals(result.get(1), new String[]{"var1"}, "Wrong item2");
        assertEquals(result.get(2), "/increase", "Wrong item3");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testParseRequestMapping2() throws Exception {
        List result = helper.parseRequestMapping("/crm/some/{var1}/increase/{var2}");
        assertEquals(result.size(), 4, "Wrong size");
        assertEquals(result.get(0), "/crm/some/", "Wrong item1");
        assertEquals(result.get(1), new String[]{"var1"}, "Wrong item2");
        assertEquals(result.get(2), "/increase/", "Wrong item3");
        assertEquals(result.get(3), new String[]{"var2"}, "Wrong item4");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testParseRequestMapping3() throws Exception {
        List result = helper.parseRequestMapping("{var1}");
        assertEquals(result.size(), 1, "Wrong size");
        assertEquals(result.get(0), new String[]{"var1"}, "Wrong item1");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testParseBadRequestMapping() throws Exception {
        assertNull(helper.parseRequestMapping("{var1"));
        assertNull(helper.parseRequestMapping("{var1}/sc/{var"));
        assertNull(helper.parseRequestMapping("sc/{var"));
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageWhenSingleItem() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Collections.singletonList(new RestServiceInfo("com.aim.polyglot.restservice.crm.CrmRestService"))),
                "com.aim.polyglot.restservice.crm",
                "Wrong most general package");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageTwoClassesSamePackage() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Arrays.asList(
                        new RestServiceInfo("com.aim.polyglot.restservice.crm.CrmRestService"),
                        new RestServiceInfo("com.aim.polyglot.restservice.crm.TestRestService"))),
                "com.aim.polyglot.restservice.crm",
                "Wrong most general package");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageTwoClassesDifferentPackages() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Arrays.asList(
                        new RestServiceInfo("com.aim.polyglot.restservice.crm.CrmRestService"),
                        new RestServiceInfo("com.aim.polyglot.restservice.test.TestRestService"))),
                "com.aim.polyglot.restservice",
                "Wrong most general package");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageTwoClassesNoPackageInOne() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Arrays.asList(
                        new RestServiceInfo("com.aim.polyglot.restservice.crm.CrmRestService"),
                        new RestServiceInfo("TestRestService"))),
                "",
                "Wrong most general package");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageTwoClassesNoPackageInBoth() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Arrays.asList(
                        new RestServiceInfo("CrmRestService"),
                        new RestServiceInfo("TestRestService"))),
                "",
                "Wrong most general package");
    }

    @Test(groups = {TestGroups.UNIT})
    public void testMostCommonPackageOneClassNoPackage() throws Exception {
        assertEquals(
                helper.getMostGeneralPackage(Collections.singletonList(
                        new RestServiceInfo("CrmRestService"))),
                "",
                "Wrong most general package");
    }



}