package org.mycoin.app;

import org.junit.Test;
import org.mycoin.app.MainApp;

import static org.junit.Assert.*;

public class MainAppTest {
	
    @Test
    public void testAppHasAGreeting() {
        MainApp classUnderTest = new MainApp();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
