package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Covers the 3-step checkout: information -> overview -> complete. */
public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstName;

    @FindBy(id = "last-name")
    private WebElement lastName;

    @FindBy(id = "postal-code")
    private WebElement postalCode;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillShippingInfo(String first, String last, String zip) {
        type(firstName, first, "First name");
        type(lastName, last, "Last name");
        type(postalCode, zip, "Postal code");
        click(continueButton, "Continue button");
        waitForUrlContains("checkout-step-two");
        return this;
    }

    public String getOrderTotalText() {
        return getText(totalLabel);
    }

    public CheckoutPage finishOrder() {
        click(finishButton, "Finish button");
        waitForUrlContains("checkout-complete");
        return this;
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader);
    }
}
