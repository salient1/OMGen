package com.omgen.discovery;

import com.omgen.InvocationContext;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassUtilsTest {
    @Mock CommandLine commandLine;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetClasses() throws Exception {
        when(commandLine.getOptions()).thenReturn(new Option[0]);
        when(commandLine.getArgs()).thenReturn(new String[] {"com.omgen"});
        InvocationContext invocationContext = spy(new InvocationContext(commandLine));

        when(invocationContext.isScanSubPackages()).thenReturn(true);
        List<String> classes = new PackageExplorer("com.omgen").getClasses(invocationContext);
        assertNotNull(classes);
    }
}
