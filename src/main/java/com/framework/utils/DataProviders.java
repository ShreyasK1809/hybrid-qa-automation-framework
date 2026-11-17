package com.framework.utils;

import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;

/** All TestNG data providers in one place. parallel=true lets rows run on separate threads. */
public final class DataProviders {

    private DataProviders() { }

    @DataProvider(name = "invalidLoginData", parallel = true)
    public static Object[][] invalidLoginData() {
        return toArray(ExcelUtils.readSheet("testdata/LoginData.xlsx", "InvalidLogin"));
    }

    @DataProvider(name = "checkoutData", parallel = true)
    public static Object[][] checkoutData() {
        return toArray(JsonUtils.readList("testdata/checkoutData.json"));
    }

    private static Object[][] toArray(List<Map<String, String>> rows) {
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }
}
