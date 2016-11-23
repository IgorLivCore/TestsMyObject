package Base;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.testng.Assert.fail;

/**
 * Created by natalia on 18.05.15.
 */
public abstract class MOWebdriver {
    public static WebDriver regularDriver;
    public static EventFiringWebDriver driver;
    public static WebDriverWait wait;
    protected static String parent="/";
    protected static String field="";
    public static String data;
    public static String data1;
    public static String data2;
    protected static int isSelectByValue;
    public static String operation;
    public static boolean expected_result=true;//true - позитивный тест-кейс, false - негативный тест-кейс
    public static ArrayList<String> emptyFields = null;
    public static ArrayList<String> emptyFieldsLegend = null;
    public static HashMap<String,Integer> count_fields;
    private static String[] monthNames= {"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    public static int timeout=30;
    public static String legend;
    protected static String active_view="workspace";
    protected static List<WebElement> elements;
    private static String additionalString;


    public abstract void goTo(String groupHeader, String nameLink) throws Exception;
    /*            ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ SELENIUM     */

    /*
    Устанавливаем родителя для поиска элементов по элементу legend
     */
    protected static boolean setParent(){
        if (isElementPresent(By.id("myModal"))||isElementPresent(By.className("modal-content")))//modal?
            active_view = "modal-content";
        else
            if (isElementPresent(By.id("right-view"))||isElementPresent(By.className("right-view")))
                active_view = "right-view";
            else
                if (isElementPresent(By.id("workspace"))||isElementPresent(By.className("workspace")))
                    active_view = "workspace";
                else active_view = "";
        if(!legend.equals("")&&!active_view.equals("")){
            //wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]")));
            if (isElementPresent(By.xpath("//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]/following-sibling::*[1]"))
                    ||isElementPresent(By.xpath("//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]/following-sibling::*[2]")))
                parent="//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]";
            else
                if (isElementPresent(By.xpath("//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]/../following-sibling::*[1]")))
                    parent="//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]/..";
                else
                    parent="//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]//node()[contains(text(),\"" + legend + "\")]";
            return true;
        }
        else{
            if (!active_view.equals(""))
                parent="//div[@id=\"" + active_view + "\" or @class=\"" + active_view + "\"]/";
            else parent="/";
            return false;
        }
    }

    /*
    Заполнение поля даты
     */
    public static void setDate(WebElement element, String date){
        String tempDate[]=date.split("\\.");
        element.click();
        new Select(element.findElement(By.xpath("parent::*//select[contains(@class,\"myobject-datepicker-year\")]"))).selectByVisibleText(tempDate[2]);//год
        new Select(element.findElement(By.xpath("parent::*//select[contains(@class,\"myobject-datepicker-month\")]"))).selectByVisibleText(monthNames[Integer.parseInt(tempDate[1]) - 1]);//месяц
        element.findElement(By.xpath("parent::*//table[@class=\"myobject-datepicker-calendar\"]//td[text()=\"" + (tempDate[0].charAt(0)=='0'?tempDate[0].replaceAll("0",""):tempDate[0]) + "\"]")).click();//число
    }

    /*
    Ждать появления элемента
     */
    public static void waitForElementPresent(By by) {
        waitForPageLoad();
        try{
            wait.until(visibilityOfElementLocated(by));
        }
        catch (Exception e)
        {
            if(by.equals(By.xpath("/html/body/ul/li/div")))
                fail("Нет сообщения о результате операции!");
            if (by.equals(By.xpath("//div[@id=\"workspace\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[@class]")))
                fail("Список пуст!");
            else
                fail("Время ожидания элемента {" + by.toString() + "} истекло.");
            System.out.println("InterruptedException");
        }
    }

    /*
    Ждать загрузку страницы приложения - отсутствие прелоадера
    */
    public static void waitForPageLoad(){
        int second;

        boolean isPageLoaded=false;
        List<WebElement> elements;
        if(!isElementPresent(By.xpath("//*[contains(@id,\"preloader\") or contains(@class,\"preloader\")]"))){
            return;
        }
        else {
            if(!isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//button[contains(@class,\"refresh\")]/span"))&&
                    isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//button[contains(@class,\"refresh\")]/img[contains(@class,\"preloader\")]")))
                wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"" + active_view + "\"]//button[contains(@class,\"refresh\")]/span")));
            for (second = 0; second < timeout; second++) {
                if (isPageLoaded)
                    return;
                elements = driver.findElements(By.xpath("//*[contains(@id,\"preloader\") or contains(@class,\"preloader\")]"));

                if (elements.size() == 0)
                    return;
                for (WebElement preloader : elements) {
                    try {
                        if (isElementPresent(preloader) && preloader.isDisplayed()) {
                            try {
                                isPageLoaded = false;
                                TimeUnit.SECONDS.sleep(1);
                            } catch (Exception e) {
                                fail("Ошибка при ожидании загрузки страницы");
                            }
                            break;
                        } else
                            isPageLoaded = true;
                    } catch (StaleElementReferenceException e) {
                        break;
                    }
                }
            }
        }
        if(!isPageLoaded) fail("Время ожидания загрузки страницы истекло.");
    }

    protected static boolean isElementPresent(By by) {
        boolean result=true;

        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try{
            if (driver.findElement(by)==null||!driver.findElement(by).isDisplayed())
                result = false;
        }
        catch(NoSuchElementException | StaleElementReferenceException e)
        {
            result = false;
        }

        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return result;
    }

    private static boolean isElementPresent(WebElement element) {
        boolean result = false;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try{
            result = element!=null;
        }
        catch (NoSuchElementException e)
        {        }
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return result;
    }

    /*
    Обработка всплывающего уведомления - результата операции
    */
    public static void messageResultOfOperation(String entity) throws Exception{
        boolean actual_result=true;
        waitForElementPresent(By.xpath("/html/body/ul/li/div"));
        String messageType=driver.findElement(By.xpath("/html/body/ul/li/div")).getAttribute("class");
        String message=driver.findElement(By.xpath("/html/body/ul/li/div")).getText();
        while (isElementPresent(By.xpath("/html/body/ul/li")))
        {
//            driver.findElement(By.xpath("/html/body/ul/li")).click();//ждем, когда сообщение скроется, чтобы не было нескольких сообщений в случае слишом быстрого выполнения тестов
        }
        switch (messageType){
            case "noty_bar noty_type_success":
                actual_result=true;
                break;
            case "noty_bar noty_type_error":
            case "noty_bar noty_type_warning":
                actual_result=false;
                break;
            default:
                fail("Нет сообщения о результате операции!");
        }

        if (actual_result!=expected_result)
        {
            System.out.println("Некорректный результат теста!\nОперация: " + operation +
                    "\nСущность: " + entity +
                    "\nОжидаемый результат: " + expected_result +
                    "\nАктуальный результат: " + (messageType.equals("noty_bar noty_type_success") ? "true" : "false") +
                    "\nТип сообщения: " + (messageType.equals("noty_bar noty_type_success") ? "success" : messageType.equals("noty_bar noty_type_warning") ? "warning" : messageType.equals("noty_bar noty_type_error") ? "error" : "Прочее") +
                    "\nСообщение: " + message +
                    (messageType.equals("noty_bar noty_type_success") ? "":("\n" + getScreenshot(messageType.equals("noty_bar noty_type_warning")? "warning": messageType.equals("noty_bar noty_type_error")? "error":"fail"))));
            fail();
        }
    }

    /*
    Проскроллить элемент на заданный шаг
     */
    public static void scroll(WebElement webElement, int step) {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        String code = "var element = document.getElementById('" + webElement.getAttribute("id") + "');" +
                "element.scrollTop+=" + step + ";";
        ((JavascriptExecutor)augmentedDriver).executeScript(code);
    }
    /*
    Сделать скриншот элемента
     */
    public static String getScreenshot(String result)
    {
        String filePath;
        try{
            ImageIO.write(getScreenshot(driver.findElement(By.id("view-content"))), "png", new File(filePath = "/home/igor/testswork/testsmyobject/src/test/screenshots/" + result + "s/" + result + "_" + (new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(System.currentTimeMillis())) + ".png"));

            return "Скриншот:" + filePath;
        }
        catch (IOException e){
            return "Не получилось сделать скриншот!\n" + e.getMessage();
        }

    }

    public static BufferedImage getScreenshot(WebElement element)
    {
        Screenshot screen;
        BufferedImage img;
        int step=Integer.parseInt(element.getAttribute("clientHeight"));
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        if(Integer.parseInt(element.getAttribute("scrollTop"))!=0)
            scroll(element, (-1) * Integer.parseInt(element.getAttribute("scrollTop")));
        try{
            BufferedImage tmp_img;
            int height=0;//текущая высота скрина
            if (element.getAttribute("id").equals("view-content")) // если элемент view-content, то сохраняем вместе с "шапкой" с главными кнопками
            {
                img = new BufferedImage(driver.findElement(By.id("top-panel")).getSize().getWidth(),
                        Integer.parseInt(driver.findElement(By.id("top-panel")).getAttribute("scrollHeight")) + Integer.parseInt(element.getAttribute("scrollHeight")),
                        BufferedImage.TYPE_INT_ARGB);
                screen = new AShot().takeScreenshot(augmentedDriver, driver.findElement(By.id("top-panel")));
                tmp_img = screen.getImage();
                img.getGraphics().drawImage(tmp_img, 0, 0, null);
                height+=Integer.parseInt(driver.findElement(By.id("top-panel")).getAttribute("scrollHeight"));
            }
            else
                img = new BufferedImage(element.getSize().getWidth(), Integer.parseInt(element.getAttribute("scrollHeight")), BufferedImage.TYPE_INT_ARGB);
            screen = new AShot().takeScreenshot(augmentedDriver, element);

            tmp_img = screen.getImage();
            img.getGraphics().drawImage(tmp_img, 0, Integer.parseInt(driver.findElement(By.id("top-panel")).getAttribute("scrollHeight")), null);
            while((Integer.parseInt(element.getAttribute("scrollHeight")) - Integer.parseInt(element.getAttribute("scrollTop")))>2*step)
            {
                height+=step;
                scroll(element.findElement(By.id("view-content")), step);//перемещаем на step пикселей

                screen = new AShot().takeScreenshot(augmentedDriver,element);
                tmp_img = screen.getImage();
                img.getGraphics().drawImage(tmp_img,0,height,null);
            }
            //последний кусочек - остатки
            if((Integer.parseInt(element.getAttribute("scrollHeight")) - Integer.parseInt(element.getAttribute("scrollTop")) - Integer.parseInt(element.getAttribute("clientHeight")))!=0){
                step = img.getHeight()/* Integer.parseInt(element.getAttribute("scrollHeight"))*/ - Integer.parseInt(element.getAttribute("scrollTop"));
                //height+=step;
                scroll(element.findElement(By.id("view-content")), step);//перемещаем на step пикселей

                screen = new AShot().takeScreenshot(augmentedDriver,element);
                tmp_img = screen.getImage();
                img.getGraphics().drawImage(tmp_img,0,img.getHeight() - Integer.parseInt(element.getAttribute("clientHeight")),null);
            }
            return img;
        }catch(Exception e){
            return null;
        }
    }

    /*
    Заполнение данных
     */
    public static void input(String nameField, String value) throws Exception
    {
        if (nameField.equals("Поиск по списку")) {
            driver.findElement(By.xpath("//input[@placeholder=\"Поиск по списку\"]")).sendKeys(value+Keys.ENTER);
            TimeUnit.SECONDS.sleep(2);
            waitForPageLoad();
        }
        else//остан
        {
            setData(nameField, value);
            if (!isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]")))
                field="";
            else
                field="/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]";

            /////////System.out.println(field);

            if (/*nameField.contains("Email")||nameField.contains("Пароль")||*/nameField.contains("Имя")||nameField.contains("Местонахождение")||nameField.contains("Адрес")||
                    nameField.contains("Телефон")||nameField.contains("Номер телефона")||
                    nameField.contains("Факс")||nameField.contains("Номер факса"))
            {
                String[] arr=new String[3];
                if(value.split("\\-").length>0)//расщепление на строки 1//-2
                    arr=value.split("\\-");
                for (int i=0;i<arr.length;i++)
                {
                    if (arr[i]==null)
                        arr[i]="";
                    try {
                        elements=driver.findElements(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::*[1]/descendant-or-self::input"));
                        if (elements.size()<1)
                            throw new IndexOutOfBoundsException();
                        if (elements.get(i).getAttribute("disabled")==null){
                            elements.get(i).click();
                            elements.get(i).sendKeys(additionalString + arr[i]);
                        }
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        fail("Не удалось найти поле ввода");
                    }
                }
                return;
            }
            elements=driver.findElements(By.xpath("(" + parent + "/following-sibling::node()//*[contains(text(),\"" + nameField + "\")]/following-sibling::*[1]/descendant-or-self::input|" +
                    parent + "/following-sibling::node()//*[contains(text(),\"" + nameField + "\")]/following-sibling::*[1]/descendant-or-self::textarea" + ")"));

            for(WebElement el:elements)
            {
                if (count_fields.get(parent + nameField)>1)
                    el = elements.get(count_fields.get(parent + nameField)-1);
                try {
                    if (el.isDisplayed()) {
                        wait.until(visibilityOf(el));
                        el.click();
                        el.sendKeys(additionalString + value);
                    }
                }
                catch (WebDriverException e){
                    scroll(driver.findElement(By.id("view-content")), Integer.parseInt(el.getAttribute("scrollHeight")));
                    if (el.isDisplayed()) {
                        wait.until(visibilityOf(el));
                        el.click();
                        el.sendKeys(additionalString + value);
                    }
                }
                break;
            }
        }
    }

    public static void input(String nameField, String val1,String val2, String val3) throws Exception
    {
        input(nameField, val1 + "-" + val2 + "-" + val3);
    }

    public static boolean button(String nameField)
    {
        setData(nameField,null);
        try {
            if (isElementPresent(By.id("add-docs-to-responsible"))&&legend.equals("Нормативные документы, требования которых были нарушены"))
                driver.findElement(By.id("add-docs-to-responsible")).click();
            else {
                //wait.until(elementToBeClickable(By.xpath(parent + "/following-sibling::node()/descendant-or-self::button[1][contains(descendant-or-self::node(),\"" + nameField + "\")]")));
                for(int i=1;i<10;i++)
                    try {
                        driver.findElement(By.xpath(parent + "/following-sibling::node()/descendant-or-self::button[" + i + "][contains(descendant-or-self::node(),\"" + nameField + "\")]")).click();
                        break;
                    }
                    catch (ElementNotVisibleException e){
                    }
            }
        }
        catch(Exception e)
        {
            fail(e.toString() + e.getMessage());
        }
        return true;
    }

    public static void label(String nameField)
    {
        setData(nameField,null);
        driver.findElement(By.xpath(parent)).click();
        driver.findElement(By.xpath(parent + "/following-sibling::node()//label/input[contains(following-sibling::text(),\"" + nameField + "\")]/..")).click();
    }

    public static void select(String nameField, String value)
    {
        setData(nameField, value);

        try { //проверяем, является ли строка числом - чтобы опеределить по индексу или по значению
            Integer.parseInt(value);
        } catch (Exception e) {
            isSelectByValue =1;
        }

        if (value==null||value.equals(""))
            return;
        //кнопочная версия
        wait.until(visibilityOfElementLocated(By.xpath("//label[contains(text(),\"" + nameField + "\")]")));
        if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::div[@class=\"my-combobox\"]")))
        {
            try {
                elements = driver.findElements(By.xpath("(" + parent + "/following-sibling::node()//label[normalize-space(text())=\"" + nameField + "\"]//following-sibling::div[@class=\"my-combobox\"]" + ")"));
                for (WebElement el:elements) {
                    if (count_fields.get(parent + nameField) > 1)
                        el = elements.get(elements.size()-1);
                    if (el.isDisplayed()) {
                        wait.until(elementToBeClickable(el.findElement(By.xpath(".//button[contains(@class,\"show-list\")]"))));
                        el.findElement(By.xpath(".//button[contains(@class,\"show-list\")]")).click();//стрелочка
                        //TimeUnit.SECONDS.sleep(5);
                        //нажатие на выбранный пункт сделано через actions, т.к. обычный click() попадает мимо и в некоторых случаях нажимает на кнопки
                        //Actions builder = new Actions(driver);
                        wait.until(visibilityOf(el.findElement(By.xpath(".//div[@class=\"my-combobox-item\"]"))));
                        //по индексу
                        if (isSelectByValue == 0)
                            //builder.moveToElement(el.findElement(By.xpath(".//div[@class=\"my-combobox-item\"][" + Integer.toString(Integer.parseInt(value) + 1) + "]")), 5, 5).click().build().perform();
                            el.findElement(By.xpath(".//div[@class=\"my-combobox-item\"][" + Integer.toString(Integer.parseInt(value) + 1) + "]")).click();//по номеру
                        //по значению
                        else
                            //builder.moveToElement(el.findElement(By.xpath(".//div[@class=\"my-combobox-item\" and contains(text(),\"" + value + "\")]")), 5, 5).click().build().perform();
                            el.findElement(By.xpath(".//div[@class=\"my-combobox-item\" and contains(text(),\"" + value + "\")]")).click();//по названию
                        return;
                    }
                }
            }
            catch (Exception e)
            {
                fail(e.getMessage()+"\nНеверно задан индекс записи!\n" + getScreenshot("fail"));
            }
        }
        else
            //select-версия
            if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select")))
            {
                //по индексу
                if (isSelectByValue ==0)
                    new Select(driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select"))).selectByIndex(Integer.parseInt(value));
                    //по значению
                else
                    new Select(driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select"))).selectByVisibleText(value);
            }
            else
                //rightview-версия
                if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..[//button[contains(@class,\"choose-btn\")] and @style=\"\"]")) ||
                        isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@class,\"choose-btn\")]")))
                {
                    try{
                        elements=driver.findElements(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@class,\"choose-btn\")]"));
                        for(WebElement el:elements)
                        {
                            if (count_fields.get(parent + nameField)>1)
                                el = elements.get(elements.size()-1);
                            if (el.isDisplayed()){
                                el.click();
                                active_view="right-view";
                                //по индексу
                                wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"" + active_view + "\"]//button[contains(@class,\"refresh\")]/span")));
                                if (isSelectByValue ==0)
                                    selectRow(Integer.parseInt(value));
                                    //по значению
                                else {
                                    input("Поиск по списку",value);
                                    selectRow(0);
                                }
                                active_view="workspace";
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        fail("Неверно задан индекс записи!\n" + e.getMessage());
                    }
                }
                else{ // right-view выбор из справочника
                    if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]")))
                    {
                        //wait.until(visibilityOfElementLocated(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]//button/span[contains(following-sibling::text(),\"Выбрать\")]/..")));
                        driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]//button/span[contains(following-sibling::text(),\"Выбрать\")]/..")).click();
                        active_view="right-view";
                        driver.findElement(By.xpath("//button[contains(text(),\"Развернуть всё\")]")).click();
                        driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//descendant-or-self::ul[1]//*[contains(text(),\"" + value + "\")]")).click();
                        driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button/span[contains(following-sibling::text(),\"Готово\")]/..")).click();
                        active_view="workspace";
                    }
                    else
                        if (!button("Добавить"))
                            fail("Не найден элемент select (поле выбора из списка \"" + nameField + "\")");
                }
    }

    public static void select(String nameField, int value)
    {
        isSelectByValue =0;
        select(nameField, Integer.toString(value));
    }

    public static void unselect(String nameField)
    {
        setData(nameField, null);

        if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@title,\"Обнулить значение\")]")))
            driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@title,\"Обнулить значение\")]")).click();
        else {
            if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(text(),\"Очистить\")]")))
            {
                driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(text(),\"Очистить\")]")).click();
            }
            else
                select(nameField, 0);
        }
        if (emptyFields == null )
            emptyFields = new ArrayList<>();
        if (emptyFieldsLegend == null )
            emptyFieldsLegend = new ArrayList<>();
        emptyFields.add(nameField);
        emptyFieldsLegend.add(legend);
    }

    public static void addByRightView(String nameField, String value)
    {

    }

    public static void tree(String nameField, String value)
    {
        setData(nameField, value);
        if (value.equals(""))
        {
            driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]/div/span")).click();
            if (driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]")).getAttribute("class").contains("jqtree-selected"))
                driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]/div/span")).click();
        }
        else
            driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]//*[contains(text(),\"" + value + "\")]")).click();
    }

    public static void date(String nameField, String value)
    {
        setData(nameField, value);
        setDate(driver.findElement(By.xpath(parent + "/..//*[contains(text(),\"" + nameField + "\")]/..//input")), value);
    }

    public static void check(String nameField)
    {
        setData(nameField, "true");
        if(!getValue(nameField).equals("true"))
            driver.findElement(By.xpath(parent + "/..//*[contains(normalize-space(label),\"" + nameField + "\") or contains(normalize-space(span),\"" + nameField + "\")]//input")).click();
    }

    public static void uncheck(String nameField)
    {
        setData(nameField, "false");
        if(!getValue(nameField).equals("false"))
            driver.findElement(By.xpath(parent + "/..//*[contains(normalize-space(label),\"" + nameField + "\") or contains(normalize-space(span),\"" + nameField + "\")]//input")).click();
    }

    public static void switcher(String nameField,String value)
    {
        setData(nameField, value);
        String factValue="";
        String val1=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]/*[contains(text(),\"\")][1]")).getText();
        String val2=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]/*[contains(text(),\"\")][2]")).getText();
        String val = value.equals(val1) ? "on" : value.equals(val2) ? "off" : "";
        WebElement el=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input[following-sibling::*[contains(@class,\"switcher\")]]"));
        //driver.findElement(By.xpath(parent + "/*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]")).click();
        if (el.getAttribute("type").equals("checkbox")&&el.getAttribute("checked")!=null)
            factValue=el.getAttribute("checked").equals("true") ? "on" : "off";
        else if (el.getAttribute("checked") == null)
            factValue= "off";
        else fail();
        if(!factValue.equals(val))
            driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]")).click();
    }

    public static void include(int index){
        waitForPageLoad();
        if(isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")))
        {
            driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")).click();
            new Select(driver.findElement(By.xpath("//label[text()=\"Поле\"]/parent::*/select"))).selectByVisibleText("Порядковый №");
            new Select(driver.findElement(By.xpath("//label[text()=\"Сортировка\"]/parent::*/select"))).selectByVisibleText("▾ - по убыванию");
            driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")).click();//убираем сортировку
        }
        for(int i=0;i<30;i++)
        {
            try{
                new Actions(driver).moveToElement(driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div"))).
                    click(driver.findElement(By.xpath("//button[contains(@title,\"Включить\")][" + (index + 1) + "]"))).build().perform();
                //wait.until(visibilityOfElementLocated(By.xpath("//button[contains(@title,\"Включить\")][" + (index + 1) + "]")));
                //new Actions(driver)..perform();
                //driver.findElement(By.xpath("//button[contains(@title,\"Включить\")][" + (index + 1) + "]")).sendKeys("\n");
                //driver.
                return;
            }
            catch (StaleElementReferenceException e)
            {            }
        }
        fail("Не удалось включить!");
    }

    public static void exclude(int index){
        waitForPageLoad();
        boolean excluded=false;
        sortList("Порядковый №","▾ - по убыванию");
        /*new Actions(driver).moveToElement(driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div"))).perform();
        //div[@id="" + active_view + ""]//div[@id="grid-rows" or @class="grid-rows"]/div
        //new Actions(driver).moveToElement(el = driver.findElement(By.xpath("//button[@title=\"Включить в состав подобъекта\"][" + index + "]"))).perform();
        driver.findElement(By.xpath("//button[contains(@title,\"Исключить\")][" + index + "]")).click();
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить исключение*/
        for(int i=0;i<30 && !excluded;i++)
        {
            try{
                new Actions(driver).moveToElement(driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div"))).perform();
                driver.findElement(By.xpath("//button[contains(@title,\"Исключить\")][" + (index + 1) + "]")).click();
                (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить исключение
                excluded=true;
            }
            catch (StaleElementReferenceException e)
            {
                excluded=false;
            }
        }
        if (!excluded) fail("Не удалось исключить!");
    }

    public static void refresh(){
        driver.navigate().refresh();
    }

    public static void save() {
        waitForPageLoad();//
        if(isElementPresent(By.xpath("//button[contains(text(),\"Сохранить\")]")))
            driver.findElement(By.xpath("//button[contains(text(),\"Сохранить\")]")).click();
        else
            driver.findElement(By.xpath("master-button")).click();
    }

    public static void add()
    {
        waitForPageLoad();
        wait.until(elementToBeClickable(By.id("master-button")));
        driver.findElement(By.id("master-button")).click();
    }

    public static void edit() throws Exception
    {
        waitForPageLoad();
        if (isElementPresent(By.xpath("//button[contains(text(),\"Редактировать\")]")))
            driver.findElement(By.xpath("//button[contains(text(),\"Редактировать\")]")).click();
        else {
            wait.until(visibilityOfElementLocated(By.id("actions")));
            if (isElementPresent(By.xpath("//button[contains(text(),\"Изменить\")]")))
                driver.findElement(By.xpath("//button[contains(text(),\"Изменить\")]")).click();
            else
                if (isElementPresent(By.id("edit")))
                    driver.findElement(By.id("edit")).click();
                else
                    if (isElementPresent(By.xpath("//a[contains(text(),\"Редактировать\")]")))
                        driver.findElement(By.xpath("//a[contains(text(),\"Редактировать\")]")).click();
                    else
                        fail("Не удалось найти кнопку редактирования!\n" + getScreenshot("fail"));
        }
        TimeUnit.SECONDS.sleep(2);
    }

    public static void delete() throws Exception {
        waitForPageLoad();
        if (isElementPresent(By.id("remove")))
            driver.findElement(By.id("remove")).click();//кликаем удалить
        else if (isElementPresent(By.xpath("//button[contains(@class,\"remove\")]"))) {
                sortList("Порядковый №","▾ - по убыванию");
                driver.findElement(By.xpath("//button[contains(@class,\"remove\")]")).click();//кликаем удалить
            }
            else
                driver.findElement(By.xpath("//*[contains(text()[last()],\"Удалить\")]")).click();//кликаем удалить
        TimeUnit.SECONDS.sleep(2);
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить удаление
        if (isElementPresent(By.xpath("//*[@class = \"alert alert-warning\"]"))) {
            /*если есть связка с другими сущностями*/
            System.out.println("Невозможно удалить сотрудника\n" + "Для удаления данной сущности, сначала необходимо убрать связь с ней из данных, представленных ниже");
            legend = "привязан";//м.б. не легенд тут надо
        }/*пока оставим else т.к. нет кода для выделения и отвязки сущностей*/
        else
        wait.until(invisibilityOfElementLocated(By.xpath("//*[@class='overlay']")));
        //TimeUnit.SECONDS.sleep(2);//ждем секунду
    }

    public static void back(){
        waitForPageLoad();
        driver.findElement(By.id("history-back")).click();
    }

    public static void selectRow(int index) throws Exception
    {
        waitForPageLoad();
        setParent();
        waitForElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div"));
        wait.until(elementToBeClickable(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div")));
        sortList("Порядковый №","▴ - по возрастанию");
        sortList("Порядковый №","▾ - по убыванию");
        for(int i=0;i<10;i++)
            try{
                waitForPageLoad();
                if(isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]//span[contains(@class,\"expand-button\")]")))
                    //если древовидная структура списка
                    driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[" + (index+1) + "][@class]/div")).click();
                else
                    //driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[" + (index+1) + "][@class]")).click();
                {//делаем через Actions клик не в середину, чтобы случаем не кликнуть по какой-нибудь ссылке
                    //wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[" + (index+1) + "]")));
                    //wait.until(elementToBeClickable(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[" + (index+1) + "]")));
                    TimeUnit.SECONDS.sleep(1);//magic - с ожиданиями валится при редактировании нар.протокола
                    Actions builder = new Actions(driver);
                    builder.moveToElement(driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[" + (index+1) + "]")), 1, 1).click().build().perform();
                }
                waitForPageLoad();
                return;
            }
            catch (IndexOutOfBoundsException e) {
                if(isListEmpty())
                    fail("Список пуст!" + getScreenshot("fail"));
            }
            catch (StaleElementReferenceException e){        }
        fail("Не удалось выбрать!\n" + getScreenshot("fail"));
    }

    public static void sortList(String column, String sortKind){
        for (int i = 0;i<10;i++){
            try{
                if(isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")))
                {
                    driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")).click();
                    new Select(driver.findElement(By.xpath("//label[text()=\"Поле\"]/parent::*/select"))).selectByVisibleText(column);
                    new Select(driver.findElement(By.xpath("//label[text()=\"Сортировка\"]/parent::*/select"))).selectByVisibleText(sortKind);
                    driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button[@title=\"Сортировка\"]")).click();
                }
                return;
            }
            catch (StaleElementReferenceException e)
            {}
        }
    }

    public static boolean isListEmpty(){
        setParent();
        wait.until(visibilityOf(driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]"))));
        //wait.until(invisibilityOfElementLocated(By.xpath("//img[@class=\"grid-preloader\"]")));
        if(!isElementPresent(By.xpath("//div[@id=\"workspace\"]//button[contains(@class,\"refresh\")]/span")))
            wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"workspace\"]//button[contains(@class,\"refresh\")]/span")));
        return !isElementPresent(By.xpath("//div[@id=\"" + active_view + "\"]//div[@id=\"grid-rows\" or @class=\"grid-rows\"]/div[@class]"));
    }

    public static String curDate(){
        return new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(System.currentTimeMillis());
    }

    public static String revisionValue(String nameField){
        try{
            setParent();
            for (int i = 0;i<10;i++)
                try{
                    return driver.findElement(By.xpath(parent + "/following-sibling::node()//descendant-or-self::node()[contains(text(),\"" + nameField + "\")]/../following-sibling::div")).getText();
                }
                catch (StaleElementReferenceException | NoSuchElementException e)
                {                }
        }
        catch (Exception e)
        {
            fail("Ошибка при проверке значения поля " + nameField + "\n" +e);
        }
        return "";
    }

    public static String revisionValueFromList(String nameField){
        try{
            setParent();
            wait.until(visibilityOfElementLocated(By.xpath("//div[@class=\"grid-rows\"]//descendant-or-self::node()[contains(text(),\"" + nameField + "\")]/../..")));
            for (int i = 0;i<10;i++)
                try{
                    return driver.findElement(By.xpath("(//div[@class=\"grid-rows\"]//descendant-or-self::node()[contains(text(),\"" + nameField + "\")]/../..)[1]")).getText();
                }
                catch (StaleElementReferenceException e)
                {                }
        }
        catch (Exception e)
        {
            fail("Ошибка при проверке значения поля " + nameField + "\n" +e);
        }
        return "";
    }

    public static String getValue(String nameField){
        String xPath;
        WebElement element;
        waitForPageLoad();
        setParent();
        xPath=parent + "/following-sibling::node()//descendant-or-self::*[contains(normalize-space(label),\"" + nameField + "\") or contains(normalize-space(span),\"" + nameField + "\")]";
        wait.until(visibilityOfElementLocated(By.xpath(xPath)));
        try{
            if (isElementPresent(By.xpath(xPath+"//descendant-or-self::input[1]"))){
                element=driver.findElement(By.xpath(xPath+"//descendant-or-self::input[1]"));

                switch (element.getAttribute("type"))
                {
                    case "text":
                        return element.getAttribute("value");
                    case "checkbox":
                        if(element.getAttribute("checked")==null || element.getAttribute("checked").equals("false"))
                            return "false";
                        else
                            return "true";
                    default:
                        throw new NoSuchElementException("Нет подходящих элементов input.");
                }
            }
            else if (isElementPresent(By.xpath(xPath + "//descendant-or-self::textarea[1]")))
                {
                    element=driver.findElement(By.xpath(xPath+"//descendant-or-self::textarea[1]"));
                    return element.getAttribute("value");
                }
                else if (isElementPresent(By.xpath(xPath + "//span[@class=\"switcher\"]")))
                    {
                        element=driver.findElement(By.xpath(xPath+"//descendant-or-self::input[1]"));
                        return element.getAttribute("value");
                    }
            throw new NoSuchElementException("Нет подходящих элементов.");
        }
        catch (NoSuchElementException e) {
            fail(e.getMessage()+" Элемент для возвращения значения не найден или задан некорректно!\n"+getScreenshot("fail"));
        }
        return null;
    }

    public static String[] checkEmptyFields() throws Exception{
        int i = 0;
        ArrayList<String> not_empty_fields = new ArrayList<>();
        String leg;
        for(String elem: emptyFields)
        {
            if (!emptyFieldsLegend.get(i).equals(""))
                leg = "//legend[text()='" + emptyFieldsLegend.get(i) + "']";
            else
                leg="";
            if (isElementPresent(By.xpath(leg + "//following-sibling::*//label[contains(text(),\"" + elem + "\")]/../span[contains(text(),\"*\")]"))
                    && !isElementPresent(By.xpath(leg + "//div[contains(@class,\"invalid\")]//label[contains(text(),\"" + elem + "\")]")))
            {
                not_empty_fields.add(driver.findElement(By.xpath(leg + "//following-sibling::*//div[contains(label,\"" + elem + "\")]/parent::*/parent::*[contains(@class,\"invalid\")]/preceding-sibling::legend[1]")).getText());
            }
            else
                if (isElementPresent(By.xpath(leg + "//following-sibling::*//label[contains(text(),\"" + elem + "\")]/span[contains(text(),\"*\")]"))
                        && !isElementPresent(By.xpath(leg + "//following-sibling::*//div[contains(@class,\"invalid\")]//label[contains(text(),\"" + elem + "\")]"))
                        && !isElementPresent(By.xpath(leg + "//following-sibling::*[contains(@class,\"invalid\") and label[contains(text(),\"" + elem + "\")]][1]//label[contains(text(),\"" + elem + "\")]")))
                    not_empty_fields.add(elem);
            i++;
        }
        return not_empty_fields.toArray(new String[not_empty_fields.size()]);
    }

    public static void setData(String nameField, String value) {
        waitForPageLoad();
        setParent();

        switch (operation) {
            case "изменение":
                additionalString = Keys.HOME + Keys.chord(Keys.SHIFT, Keys.END) + Keys.DELETE;
                break;
            case "добавление":
            case "удаление":
            default:
                additionalString = Keys.HOME + Keys.chord(Keys.SHIFT, Keys.END) + Keys.DELETE;
                break;
        }

        if (value != null)
            if (value.equals("") || value.equals("--") || value.equals("false")) {
                if (emptyFields == null)
                    emptyFields = new ArrayList<>();
                if (emptyFieldsLegend == null)
                    emptyFieldsLegend = new ArrayList<>();
                emptyFields.add(nameField);
                emptyFieldsLegend.add(legend);
            }
        count_fields.put(parent + nameField, count_fields.containsKey(parent + nameField) ? count_fields.get(parent + nameField) + 1 : 1);
    }

    /*public static void setData(String nameField, String inputType, String value) {
        waitForPageLoad();
        setParent();

        switch (operation){
            case "изменение":
                additionalString = Keys.HOME + Keys.chord(Keys.SHIFT,Keys.END) + Keys.DELETE;
                break;
            case "добавление":
            case "удаление":
            default:
                additionalString = Keys.HOME + Keys.chord(Keys.SHIFT,Keys.END) + Keys.DELETE;
                break;
        }

        if (value!=null)
            if (value.equals("")||value.equals("--")||value.equals("false"))
            {
                if (emptyFields==null)
                    emptyFields=new ArrayList<>();
                emptyFields.add(nameField);
            }
        count_fields.put(parent + nameField, count_fields.containsKey(parent + nameField) ? count_fields.get(parent + nameField)+1:1);

        switch (inputType){

            case "button":
                if (isElementPresent(By.id("add-docs-to-responsible"))&&legend.equals("Нормативные документы, требования которых были нарушены"))
                    driver.findElement(By.id("add-docs-to-responsible")).click();
                else {
                    //wait.until(elementToBeClickable(By.xpath(parent + "/following-sibling::node()/descendant-or-self::button[1][contains(descendant-or-self::node(),\"" + nameField + "\")]")));
                    for(int i=1;i<10;i++)
                        try {
                            driver.findElement(By.xpath(parent + "/following-sibling::node()/descendant-or-self::button[" + i + "][contains(descendant-or-self::node(),\"" + nameField + "\")]")).click();
                            break;
                        }
                        catch (ElementNotVisibleException e){
                            continue;
                        }
                }
                break;
            case "label":
                driver.findElement(By.xpath(parent)).click();
                driver.findElement(By.xpath(parent + "/following-sibling::node()//label/input[contains(following-sibling::text(),\"" + nameField + "\")]/..")).click();
                break;
            case "select":
                if (value==null||value.equals(""))
                    break;
                //кнопочная версия
                wait.until(visibilityOfElementLocated(By.xpath("//label[contains(text(),\"" + nameField + "\")]")));
                if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::div[@class=\"my-combobox\"]")))
                {
                    try {
                        elements = driver.findElements(By.xpath("(" + parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]//following-sibling::div[@class=\"my-combobox\"]" + ")"));
                        for (WebElement el:elements) {
                            if (count_fields.get(parent + nameField) > 1)
                                el = elements.get(elements.size()-1);
                            if (el.isDisplayed()) {
                                wait.until(elementToBeClickable(el.findElement(By.xpath(".//button[contains(@class,\"show-list\")]"))));
                                el.findElement(By.xpath(".//button[contains(@class,\"show-list\")]")).click();//стрелочка
                                //по индексу
                                if (isSelectByValue == 0)
                                    el.findElement(By.xpath(".//div[@class=\"my-combobox-item\"][" + Integer.toString(Integer.parseInt(value) + 1) + "]")).click();//по номеру
                                    //по значению
                                else
                                    el.findElement(By.xpath(".//div[@class=\"my-combobox-item\" and contains(text(),\"" + value + "\")]")).click();//по названию
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        fail(e.getMessage()+"Неверно задан индекс записи!");
                    }
                }
                else
                    //select-версия
                    if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select")))
                    {
                        //по индексу
                        if (isSelectByValue==0)
                            new Select(driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select"))).selectByIndex(Integer.parseInt(value));
                            //по значению
                        else
                            new Select(driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//select"))).selectByVisibleText(value);
                    }
                    else
                        //rightview-версия
                        if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..[//button[contains(@class,\"choose-btn\")] and @style=\"\"]")) ||
                                isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@class,\"choose-btn\")]")))
                        {
                            try{
                                elements=driver.findElements(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@class,\"choose-btn\")]"));
                                for(WebElement el:elements)
                                {
                                    if (count_fields.get(parent + nameField)>1)
                                        el = elements.get(elements.size()-1);
                                    if (el.isDisplayed()){
                                        el.click();
                                        active_view="right-view";
                                        //по индексу
                                        wait.until(visibilityOfElementLocated(By.xpath("//div[@id=\"" + active_view + "\"]//button[contains(@class,\"refresh\")]/span")));
                                        if (isSelectByValue==0)
                                            selectRow(Integer.parseInt(value));
                                            //по значению
                                        else {
                                            input("Поиск по списку",value);
                                            selectRow(0);
                                        }
                                        active_view="workspace";
                                        break;
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                fail("Неверно задан индекс записи!" + e.getMessage());
                            }
                        }
                        else{ // right-view выбор из справочника
                            if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]")))
                            {
                                //wait.until(visibilityOfElementLocated(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]//button/span[contains(following-sibling::text(),\"Выбрать\")]/..")));
                                driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//div[contains(@class,\"mo-multiple-select\")]//button/span[contains(following-sibling::text(),\"Выбрать\")]/..")).click();
                                active_view="right-view";
                                driver.findElement(By.xpath("//button[contains(text(),\"Развернуть всё\")]")).click();
                                driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//descendant-or-self::ul[1]//*[contains(text(),\"" + value + "\")]")).click();
                                driver.findElement(By.xpath("//div[@id=\"" + active_view + "\"]//button/span[contains(following-sibling::text(),\"Готово\")]/..")).click();
                                active_view="workspace";
                                break;
                            }
                            else
                            if (!button("Добавить"))
                                fail("Не найден элемент select (поле выбора из спиcка \"" + nameField + "\")");
                        }

                break;
            case "unselect":
                if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@title,\"Обнулить значение\")]")))
                    driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(@title,\"Обнулить значение\")]")).click();
                else {
                    if (isElementPresent(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(text(),\"Очистить\")]")))
                    {
                        driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/..//button[contains(text(),\"Очистить\")]")).click();
                    }
                    else
                        select(nameField, 0);
                }
                if (emptyFields==null)
                    emptyFields=new ArrayList<>();
                emptyFields.add(nameField);
                break;
            case "date":
                setDate(driver.findElement(By.xpath(parent + "/..//*[contains(text(),\"" + nameField + "\")]/..//input")), value);
                break;
            case "check":
                if(!getValue(nameField).equals(value))
                    driver.findElement(By.xpath(parent + "/..//*[contains(normalize-space(label),\"" + nameField + "\") or contains(normalize-space(span),\"" + nameField + "\")]//input")).click();
                break;
            case "switch":
                String factValue="";
                String val1=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]/*[contains(text(),\"\")][1]")).getText();
                String val2=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]/*[contains(text(),\"\")][2]")).getText();
                String val = value.equals(val1) ? "on" : value.equals(val2) ? "off" : "";
                WebElement el=driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input[following-sibling::*[contains(@class,\"switcher\")]]"));
                //driver.findElement(By.xpath(parent + "/*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]")).click();
                if (el.getAttribute("type").equals("checkbox")&&el.getAttribute("checked")!=null)
                    factValue=el.getAttribute("checked").equals("true") ? "on" : "off";
                else if (el.getAttribute("checked") == null)
                    factValue= "off";
                else fail();
                if(!factValue.equals(val))
                    driver.findElement(By.xpath(parent + "/..//*[contains(*,\"" + nameField + "\")]//input/following-sibling::*[contains(@class,\"switcher\")]")).click();
                break;
            case "tree":
                if (value.equals(""))
                {
                    driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]/div/span")).click();

                    if (driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]")).getAttribute("class").contains("jqtree-selected"))
                        driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]/li[1]/div/span")).click();
                }
                else
                    driver.findElement(By.xpath(parent + "/following-sibling::node()//label[contains(text(),\"" + nameField + "\")]/following-sibling::node()/descendant-or-self::ul[1]//*[contains(text(),\"" + value + "\")]")).click();
                break;
        }
    }*/
}
