package com.framework.base;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Owns the WebDriver lifecycle. One driver per thread via ThreadLocal, so
 * parallel TestNG threads never share (and corrupt) a browser session.
 * Selenium Manager (built into Selenium 4.6+) downloads the matching driver binary automatically.
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() { }

    public static void initDriver(String browser, boolean headless) {
        WebDriver driver = switch (browser.toLowerCase()) {
            case "chrome"  -> createChrome(headless);
            case "firefox" -> createFirefox(headless);
            case "edge"    -> createEdge(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        // Implicit wait deliberately left at 0: mixing implicit + explicit waits causes unpredictable delays.
        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
        log.info("Started {} (headless={}) on thread {}", browser, headless, Thread.currentThread().getName());
    }

    /** May return null when no browser was started (e.g. API tests) - callers such as the listener rely on that. */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();   // essential: pooled threads are reused, avoid stale/leaked references
            }
            log.info("Browser closed on thread {}", Thread.currentThread().getName());
        }
    }

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions o = new ChromeOptions();
        o.addArguments("--window-size=1920,1080", "--disable-notifications",
                "--remote-allow-origins=*", "--no-sandbox", "--disable-dev-shm-usage");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);          // kills "save password" bubble
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false); // kills "password breach" popup on SauceDemo
        o.setExperimentalOption("prefs", prefs);
        if (headless) {
            o.addArguments("--headless=new");
        }
        return new ChromeDriver(o);
    }

    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions o = new FirefoxOptions();
        if (headless) {
            o.addArguments("-headless");
        }
        return new FirefoxDriver(o);
    }

    private static WebDriver createEdge(boolean headless) {
        EdgeOptions o = new EdgeOptions();
        o.addArguments("--window-size=1920,1080");
        if (headless) {
            o.addArguments("--headless=new");
        }
        return new EdgeDriver(o);
    }
}
