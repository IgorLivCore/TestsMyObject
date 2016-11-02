package Base; /**
 * Created by natalia on 12.01.15.
 * TODO сведения об опо(формирует ли)
 * TODO сведения о ПК(формирует ли)
 * TODO роли+проверка ограничений
 * TODO пользователи+доступ только адинам
 * TODO после удаления сущностей сделать проверку в корзине
 *
 * TODO добавление через правое меню
 *
 * TODO админка
 */

import org.testng.annotations.*;
import org.openqa.selenium.*;
import java.lang.*;
import java.util.concurrent.*;
import org.openqa.selenium.support.ui.*;
import static Base.MOWebdriver.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.testng.Assert.fail;

public class MOTestBase extends TestBase{

    @BeforeMethod(alwaysRun = true)
    public void setUpMethod() throws Exception {
        legend="";
        operation="";
        emptyFields = null;
        emptyFieldsLegend = null;
        expected_result=true;
        isSelectByValue =1;
        if (!count_fields.isEmpty())
            count_fields.clear();
        if (!first_test){
            goTo("Эксплуатация","Объекты");
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
        //driver.manage().window().maximize();
        driver.get("http://localhost");
        wait = new WebDriverWait(driver, 200);
        goTo("Вход в myObject");
        //waitForElementPresent(By.xpath("//*/div[@class=\"modal fade authWindow in\"]"));//  //*[@id=\"authWindow\"]

        waitForElementPresent(By.xpath("//div [@class=\"modal fade authWindow in\"]"));


        driver.findElement(By.xpath("//input[@name=\"email\"]")).sendKeys("import@test.test");
        driver.findElement(By.xpath("//input[@name=\"passwd\"]")).sendKeys("1111");

        /*try {
            //input("Email","ww@ww.ww");
            //input("Пароль", "wwww");
            System.out.println("n1");
        }
        catch(Exception e){
            System.out.println("f1");
        }*/ //нужно доработать, выше просто заглушка


        button("Войти");


        waitForElementPresent(By.xpath("//div[@id=\"left-menu\"]"));

        wait.until(frameToBeAvailableAndSwitchToIt(By.xpath("//div[@class='zopim']/iframe")));

        //driver.switchTo().defaultContent();
        //driver.switchTo().frame(driver.findElements(By.tagName("iframe")).get(0));
        /*for(int i = 0; i < 10; i++)
        {
            try{
                driver.switchTo().defaultContent();
                wait.until(frameToBeAvailableAndSwitchToIt(By.xpath("//div[@class='zopim']/iframe")));
                driver.findElement(By.xpath("//div[contains(@title,'Закрыть')]")).click();
                break;
            }
            catch (StaleElementReferenceException | ElementNotVisibleException e) {}
        }*/
        driver.switchTo().defaultContent();
    }

    public static void goTo(String nameLink) throws Exception
    {
        waitForPageLoad();
        for (int i=0;i<10;i++) {
            try {
                if (!MOWebdriver.isElementPresent(By.xpath("//div[@id=\"view-content\"]")))
                {
                    driver.findElement(By.xpath("//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]")).click();
                    return;
                }
                waitForElementPresent(By.xpath("//div[@id=\"view-content\"]//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]"));
                wait.until(elementToBeClickable(By.xpath("//div[@id=\"view-content\"]//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]")));
                driver.findElement(By.xpath("//div[@id=\"view-content\"]//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]")).click();
                return;
            }
            catch (ElementNotVisibleException | StaleElementReferenceException e) {
            }
        }
    }

    public static void goTo(String groupHeader, String nameLink) throws Exception
    {
        waitForPageLoad();
        for (int i=0;i<10;i++) {
            try {
                //waitForElementPresent(By.xpath("//div[@id=\"left-menu\"]//li[@class=\"group-header\" and contains(text(),\"" + groupHeader + "\")]/following-sibling::node()//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]"));
                wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"left-menu\"]//li[@class=\"group-header\" and contains(text(),\"" + groupHeader + "\")]/following-sibling::node()//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]")));
                driver.findElement(By.xpath("//div[@id=\"left-menu\"]//li[@class=\"group-header\" and contains(text(),\"" + groupHeader + "\")]/following-sibling::node()//a[contains(descendant-or-self::node(),\"" + nameLink + "\")]")).click();
                waitForPageLoad();
                return;
            }
            catch (ElementNotVisibleException e)
            {   }
        }
    }

    public static boolean isListEmpty(String[] lists){
        try{
            for(String list:lists)
            {
                switch (list){
                    case "Должности":
                        goTo("Общее", "Персонал");
                    case "Персонал":
                        goTo("Общее", list);
                        break;
                    case "Структур. подразделения":
                        goTo("Общее", "Предприятие");
                    case "Сводная информция":
                    case "Планы":
                    case "Предприятие":
                        goTo("Общее", list);
                        break;
                    case "Объекты":
                    case "ТУ":
                    case "ЗиС":
                        goTo("Эксплуатация", list);
                        break;
                    case "Предписания РТН":
                    case "Протоколы ВП":
                        goTo("Производственный контроль", list);
                        break;
                    case "Аварии":
                    case "Инциденты":
                    case "Утрата ВМ":
                    case "Несчастные случаи":
                        goTo("Инциденты", list);
                        break;
                    case "Типы документов":
                        goTo("Справочники", "Нормативные документы");
                    case "Нормативные документы":
                        goTo("Справочники", list);
                        break;
                    case "Роли":
                        goTo("Прочее", "Администрирование");
                    case "Администрирование":
                    case "Корзина":
                        goTo("Прочее", list);
                        break;
                    case "Мероприятия":
                        goTo("Общее", "Предприятие");
                        goTo("Документы");
                        selectRow(0);
                        goTo("Мероприятия");
                        break;
                    case "Документы":
                        goTo("Общее", "Предприятие");
                    default:
                        goTo(list);
                        break;
                }

                if (MOWebdriver.isListEmpty())
                    return true;
            }
            return false;
        }
        catch (Exception e)
        {
            fail("При проверке наличия данных в списке возникла ошибка:\n"
                    + e.getMessage());
            return false;
        }
    }
}
