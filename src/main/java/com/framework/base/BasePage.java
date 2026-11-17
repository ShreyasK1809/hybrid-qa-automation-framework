package com.framework.base;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Parent of every Page Object. Initialises Page Factory and wraps raw Selenium calls
 * with explicit waits, stale-element tolerance and logging, so page classes stay declarative.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger log = LogManager.getLogger(getClass());

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getInt("explicit.wait", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        this.wait.ignoring(StaleElementReferenceException.class);
        // Ajax locator re-polls each @FindBy lookup, and proxies re-locate on every call -> far fewer stale errors.
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, timeout), this);
    }

    protected void click(WebElement element, String description) {
        log.info("Click -> {}", description);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Click intercepted on '{}', falling back to JS click", description);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(WebElement element, String text, String description) {
        log.info("Type '{}' -> {}", description.toLowerCase().contains("password") ? "****" : text, description);
        WebElement el = wait.until(ExpectedConditions.visibilityOf(element));
        el.clear();
        if (text != null && !text.isEmpty()) {
            el.sendKeys(text);
        }
    }

    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }
}
