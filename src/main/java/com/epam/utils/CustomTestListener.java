package com.epam.utils;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class CustomTestListener implements ITestListener {
    private static final Logger LOG = Logger.getLogger(CustomTestListener.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String repLog = String.format("The test %s started with method %s", iTestResult.getName(), getTestMethodName(iTestResult));
        logAndReport(repLog);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String repLog = String.format("The test %s, method %s  SUCCEED", iTestResult.getName(), getTestMethodName(iTestResult));
        logAndReport(repLog);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String repLog = String.format("The test %s, method %s FAILED", iTestResult.getName(), getTestMethodName(iTestResult));
           }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String repLog = String.format("The test %s, method %s SKIPPED", iTestResult.getName(), getTestMethodName(iTestResult));
        logAndReport(repLog);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        String repLog = String.format("The test %s method %s FAILED but it is in defined SUCCEES RATIO", iTestResult.getName(), getTestMethodName(iTestResult));
        logAndReport(repLog);
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        String repLog = String.format("The test starts with %s", iTestContext.getName());
        logAndReport(repLog);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        String repLog = String.format("The test finiched with %s", iTestContext.getName());
        logAndReport(repLog);
    }

    private void logAndReport(String repLog) {
        LOG.info(repLog);
        Reporter.log(repLog);
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
}
