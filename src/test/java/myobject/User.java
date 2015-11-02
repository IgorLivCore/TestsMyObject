package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;

/**
 * Created by natalia on 02.10.15.
 */
public class User extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Персонал", "Роли"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Прочее","Администрирование");
        goTo("Прочее","Учетные записи");
    }

    @Test(priority=30, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        legend="Данные учетной записи";
        select("Сотрудник, с которым связана данная учетная запись",0);
        input("E-mail (логин)", data = "testUser" + curDate() + "@test.test");
        input("№ телефона", "6385429180");

        legend = "Роли учетной записи";
        select("Роль", 0);

        legend = "Реклама";
        switcher("Показывать рекламу", "Нет");

        save();
        messageResultOfOperation("Пользователь");

        goTo("Прочее","Администрирование");
        goTo("Прочее","Учетные записи");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("E-mail (логин)").trim(), data,"Пользователь не добавлен");
    }


    @Test(priority=30, groups = { "add","empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        legend="Данные учетной записи";
        unselect("Сотрудник, с которым связана данная учетная запись");
        input("Фамилия","");
        input("Имя","");
        input("E-mail (логин)","");

        legend = "Роли учетной записи";
        unselect("Роль");

        save();
        messageResultOfOperation("Пользователь");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=230, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        legend="Данные учетной записи";
        select("Сотрудник, с которым связана данная учетная запись",1);
        input("Фамилия","ТестИзменениеПользователяФамилия");
        input("Имя","ТестИзменениеПользователяИмя");
        input("Отчество","ТестИзменениеПользователяОтчество");
        input("№ телефона", data = new SimpleDateFormat("ddMMyyyyHHmmss").format(System.currentTimeMillis()));

        legend = "Роли учетной записи";
        while (1==1) {  //до тех пока есть кнопка удаления роли - удаляем все прошлые роли
            try {
                button("Удалить");
                (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить удаление
            }
            catch (AssertionError e){
                break;
            }
        }
        button("Добавить");
        select("Роль", 1);

        legend = "Реклама";
        switcher("Показывать рекламу", "Нет");

        save();
        messageResultOfOperation("Пользователь");

        goTo("Прочее","Администрирование");
        goTo("Прочее","Учетные записи");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("№ телефона").trim(), data,"Пользователь не изменен");
    }

    @Test(priority=231, groups = { "edit","empty" })
    public void testEditEmpty() throws Exception
    {
        expected_result = false;
        selectRow(0);
        edit();

        legend="Данные учетной записи";
        input("Фамилия", "");
        input("Имя","");

        legend = "Роли учетной записи";
        button("Добавить");
        unselect("Роль");

        save();
        messageResultOfOperation("Пользователь");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=330, groups = { "deactivate" })
    public void testDeactivate() throws Exception
    {
        operation="деактивация";
        filterList("Статус учетной записи", "select", "Активная");
        selectRow(0);
        data = revisionValue("E-mail (логин)").trim();
        button("Деактивировать");
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить деактивацию

        messageResultOfOperation("Пользователь");

        goTo("Прочее","Администрирование");
        goTo("Прочее","Учетные записи");
        filterList("Статус учетной записи", "select", "Активная");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("E-mail (логин)").trim(), data, "Пользователь не деактивирован!\n" + getScreenshot("fail"));
        }
    }

    @Test(priority=330, groups = { "activate" })
    public void testActivate() throws Exception
    {
        operation="деактивация";
        filterList("Статус учетной записи", "select", "Деактивированная");
        selectRow(0);
        data = revisionValue("E-mail (логин)").trim();
        button("Восстановить");
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.alertIsPresent()).accept();//подтвердить деактивацию

        messageResultOfOperation("Пользователь");

        goTo("Прочее","Администрирование");
        goTo("Прочее","Учетные записи");
        filterList("Статус учетной записи", "select", "Деактивированная");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("E-mail (логин)").trim(), data, "Пользователь не активирован!\n" + getScreenshot("fail"));
        }
    }
}
