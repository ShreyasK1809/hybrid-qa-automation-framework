package com.framework.listeners;

import com.aventstack.extentreports.ExtentTest;

/** ThreadLocal holder so parallel threads each write to their own ExtentTest node. */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentTestManager() { }

    public static void set(ExtentTest test) { TEST.set(test); }

    public static ExtentTest get() { return TEST.get(); }

    public static void remove() { TEST.remove(); }
}
