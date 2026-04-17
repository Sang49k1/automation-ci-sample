package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class DriverFactory {

    public static WebDriver getDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("window-size=1920,1080");

            return new RemoteWebDriver(
                    new URL("http://host.docker.internal:4444/wd/hub"),
                    options
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}