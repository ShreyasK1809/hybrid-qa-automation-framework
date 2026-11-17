package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        return this;
    }

    /** Happy path: returns the next page object (fluent navigation). */
    public InventoryPage loginAs(String username, String password) {
        submitCredentials(username, password);
        waitForUrlContains("inventory");
        return new InventoryPage(driver);
    }

    /** Negative path: stays on the login page. */
    public LoginPage loginExpectingFailure(String username, String password) {
        submitCredentials(username, password);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    private void submitCredentials(String username, String password) {
        type(usernameInput, username, "Username field");
        type(passwordInput, password, "Password field");
        click(loginButton, "Login button");
    }
}
