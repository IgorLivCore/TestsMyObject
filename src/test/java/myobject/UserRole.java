package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;
import static org.testng.Assert.assertNotEquals;

/**
 * Created by natalia on 02.11.15.
 */
public class UserRole extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Прочее","Администрирование");
        goTo("Прочее","Роли");
    }

    @Test(priority=30, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        input("Наименование роли", data = "ТестСозданиеРолиНаименование" + curDate());
        input("Описание", "ТестСозданиеРолиОписание");

        legend = "Разрешения для этой роли";
        goTo("Несчастные случаи");
        switcher("Создание несчастного случая", "Да");

        save();
        messageResultOfOperation("Роль");
        //т.к. отсутствуют id, нельзя отсортировать, также нельзя отфильтровать
        //поэтому ограничимся сообщением о рез-те
    }

    @Test(priority=30, groups = { "add","empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        input("Наименование роли", "");

        save();
        messageResultOfOperation("Роль");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=230, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);

        input("Наименование роли", data = "ТестИзменениеРолиНаименование" + curDate());
        input("Описание", "ТестИзменениеРолиОписание");

        legend = "Разрешения для этой роли";
        goTo("Мероприятия");
        switcher("Создание нового мероприятия", "Да");

        save();
        messageResultOfOperation("Роль");

        goToList();
        selectRow(0);
        legend = "";
        assertEquals(getValue("Наименование роли").trim(), data, "Роль не изменена");
    }

    @Test(priority=231, groups = { "edit","empty" })
    public void testEditEmpty() throws Exception
    {
        expected_result = false;
        selectRow(0);

        input("Наименование роли", "");

        save();
        messageResultOfOperation("Роль");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }
}
