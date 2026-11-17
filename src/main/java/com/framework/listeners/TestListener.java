package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.base.DriverFactory;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * Bridges TestNG events to ExtentReports + Log4j2 and captures a screenshot on UI failures.
 * TestNG calls onTestFailure BEFORE @AfterMethod, so the browser is still open when we screenshot.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private final ExtentReports extent = ExtentManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        String name = displayName(result);
        ExtentTest test = extent.createTest(name, result.getMethod().getDescription());
        test.assignCategory(result.getMethod().getGroups());
        ExtentTestManager.set(test);
        log.info("========== START: {} ==========", name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.get().pass("Test passed");
        log.info("PASSED: {}", displayName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.get();
        test.fail(result.getThrowable());
        log.error("FAILED: {}", displayName(result), result.getThrowable());

        WebDriver driver = DriverFactory.getDriver();    // null for API tests -> no screenshot
        if (driver != null) {
            try {
                String base64 = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
                test.fail("Screenshot at point of failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Failure screenshot").build());
            } catch (Exception e) {
                log.warn("Screenshot capture failed: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentTestManager.get();
        if (test != null) {
            test.skip(result.getThrowable() != null ? result.getThrowable().toString() : "Skipped / retried");
        }
        log.warn("SKIPPED (or retried): {}", displayName(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        ExtentTestManager.remove();
        log.info("Report flushed for <test> '{}'", context.getName());
    }

    private String displayName(ITestResult result) {
        String name = result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            String p = Arrays.toString(params);
            name += " " + (p.length() > 90 ? p.substring(0, 90) + "..." : p);
        }
        return name;
    }
}
