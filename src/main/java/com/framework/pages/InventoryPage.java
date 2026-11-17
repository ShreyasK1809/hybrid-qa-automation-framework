package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InventoryPage extends BasePage {

    /** Dynamic locator template: the product name is injected at runtime, so no per-product @FindBy is needed. */
    private static final String ADD_BUTTON_FOR_PRODUCT =
            "//div[@data-test='inventory-item-name' and normalize-space()='%s']"
                    + "/ancestor::div[@class='inventory_item']//button";
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public InventoryPage addProductToCart(String productName) {
        int before = getCartCount();
        By addButton = By.xpath(String.format(ADD_BUTTON_FOR_PRODUCT, productName));
        log.info("Adding product to cart: {}", productName);
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        // Deterministic sync: wait for the badge to reflect the change instead of using Thread.sleep
        wait.until(d -> getCartCount() > before);
        return this;
    }

    public int getCartCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);   // badge is absent when cart is empty
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public CartPage goToCart() {
        click(cartLink, "Cart icon");
        waitForUrlContains("cart");
        return new CartPage(driver);
    }
}
