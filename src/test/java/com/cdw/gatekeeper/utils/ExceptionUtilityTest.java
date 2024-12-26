package com.cdw.gatekeeper.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Suite that holds test cases for Exception utilities
 */
public class ExceptionUtilityTest {

    private ExceptionUtility exceptionUtility;

    @BeforeEach
    void setUp() {
        exceptionUtility = new ExceptionUtility();
    }

    @Test
    void testGetPropertyCode_Success() throws IOException {
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty("GK003")).thenReturn("That Respective Error Code from the property file");

        try (MockedStatic<PropertiesLoaderUtils> mocked = mockStatic(PropertiesLoaderUtils.class)) {
            mocked.when(() -> PropertiesLoaderUtils.loadAllProperties("errorCodes.properties")).thenReturn(mockProperties);

            String result = exceptionUtility.getPropertyCode("GK003");

            assertEquals("That Respective Error Code from the property file", result);
        }
    }

    @Test
    void testGetPropertyCode_PropertyNotFound() throws IOException {
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty("GK999")).thenReturn(null);

        try (MockedStatic<PropertiesLoaderUtils> mocked = mockStatic(PropertiesLoaderUtils.class)) {
            mocked.when(() -> PropertiesLoaderUtils.loadAllProperties("errorCodes.properties")).thenReturn(mockProperties);

            String result = exceptionUtility.getPropertyCode("GK999");

            assertNull(result);
        }
    }

    @Test
    void testGetPropertyCode_IOException() throws IOException {
        try (MockedStatic<PropertiesLoaderUtils> mocked = mockStatic(PropertiesLoaderUtils.class)) {
            mocked.when(() -> PropertiesLoaderUtils.loadAllProperties("errorCodes.properties"))
                    .thenThrow(new IOException("Error loading properties"));

            assertThrows(IOException.class, () -> exceptionUtility.getPropertyCode("GK003"));
        }
    }
}
