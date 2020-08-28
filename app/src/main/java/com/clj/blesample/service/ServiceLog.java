package com.clj.blesample.service;

import android.util.Log;

import com.clj.blesample.BuildConfig;

/**
 * 日志封装
 */
public class ServiceLog {

    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    /**
     * 判断是否可以调试
     *
     * @return
     */
    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(ServiceErrorCode message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName).append("(): ").append(lineNumber).append(" - ");
        buffer.append(message.toString());
        return buffer.toString();
    }

    private static String createLog(String message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName).append("(): ").append(lineNumber).append(" - ");
        buffer.append(message);
        return buffer.toString();
    }

    /**
     * 获取文件名、方法名、所在行数
     *
     * @param sElements
     */
    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(ServiceErrorCode message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(ServiceErrorCode message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(ServiceErrorCode message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(ServiceErrorCode message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(ServiceErrorCode message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void e(String format, Object... args) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(String.format(format, args)));
    }

    public static void i(String format, Object... args) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(String.format(format, args)));
    }

    public static void d(String format, Object... args) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(String.format(format, args)));
    }

    public static void v(String format, Object... args) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(String.format(format, args)));
    }

    public static void w(String format, Object... args) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(String.format(format, args)));
    }
}
