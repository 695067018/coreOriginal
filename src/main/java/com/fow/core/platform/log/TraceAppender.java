package com.fow.core.platform.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * Created by Greg.Chen on 2015/5/22.
 */
public class TraceAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private String logFolder;
    private PatternLayout layout;


    @Override
    public void start() {
        TraceLogger logger = TraceLogger.getInstance();
        logger.setLayout(layout);
        logger.setLogFolder(logFolder);
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            TraceLogger.getInstance().process(event);
        } catch (Exception e) {
            addError("failed to write log", e);
        }
    }

    @Override
    public void stop() {

        super.stop();
    }


    // set on logback.xml
    public void setLogFolder(String logFolder) {

        this.logFolder = logFolder;
    }

    // set on logback.xml
    public void setLayout(PatternLayout layout) {

        this.layout = layout;
    }
}
