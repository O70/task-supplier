package org.thraex.supplier.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 鬼王
 * @date 2019/09/13 12:16
 */
public class Test {

    private static final Logger logger = LogManager.getLogger(Test.class);

    public static void main(String[] args) {
        //logger.error("Hello, World!");
        //logger.info("Hello, World!");
        //logger.debug("Hello, World!");
        //logger.warn("Hello, World!");
        //logger.trace("Hello, World!");

        logger.fatal("fatal...");
        logger.error("error...");
        logger.warn("warn...");
        logger.info("info....");
        logger.debug("debug...");
        logger.trace("trace...");

    }

}
