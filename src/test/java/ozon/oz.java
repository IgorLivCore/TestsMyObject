package ozon;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import Listeners.ListenerThatHiglilightsElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import ru.stqa.selenium.factory.WebDriverFactory;


import java.util.concurrent.TimeUnit;

import static Base.MOWebdriver.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


import java.lang.String;
import java.lang.*;


/**
 * Created by igor on 01.11.16.
 */
public class oz {
    @Test(priority=1, groups = { "addIn" })
    public void testAddIn() throws Exception
    {
        DesiredCapabilities browser = DesiredCapabilities.firefox();
        browser.setCapability("browser.cache.disk.enable", false);
        browser.setCapability("browser.cache.memory.enable", false);
        browser.setCapability("browser.cache.offline.enable", false);
        browser.setCapability("network.http.use-cache", false);

        /****************1*********************/

        regularDriver = WebDriverFactory.getDriver(browser);
        driver = new EventFiringWebDriver(regularDriver);

        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://ozon.ru/");
        //driver.get("https://www.ozon.ru/catalog/1143476/?musedition=53620");
        wait = new WebDriverWait(driver, 200);


        //span[@class="eCatalogButton_TextWrap"]
        /***************2**********************/

        waitForPageLoad();
        //wait.until(visibilityOfElementLocated(By.xpath("//span[@class=\"eCatalogButton_TextWrap\"]"))).click();
        driver.findElement(By.xpath("//span[@class=\"eCatalogButton_TextWrap\"]")).click();
        driver.findElement(By.xpath("//li[@class=\"eMenuItem js_submenu_1060 mobile_div_music  mCommonItem\"]")).click();

        /***************3**********************/
        waitForPageLoad();
        driver.findElement(By.xpath("//li[@class=\"eSubMenu_ListItem\"]/a[text()=\"Коллекционные издания\"]")).click();

        /***************4*********************проверка?*/
//div[1][@class="bOneTile inline jsUpdateLink mRuble"]
//div[@class="eOneTile_ItemName"]
//div[1][@class="bOneTile inline jsUpdateLink mRuble"]/a[@class="jsUpdateLink eOneTile_link"]
//div[1][@class="bOneTile inline jsUpdateLink mRuble"]/a[@class="eOneTile_tileLink jsUpdateLink"]
        //div[@class="bOneTile inline jsUpdateLink mRuble"]/a[@class="eOneTile_tileLink jsUpdateLink"][1]

        /***************5**********************/
        waitForPageLoad();
        driver.findElement(By.xpath("//div[1][@class=\"bOneTile inline jsUpdateLink mRuble \"]/a[@class=\"eOneTile_tileLink jsUpdateLink\"]")).click();

        /***************6**********************/
        waitForPageLoad();
        String price = driver.findElement(By.xpath("//div[@class=\"bOzonPrice\"]/span[@class=\"eOzonPrice_main\"]")).getText();
        String name = driver.findElement(By.xpath("//div[@class=\"bDetailNameAndTags\"]/h1[@class=\"bItemName\"]")).getText();

        System.out.println(name);
        System.out.println(price);

        /***************7**********************/
        waitForPageLoad();
        driver.findElement(By.xpath("//div[text()=\"Добавить в корзину\"]")).click();

        /***************8**********************/
        waitForPageLoad();
        driver.findElement(By.xpath("//a[text()=\"Корзина\"]")).click();
        /**************************************/
        try{
            String name2 = driver.findElement(By.xpath("//a[@id=\"phCenter_ctl01_ctl00_ctl01_CartRepeater_ctl02_0_ItemNameHyperLink_0\"]")).getText();
            String price2 = driver.findElement(By.xpath("//div[@class=\"eCartBlock_eItemTotalCost\"]")).getText();

            if( name == name2 && price == price2 ) System.out.println("good"); else System.out.println("bad");
        }
        catch (Exception e){System.out.println("very bad");}

        /***************9**********************/
        waitForPageLoad();//hover
        driver.findElement(By.xpath("//label[@class=\"eCheckbox_eLabel jsItemCheckboxLabel\"]")).click();//при добавлении товаров>1 в корзину будет ошибка

        waitForPageLoad();//delete
        driver.findElement(By.xpath("//input[@id=\"phCenter_ctl01_ctl00_ctl01_CartRepeater_DeleteLinkButtonMulti\"]")).click();

        /***************10**********************/
        /*тут нужно обновить страницу*/
        //refresh();
        //driver.SwitchTo().Alert().Accept();
        //driver.findElement(By.xpath("//input[@id=\"phCenter_ctl01_ctl00_ctl01_CartRepeater_DeleteLinkButtonMulti\"]")).click();

        driver.get("https://www.ozon.ru/context/cart/");
        waitForPageLoad();

        try{/*если нет того элемента*/
            driver.findElement(By.xpath("//div[@class=\"eEmptyCartMessage_eTitle\"]"));//тут что то не то
            System.out.println("elementa net!");
        }
        catch (Exception e){ System.out.println("element est!"); }

        /***************11**********************/
        driver.quit();
    }
}
