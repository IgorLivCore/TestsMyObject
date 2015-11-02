package Base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.util.concurrent.TimeUnit;

import static Base.MOWebdriver.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;

/**
 * Created by natalia on 02.10.15.
 */
public class AdminTestBase extends TestBase {
    @BeforeMethod(alwaysRun = true)
    public void setUpMethod() throws Exception {
        legend="";
        operation="";
        emptyFields = null;
        emptyFieldsLegend = null;
        expected_result = true;
        if (!count_fields.isEmpty())
            count_fields.clear();
        if (!first_test){
            goTo("Компании");
            refresh();
        }
    }

    @BeforeTest(alwaysRun = true)
    public synchronized  void setUpTest() throws Exception {
        timeout=30;
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        //baseUrl = "http://myobject.ru/";
        //driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),DesiredCapabilities.firefox());

        registerWebdriver();

        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://admin.myobject");
        wait = new WebDriverWait(driver, 200);
        waitForElementPresent(By.xpath("//*[@class=\"login-form\"]"));
        input("Логин","admin@myobject.ru");
        input("Пароль", "test");
        button("Войти");

        waitForElementPresent(By.xpath("//div[@id=\"top-menu\"]"));

    }

    public static void goTo(String nameLink) throws Exception
    {
        for (int i=0;i<10;i++) {
            try {
                if (!MOWebdriver.isElementPresent(By.xpath("//div[@id=\"view-content\"]")))
                {
                    driver.findElement(By.xpath("(//a[contains(descendant-or-self::node(),\"" + nameLink + "\")])[last()]")).click();
                    return;
                }
                waitForElementPresent(By.xpath("(//a[contains(descendant-or-self::node(),\"" + nameLink + "\")])[last()]"));
                wait.until(elementToBeClickable(By.xpath("(//a[contains(descendant-or-self::node(),\"" + nameLink + "\")])[last()]")));
                driver.findElement(By.xpath("(//a[contains(descendant-or-self::node(),\"" + nameLink + "\")])[last()]")).click();
                return;
            }
            catch (ElementNotVisibleException e) {
            }
        }
    }
}
