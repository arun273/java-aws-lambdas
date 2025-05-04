package com.example.pdf;

public class CallerInfoUtility {

    // Returns the simple class name (e.g., UserService)
    public static String getCallingClassName() {
        return getCaller().getClassName().substring(getCaller().getClassName().lastIndexOf('.') + 1);
    }

    // Returns the method name
    public static String getCallingMethodName() {
        return getCaller().getMethodName();
    }

    // Internal helper
    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // stack[0] = getStackTrace, stack[1] = getCaller(), stack[2] = public method, stack[3] = real caller
        return stackTrace[3];
    }
}
