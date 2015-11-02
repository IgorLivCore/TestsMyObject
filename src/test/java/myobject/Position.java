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
public class Position extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Структур. подразделения" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Персонал");
        goTo("Общее","Должности");
    }

    @Test(priority=6, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        select("Тип должности", "Специалист");
        input("Название должности", data = "ТестСозданиеДолжностиНазваниеДолжности" + curDate());
        select("Структурное подразделение", 0);
        switcher("Эксплуатация ТУ на ОПО", "Да");
        select("Области аттестации (можно указать несколько)", "Б.2.3.");

        save();
        messageResultOfOperation("должность");

        goTo("Общее","Персонал");
        goTo("Общее","Должности");
        selectRow(0);
        assertEquals(getValue("Название должности"), data, "Должность не добавлена");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        expected_result=false;
        add();

        unselect("Тип должности");
        input("Название должности", "");
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Должность");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);

        select("Тип должности", "Рабочий");
        input("Название должности", data = "ТестИзменениеДолжностиНазваниеДолжности" + curDate());
        select("Структурное подразделение", 1);
        switcher("Эксплуатация ТУ на ОПО", "Да");
        select("Области аттестации (можно указать несколько)", "Б.2.4.");

        save();
        messageResultOfOperation("должность");

        goTo("Общее","Персонал");
        goTo("Общее","Должности");
        selectRow(0);
        assertEquals(getValue("Название должности"), data, "Должность не изменена");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        expected_result=false;
        selectRow(0);

        unselect("Тип должности");
        input("Название должности", "");
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Должность");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = getValue("Название должности");
        back();
        delete();

        //messageResultOfOperation("Должность");

        goTo("Общее","Персонал");
        goTo("Общее", "Должности");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Название должности"), data, "Должность не удалена");
        }
    }
}
