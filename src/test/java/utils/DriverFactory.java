package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public final class DriverFactory {

    private static final String GRID_URL_PROPERTY = "selenium.grid.url";
    private static final String GRID_URL_ENV = "SELENIUM_GRID_URL";
    private static final String DEFAULT_GRID_URL = "http://host.docker.internal:4444/wd/hub";

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        return new RemoteWebDriver(resolveGridUrl(), buildChromeOptions());
    }

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1920,1080"
        );
        return options;
    }

    private static URL resolveGridUrl() {
        String gridUrl = firstNonBlank(
                System.getProperty(GRID_URL_PROPERTY),
                System.getenv(GRID_URL_ENV),
                DEFAULT_GRID_URL
        );

        try {
            return new URL(gridUrl);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Selenium Grid URL: " + gridUrl, e);
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        throw new IllegalStateException("No Selenium Grid URL is configured");
    }
}
