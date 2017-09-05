package com.sug.core.log.test;

import com.sug.core.platform.log.LoggingEventProcessor;
import com.sug.core.platform.log.TraceLogger;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Greg.Chen on 2015/5/22.
 */
public class LogTest {

    Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void test() {
        TraceLogger.getInstance().initialize();
        logger.error("hello");
        logger.error("world");

        TraceLogger.getInstance().cleanup();
    }

    @Test
    public void pathTest(){
        LoggingEventProcessor processor = new LoggingEventProcessor(null,"E:/java_project/log");


        System.out.print(processor.generateLogFilePath());

    }
}
