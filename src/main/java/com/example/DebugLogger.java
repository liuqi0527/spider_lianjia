package com.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiuQi - [Created on 2018-01-11]
 */
public class DebugLogger {

    private static final Logger logger = LoggerFactory.getLogger("Debug");

    public static Logger getLogger() {
        return logger;
    }


    public static void error(Throwable throwable) {
        getLogger().error("", throwable);
    }

    public static void error(String msg, Object... params) {
        getLogger().error(joinLogMessage(msg, params));
    }

    public static void error(Throwable throwable, String msg, Object... params) {
        getLogger().error(joinLogMessage(msg, params), throwable);
    }


    public static void warn(Throwable throwable) {
        getLogger().warn("", throwable);
    }

    public static void warn(String msg, Object... params) {
        getLogger().warn(msg, params);
    }

    public static void warn(Throwable throwable, String msg, Object... params) {
        getLogger().info(joinLogMessage(msg, params), throwable);
    }


    public static void info(Throwable throwable) {
        getLogger().info("", throwable);
    }

    public static void info(String msg, Object... params) {
        getLogger().info(joinLogMessage(msg, params));
    }

    public static void info(Throwable throwable, String msg, Object... params) {
        getLogger().info(joinLogMessage(msg, params), throwable);
    }


    public static void debug(Throwable throwable) {
        getLogger().debug("", throwable);
    }

    public static void debug(String msg, Object... params) {
        getLogger().debug(joinLogMessage(msg, params));
    }

    public static void debug(Throwable throwable, String msg, Object... params) {
        getLogger().debug(joinLogMessage(msg, params), throwable);
    }


    public static void trace(Throwable throwable) {
        getLogger().trace("", throwable);
    }

    public static void trace(String msg, Object... params) {
        getLogger().trace(joinLogMessage(msg, params));
    }

    public static void trace(Throwable throwable, String msg, Object... params) {
        getLogger().trace(joinLogMessage(msg, params), throwable);
    }

    public static String joinLogMessage(String msg, Object[] params) {
        if (params == null || params.length <= 0) {
            return msg;
        }

        StringBuilder stringBuilder = new StringBuilder(msg);
        for (Object param : params) {
            stringBuilder.append("|").append(param);
        }
        return stringBuilder.toString();
    }

}
