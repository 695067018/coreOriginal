package com.sug.core.platform.log;

import com.sug.core.util.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Greg.chen on 2016-03-15.
 */
public class ActionLogger {
    private final Logger logger = LoggerFactory.getLogger(ActionLogger.class);

    private static final String LOG_SPLITTER = " | ";

    private static final ActionLogger INSTANCE = new ActionLogger();

    private final ConcurrentMap<Long, ActionLog> logs = new ConcurrentHashMap<>();

    public static ActionLogger get() {
        return INSTANCE;
    }

    public void initialize() {
        long threadId = Thread.currentThread().getId();
        logs.put(threadId, new ActionLog());
    }

    private long getTargetThreadId() {
        String targetThreadId = MDC.get(LogConstants.MDC_TARGET_THREAD_ID);
        return Convert.toLong(targetThreadId, Thread.currentThread().getId());
    }

    public ActionLog currentActionLog() {
        return logs.get(getTargetThreadId());
    }

    public void save() {
        long threadId = Thread.currentThread().getId();
        ActionLog actionLog = logs.remove(threadId);
        actionLog.setElapsedTime(System.currentTimeMillis() - actionLog.getRequestDate().getTime());
        logger.info(buildActionLogText(actionLog));
    }

    private String buildActionLogText(ActionLog actionLog) {
        StringBuilder builder = new StringBuilder();
        builder.append(actionLog.getResult())
                .append(LOG_SPLITTER)
                .append(actionLog.getRequestId())
                .append(LOG_SPLITTER)
                .append(actionLog.getElapsedTime())
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getClientIP()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getRequestURI()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPMethod()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPStatusCode()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getException()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getErrorMessage()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getTraceLogPath()));


        return builder.toString();
    }

    private String buildLogField(Object field) {
        return field == null ? "" : String.valueOf(field);
    }
}
