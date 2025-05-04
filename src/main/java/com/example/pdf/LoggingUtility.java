package com.example.pdf;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class LoggingUtility {

    private final String className;
    private final String methodName;
    private Instant start;

    private LoggingUtility(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    // Factory method: preferred entry point
    public static LoggingUtility start(String className, String methodName) {
        LoggingUtility log = new LoggingUtility(className, methodName);
        log.logStart();
        return log;
    }

    // Separate start call if you want to log manually later
    public void logStart() {
        this.start = Instant.now();
        log.info(String.format("[START] %s.%s", className, methodName));
    }

    public void logEnd() {
        if (start == null) {
            throw new IllegalStateException("Start time not set. Call logStart() or use start() factory method.");
        }
        long duration = Duration.between(start, Instant.now()).toMillis();
        log.info(String.format("[END] %s.%s (Duration: %d ms)", className, methodName, duration));
    }

    public void logError(Exception e) {
        if (start == null) {
            throw new IllegalStateException("Start time not set. Call logStart() or use start() factory method.");
        }
        long duration = Duration.between(start, Instant.now()).toMillis();
        log.error(String.format("[ERROR] %s.%s failed (Duration: %d ms): %s", className, methodName, duration, e.getMessage()));
    }
}
