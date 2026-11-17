package com.framework.tests.ui;

import com.framework.base.BaseTest;
import com.framework.config.ConfigReader;
import com.framework.pages.CartPage;
import com.framework.pages.CheckoutPage;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import com.framework.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CheckoutTest extends BaseTest {

    private static final String PRODUCT_1 = "Sauce Labs Backpack";
    private static final String PRODUCT_2 = "Sauce Labs Bike Light";

    // Data-driven from JSON: shipping details per run
    @Test(groups = {"smoke", "regression"}, dataProvider = "checkoutData", dataProviderClass = DataProviders.class,
            description = "E2E: login -> add 2 products -> verify cart -> checkout -> order confirmation")
    public void endToEndCheckout_completesOrder(Map<String, String> data) {
        // 1. Login
        InventoryPage inventory = new LoginPage(driver()).open()
                .loginAs(ConfigReader.get("app.username"), ConfigReader.get("app.password"));
        Assert.assertEquals(inventory.getTitle(), "Products");

        // 2. Add products
        inventory.addProductToCart(PRODUCT_1).addProductToCart(PRODUCT_2);
        Assert.assertEquals(inventory.getCartCount(), 2, "Cart badge should show 2 items");

        // 3. Verify cart contents
        CartPage cart = inventory.goToCart();
        List<String> items = cart.getItemNames();
        Assert.assertEquals(items.size(), 2, "Unexpected number of items in cart");
        Assert.assertTrue(items.contains(PRODUCT_1) && items.contains(PRODUCT_2),
                "Cart is missing selected products: " + items);

        // 4. Checkout
        CheckoutPage checkout = cart.proceedToCheckout()
                .fillShippingInfo(data.get("firstName"), data.get("lastName"), data.get("postalCode"));
        Assert.assertTrue(checkout.getOrderTotalText().startsWith("Total: $"), "Order total not displayed");

        checkout.finishOrder();
        Assert.assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!",
                "Order confirmation message mismatch");
    }
}
