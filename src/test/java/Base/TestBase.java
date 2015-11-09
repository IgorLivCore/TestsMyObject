package Base;

import Listeners.ListenerThatWaitsBeforeAnyAction;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import ru.stqa.selenium.factory.WebDriverFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.driver;

/**
 * Created by natalia on 02.10.15.
 */
public abstract class TestBase {
    protected static boolean first_test;
    @BeforeMethod(alwaysRun = true) public abstract void setUpMethod() throws Exception;
    @BeforeTest(alwaysRun = true) public abstract void setUpTest() throws Exception;

    protected void registerWebdriver(){
        DesiredCapabilities browser = DesiredCapabilities.chrome();
        browser.setCapability("browser.cache.disk.enable", false);
        browser.setCapability("browser.cache.memory.enable", false);
        browser.setCapability("browser.cache.offline.enable", false);
        browser.setCapability("network.http.use-cache", false);

        regularDriver = WebDriverFactory.getDriver(browser);
        driver = new EventFiringWebDriver(regularDriver);
        driver.register(new ListenerThatWaitsBeforeAnyAction(driver,2000, TimeUnit.MILLISECONDS));//для замедления
        //driver.register(new ListenerThatHiglilightsElements(1,500, TimeUnit.MILLISECONDS));//для замедления и подсветки
    }

    @BeforeTest
    public void setTestBase(){
        timeout=30;
        legend="";
        operation="";
        first_test=true;
        count_fields = new HashMap<>();
        registerWebdriver();

        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    @AfterTest(alwaysRun = true)
    public void tearDownTest() throws Exception {
        WebDriverFactory.dismissAll();
        if (driver != null)
            driver.quit();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod() throws Exception {
        first_test=false;
    }

    public void filterList(String fieldNameBy, String fieldType, String value) throws Exception
    {
        try{
            if (driver.findElement(By.xpath("//div[@class=\"grid-filters\"]"))!=null)
                driver.findElement(By.xpath("//button[contains(@title,\"Фильтрация\")]")).click();//кликаем по кнопке фильтрации
        }
        catch (Exception e){
        }
        button("Сбросить всё");
        Class[] paramTypes = new Class[] { String.class, String.class };
        Method method =MOWebdriver.class.getMethod(fieldType, paramTypes);
        Object[] args = new Object[] { fieldNameBy, value };
        method.invoke(MOWebdriver.class, args);
        button("Применить");
    }
}
