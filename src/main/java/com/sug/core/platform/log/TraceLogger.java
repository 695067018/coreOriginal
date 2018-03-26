package com.sug.core.platform.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by A on 2015/5/22.
 */
public class TraceLogger {

    private static  final TraceLogger logger = new TraceLogger();

    private String logFolder;
    private PatternLayout layout;
    private final ConcurrentMap<Long, LoggingEventProcessor> processors = new ConcurrentHashMap<>();

    public static TraceLogger getInstance(){
        return logger;
    }



    public void process(ILoggingEvent event) throws IOException {
        long threadId = getTargetThreadId();
        LoggingEventProcessor processor = processors.get(threadId);

        if(processor != null){
            processor.process(event);
        }
    }

    public void initialize() {
        long threadId = Thread.currentThread().getId();
        processors.put(threadId, new LoggingEventProcessor(layout, logFolder));
    }

    private long getTargetThreadId() {

        return Thread.currentThread().getId();
    }

    public void cleanup() {
        try {
            long threadId = Thread.currentThread().getId();
            LoggingEventProcessor processor = processors.remove(threadId);
            processor.cleanup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLogFolder() {
        return logFolder;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }

    public PatternLayout getLayout() {
        return layout;
    }

    public void setLayout(PatternLayout layout) {
        this.layout = layout;
    }

}
