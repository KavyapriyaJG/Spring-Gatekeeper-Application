package com.cdw.gatekeeper.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


/**
 * Class that holds Exceptions utilities
 */
public class ExceptionUtility {

    /**
     * Fetches property codes from the mentioned file for exception messages
     * @param code
     * @return
     * @throws IOException
     */
    public String getPropertyCode(String code) throws IOException {
        Properties props = PropertiesLoaderUtils.loadAllProperties("errorCodes.properties");
        return props.getProperty(code);
    }
}
