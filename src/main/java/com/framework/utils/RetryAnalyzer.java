package com.framework.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Retries a failed test once to absorb environment flakiness (network blips, slow CI browsers). */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY = 1;
    // Keyed per method + data row, because TestNG shares one analyzer instance across data-provider rows
    private static final Map<String, Integer> ATTEMPTS = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        String key = result.getMethod().getQualifiedName() + Arrays.toString(result.getParameters());
        int used = ATTEMPTS.merge(key, 1, Integer::sum);
        return used <= MAX_RETRY;
    }
}
