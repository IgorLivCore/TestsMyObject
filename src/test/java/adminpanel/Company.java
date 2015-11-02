package adminpanel;

import Base.AdminTestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.checkEmptyFields;
import static Base.MOWebdriver.getScreenshot;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by natalia on 07.10.15.
 */
public class Company extends AdminTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Компании");
        selectRow(0);
        goTo("Разрешения и ограничения");
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
}
