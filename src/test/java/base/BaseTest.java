package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.DriverFactory;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver currentDriver = driver;
        driver = null;

        if (currentDriver != null) {
            currentDriver.quit();
        }
    }

    protected WebDriver createDriver() {
        return DriverFactory.getDriver();
    }

    protected WebDriver driver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Ensure setup has run.");
        }
        return driver;
    }
}
