package tests;

import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleTest extends BaseTest {

    private static final String PAGE_URL = "https://example.com";
    private static final String EXPECTED_TITLE_TEXT = "Example";

    @Test
    public void shouldOpenExamplePage() {
        driver().get(PAGE_URL);

        Assert.assertTrue(
                driver().getTitle().contains(EXPECTED_TITLE_TEXT),
                "Page title should contain expected text sangnt5"
        );
    }
}
