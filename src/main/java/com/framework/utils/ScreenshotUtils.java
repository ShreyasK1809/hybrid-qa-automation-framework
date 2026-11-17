package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public final class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() { }

    /**
     * Saves a PNG under target/screenshots and returns it Base64-encoded so it can be embedded
     * in the Extent HTML (self-contained report, easy to download as a CI artifact).
     */
    public static String capture(WebDriver driver, String testName) {
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        try {
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            Path file = Paths.get("target", "screenshots", safeName + "_" + ts + ".png");
            Files.createDirectories(file.getParent());
            Files.write(file, png);
            log.info("Screenshot saved: {}", file);
        } catch (IOException e) {
            log.warn("Could not save screenshot to disk: {}", e.getMessage());
        }
        return Base64.getEncoder().encodeToString(png);
    }
}
