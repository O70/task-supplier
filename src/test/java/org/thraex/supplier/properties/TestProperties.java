package org.thraex.supplier.properties;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 鬼王
 * @date 2019/09/23 16:18
 */
@Log4j2
public class TestProperties {

    private final static String CONFIG_LOCATION = "application.properties";

    @Test
    public void properties() {
        PropertiesUtil properties = PropertiesUtil.getProperties();
        log.info(properties);
        Properties systemProperties = PropertiesUtil.getSystemProperties();
        log.info(systemProperties);

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_LOCATION)) {
            Properties pro = new Properties();
            pro.load(inputStream);

            log.info(pro.getProperty("jdbc.url"));
            log.info(pro.getProperty("username"));
            log.info(pro.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
