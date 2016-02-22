package com.sainsburys.tu.appiumtests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        // set up appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("platformVersion", getCapabilityFromEnvVariable("APPIUM_PLATFORM_VERSION", "4.1.2"));
        capabilities.setCapability("app", new File(getCapabilityFromEnvVariable("APPIUM_APP_PATH", "../app/build/outputs/apk/app-debug.apk")));
        URL serverAddress = new URL(getCapabilityFromEnvVariable("APPIUM_SERVER_URL", "http://127.0.0.1:4723/wd/hub"));
        this.driver = new AndroidDriver(serverAddress, capabilities);
    }

    @After
    public void tearDown() throws Exception {
        this.driver.quit();
    }

    private String getCapabilityFromEnvVariable(String envVariable, String defaultValue) {
        String value = System.getenv(envVariable);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    protected WebElement findElement(By by) {
        return findElement(by, 20, TimeUnit.SECONDS);
    }

    protected WebElement findElement(By by, long timeout, TimeUnit unit) {
        new WebDriverWait(driver, 30).withTimeout(timeout, unit).until(ExpectedConditions.presenceOfElementLocated(by));
        return by.findElement(driver);
    }

    protected void assertElementExists(By by) {
        Assert.assertNotNull(findElement(by));
    }

    protected void assertElementDoesNotExist(By by, int seconds) {
        try {
            new WebDriverWait(driver, 30).withTimeout(seconds, TimeUnit.SECONDS).until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e) {
            return;
        }
        throw new RuntimeException("Element not wantet but found: " + by.toString());
    }
}