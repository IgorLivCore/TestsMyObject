package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;
import static Base.MOWebdriver.*;

/**
 * Created by natalia on 16.02.15.
 *
 */
public class Responsibility extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Персонал" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Персонал");
        goTo("Общее","Ответственность");
    }

    @Test(priority=6, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        select("Сотрудник", 0);
        select("Участие в производственном контроле ПБ","отвечает за осуществление ПК");
        input("Зона ответственности", data = "ТестСозданиеОтветственностиЗона" + curDate());

        save();
        messageResultOfOperation("Ответственность");

        goTo("Общее","Персонал");
        goTo("Общее","Ответственность");
        selectRow(0);
        assertEquals(getValue("Зона ответственности"), data, "Ответственность не добавлена");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        unselect("Сотрудник");
        unselect("Участие в производственном контроле ПБ");
        input("Зона ответственности", "");

        save();
        messageResultOfOperation("Ответственность");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);

        select("Сотрудник", 1);
        select("Участие в производственном контроле ПБ", "отвечает за организацию ПК");
        input("Зона ответственности", data = "ТестИзменениеОтветственностиЗона" + curDate());

        save();
        messageResultOfOperation("Ответственность");

        goTo("Общее","Персонал");
        goTo("Общее","Ответственность");
        selectRow(0);
        assertEquals(getValue("Зона ответственности"), data, "Ответственность не изменена");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);

        unselect("Сотрудник");
        unselect("Участие в производственном контроле ПБ");
        input("Зона ответственности", "");

        save();
        messageResultOfOperation("Ответственность");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = getValue("Зона ответственности");
        delete();

        //messageResultOfOperation("Должность");

        goTo("Общее","Персонал");
        goTo("Общее", "Ответственность");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Зона ответственности"), data, "Ответственность не удалена");
        }
    }
}
