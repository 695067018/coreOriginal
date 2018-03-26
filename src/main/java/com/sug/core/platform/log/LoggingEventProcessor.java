package com.sug.core.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.sug.core.util.CharacterEncodings;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

import java.io.*;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by A on 2015/5/22.
 */
public class LoggingEventProcessor {

    private static final int MAX_HOLD_SIZE = 5000;
    private final PatternLayout layout;
    private final String logFolder;

    private final Queue<ILoggingEvent> events = new ConcurrentLinkedDeque<>();
    private final AtomicInteger eventSize = new AtomicInteger(0);

    private volatile Writer writer;
    private final Lock lock = new ReentrantLock();
    private volatile boolean hold = true;

    public LoggingEventProcessor(PatternLayout layout, String logFolder) {
        this.layout = layout;
        this.logFolder = logFolder;
    }

    public void process(ILoggingEvent event) throws IOException{

        if (hold) {
            addEvent(event);
            if (flushLog(event)) {
                flushTraceLogs();
                hold = false;
            }
        } else {
            write(event);
        }
    }

    private void flushTraceLogs() throws IOException{
        try{
            lock.lock();
            if(writer == null)
                writer = createWriter();

            while(true){
                ILoggingEvent event = events.poll();
                if(event == null)
                    return;

                write(event);
            }

        }
        finally {
            lock.unlock();
        }

    }

    private void write(ILoggingEvent event) throws IOException {
        String log = layout.doLayout(event);
        writer.write(log);
    }

    private Writer createWriter() throws FileNotFoundException {
        String logFilePath = generateLogFilePath();
        File logFile = new File(logFilePath);
        createParentFolder(logFile);

        ActionLog actionLog = ActionLogger.get().currentActionLog();
        actionLog.setTraceLogPath(logFilePath);

        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), CharacterEncodings.CHARSET_UTF_8));
    }

    private void createParentFolder(File logFile){
        File folder = logFile.getParentFile();
        folder.mkdirs();
    }

    public String generateLogFilePath() {
        Date date = new Date();
        String sequence = RandomStringUtils.randomAlphanumeric(5);

        return String.format("%1$s/%2$tY/%2$tm/%2$td/%2$tH%2$tM.%3$s.log",
                logFolder, date, sequence);
    }

    private boolean flushLog(ILoggingEvent event) {

        long startTime = Long.parseLong(MDC.get("startTime"));
        if(System.currentTimeMillis() - startTime > 5000){
            return true;
        }

        return event.getLevel().isGreaterOrEqual(Level.WARN) || eventSize.get() > MAX_HOLD_SIZE;
    }

    private void addEvent(ILoggingEvent event) {
        event.getThreadName(); // force "logback" to remember current thread
        events.add(event);
        eventSize.getAndIncrement();
    }

    public void cleanup() throws IOException {


        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

}
