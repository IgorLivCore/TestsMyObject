package adminpanel;

import Base.AdminTestBase;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by natalia on 01.10.15.
 */
public class Message extends AdminTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Сообщения");
    }

    @Test(priority=30, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        goTo("Создать сообщение");

        input("Тема сообщения", data = "ТестСозданиеСообщенияТемаСообщения" + curDate());
        input("Текст сообщения","ТестСозданиеСообщенияТекстСообщения");
        date("Дата старта рассылки", "01.01.2015");
        date("Дата остановки рассылки", "01.01.2016");
        input("Комментарий администратора","ТестСозданиеСообщенияКомментарийАдминистратора");
        selectRow(0);

        save();
        messageResultOfOperation("Сообщение");

        goTo("Сообщения");
        goTo("Список сообщений");
        selectRow(0);
        assertEquals(revisionValue("Тема сообщения"), data,"Сообщение не добавлено");
    }

    @Test(priority=30, groups = { "add","empty" })
    public void testAddEmpty() throws Exception
    {
        operation="добавление";
        expected_result=false;
        goTo("Создать сообщение");

        input("Тема сообщения", "");
        input("Текст сообщения", "");
        input("Дата старта рассылки", "");

        save();
        messageResultOfOperation("Сообщение");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=230, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        goTo("Список сообщений");
        selectRow(0);
        edit();

        input("Тема сообщения", data = "ТестИзменениеСообщенияТемаСообщения" + curDate());
        input("Текст сообщения","ТестИзменениеСообщенияТекстСообщения");
        date("Дата остановки рассылки", "01.02.2016");
        input("Комментарий администратора","ТестИзменениеСообщенияКомментарийАдминистратора");

        save();
        messageResultOfOperation("Сообщение");

        goTo("Сообщения");
        goTo("Список сообщений");
        selectRow(0);
        assertEquals(revisionValue("Тема сообщения"), data,"Сообщение не изменено");
    }

    @Test(priority=231, groups = { "edit","empty" })
    public void testEditEmpty() throws Exception
    {
        operation="изменение";
        expected_result=false;
        goTo("Список сообщений");
        selectRow(0);
        edit();

        input("Тема сообщения", "");
        input("Текст сообщения","");

        save();
        messageResultOfOperation("Сообщение");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=330, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        goTo("Список сообщений");
        selectRow(0);
        data = revisionValue("Тема сообщения");
        delete();

        //messageResultOfOperation("Сообщение");

        goTo("Сообщения");
        goTo("Список сообщений");
        if (!isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Тема сообщения"), data, "Сообщение не удалено!");
        }
    }
}
