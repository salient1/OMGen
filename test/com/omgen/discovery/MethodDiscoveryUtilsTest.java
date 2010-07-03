package com.omgen.discovery;

import com.omgen.generator.ConstructorInfo;
import com.omgen.generator.SetterMethod;
import com.omgen.samples.sample1.Address;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.Assert.*;


/**
 *
 */
public class MethodDiscoveryUtilsTest {
    @Test
    public void testBuildConstructorList() throws Exception {
        List<ConstructorInfo> constructorInfos = ConstructorUtils.buildConstructorList(String.class);
        assertNotNull(constructorInfos);
        assertEquals(15, constructorInfos.size());
        assertNull(constructorInfos.get(0).getParameterInfos());
        assertNotNull(constructorInfos.get(1).getParameterInfos());
    }

    @Test
    public void testBuildMethodList() throws Exception {
        List<SetterMethod> setterMethods = MethodDiscoveryUtils.buildMethodList(Address.class);
        assertNotNull(setterMethods);
        assertEquals(5, setterMethods.size());
        SetterMethod setterMethod = setterMethods.get(0);
        assertEquals("String", setterMethod.getArgType());
        assertEquals("state", setterMethod.getShortName());
        assertEquals("setState", setterMethod.getName());
    }

    @Test
    public void testGetShortName() throws Exception {
        String shortName = MethodDiscoveryUtils.getShortName(Address.class.getMethod("setState", String.class));
        assertNotNull(shortName);
        assertEquals("state", shortName);
    }

    @Test
    public void testGetSimplestClassName() throws Exception {
        String shortName = MethodDiscoveryUtils.getSimplestClassName(Address.class);
        assertNotNull(shortName);
        assertEquals("com.omgen.samples.sample1.Address", shortName);
    }

    @Test
    public void testGetWriteMethods() throws Exception {
        List<Method> methods = MethodDiscoveryUtils.getWriteMethods(Address.class);
        assertNotNull(methods);
        assertEquals(5, methods.size());
        for (Method method : methods) {
            assertTrue(method.getName().startsWith("set"));
            assertTrue(Modifier.isPublic(method.getModifiers()));
            assertTrue(method.getReturnType().getName().equals("void"));
        }
    }
}
