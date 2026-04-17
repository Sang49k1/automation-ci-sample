package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.DriverFactory;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}