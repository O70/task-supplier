package org.thraex.supplier.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 鬼王
 * @date 2019/09/13 20:16
 */
public class TestLog4j2 {

    private static final Logger logger = LogManager.getLogger(TestLog4j2.class);

    public static void main(String[] args) {
        logger.fatal("fatal...");
        logger.error("error...");
        logger.warn("warn...");
        logger.info("info....");
        logger.debug("debug...");
        logger.trace("trace...");
    }

}
