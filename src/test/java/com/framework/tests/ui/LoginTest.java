package com.framework.tests.ui;

import com.framework.base.BaseTest;
import com.framework.config.ConfigReader;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import com.framework.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Valid user lands on the Products page")
    public void validLogin_landsOnInventoryPage() {
        InventoryPage inventory = new LoginPage(driver()).open()
                .loginAs(ConfigReader.get("app.username"), ConfigReader.get("app.password"));

        Assert.assertEquals(inventory.getTitle(), "Products", "Products header mismatch after login");
        Assert.assertTrue(driver().getCurrentUrl().contains("inventory"), "User was not redirected to inventory");
    }

    // Data-driven from Excel: each row = one negative scenario
    @Test(groups = {"regression"}, dataProvider = "invalidLoginData",
            dataProviderClass = DataProviders.class, description = "Invalid logins show the right error")
    public void invalidLogin_showsExpectedError(Map<String, String> data) {
        LoginPage login = new LoginPage(driver()).open()
                .loginExpectingFailure(data.get("username"), data.get("password"));

        Assert.assertEquals(login.getErrorMessage(), data.get("expectedError"),
                "Wrong error for scenario: " + data.get("testCase"));
    }
}
