package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class GoogleTest extends BaseTest {

    @Test
    public void testSearch() {
        driver.get("https://example.com");

        Assert.assertTrue(driver.getTitle().contains("Example"));
    }
}