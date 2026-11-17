package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.config.ConfigReader;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Thread-safe singleton that builds the one ExtentReports instance for the whole run. */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() { }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/ExtentReport_" + ts + ".html");
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Automation Execution Report");
            spark.config().setReportName("Hybrid QA Framework - UI + API");
            spark.config().setEncoding("utf-8");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Browser", ConfigReader.get("browser", "chrome"));
            extent.setSystemInfo("Headless", ConfigReader.get("headless", "false"));
            extent.setSystemInfo("UI Base URL", ConfigReader.get("base.url", ""));
            extent.setSystemInfo("API Base URL", ConfigReader.get("api.base.url", ""));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }
}
