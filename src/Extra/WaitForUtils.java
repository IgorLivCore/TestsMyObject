import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class WaitForUtils {

    int timeoutSeconds = 30000;
    By locator;
    WebDriverWait wait;
    WebDriver driver;

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public By getLocator() {
        return locator;
    }

    public void setLocator(By locator) {
        this.locator = locator;
    }

    public WaitForUtils(WebDriver driver){
        this.wait = new WebDriverWait(driver,this.timeoutSeconds);
        this.driver = driver;
    }

    public WaitForUtils(WebDriver driver, int timeoutSeconds){
        this.wait = new WebDriverWait(driver,timeoutSeconds);
    }

    Function<webdriver, webelement=""> presenceOfElementLocated(final By locator) {
        return new Function<webdriver, webelement="">() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    public void waitForElementPresent(final By locator){
        this.wait.until(presenceOfElementLocated(locator));
    }

    public void waitForElementPresent(final By locator, long seconds){
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis()-startTime<seconds*1000)
        {
            30="" default="" driver="" exception="" int="" is="" new="" out:="" param="" pre="" public="" return="" seconds="" static="" time="" timeout="" wait="" waitforutils="" webdriver=""><p> </p>
            </seconds*1000){></webdriver,></webdriver,>