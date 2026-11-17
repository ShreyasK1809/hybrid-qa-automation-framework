package com.framework.base;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Parent of all UI test classes. Each test method gets its own browser (ThreadLocal in DriverFactory),
 * which is what makes parallel="methods" safe.
 * Browser precedence: testng.xml parameter -> -Dbrowser / env BROWSER -> config.properties.
 */
public abstract class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browserFromSuite) {
        String browser = browserFromSuite.isBlank() ? ConfigReader.get("browser") : browserFromSuite;
        boolean headless = ConfigReader.getBoolean("headless");
        DriverFactory.initDriver(browser, headless);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }
}
