package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item .inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return itemNames.stream().map(e -> e.getText().trim()).toList();
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton, "Checkout button");
        waitForUrlContains("checkout-step-one");
        return new CheckoutPage(driver);
    }
}
