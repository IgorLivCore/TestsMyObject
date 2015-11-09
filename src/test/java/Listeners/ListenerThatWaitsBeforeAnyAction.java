package Listeners;

/**
 * Created by natalia on 16.01.15.
 */
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

@Augmentable
public class ListenerThatWaitsBeforeAnyAction extends AbstractWebDriverEventListener {

    private final long timeout;
    private final WebDriver driver;

    public ListenerThatWaitsBeforeAnyAction(WebDriver driver, long timeout, TimeUnit unit) {
        this.timeout = TimeUnit.MILLISECONDS.convert(Math.max(0, timeout), unit);
        this.driver = driver;
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        sleep();
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        sleep();
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        sleep();
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        sleep();
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        sleep();
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(timeout);
            driver.switchTo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}