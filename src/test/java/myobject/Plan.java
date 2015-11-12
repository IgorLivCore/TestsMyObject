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
public class Plan extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Персонал" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Планы");
    }

    @Test(priority=6, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        select("Тип плана", "Прочее");
        input("Название плана", data = "ТестСозданиеПланаНазваниеПлана" + curDate());
        select("Ответственный", 0);

        save();
        messageResultOfOperation("План");

        goTo("Общее","Планы");
        selectRow(0);
        edit();
        assertEquals(getValue("Название плана"), data, "План не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        expected_result=false;
        add();

        unselect("Тип плана");
        input("Название плана", "");
        unselect("Ответственный");

        save();
        messageResultOfOperation("План");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        select("Тип плана", "Прочее");
        input("Название плана", data = "ТестИзменениеПланаНазваниеПлана" + curDate());
        select("Ответственный", 1);

        save();
        messageResultOfOperation("План");

        goTo("Общее","Планы");
        selectRow(0);
        edit();
        assertEquals(getValue("Название плана"), data, "План не изменен");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        expected_result=false;
        selectRow(0);
        edit();

        unselect("Тип плана");
        input("Название плана", "");
        unselect("Ответственный");

        save();
        messageResultOfOperation("План");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        edit();
        data = getValue("Название плана");
        back();
        delete();

        //messageResultOfOperation("План");

        goTo("Общее","Планы");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            edit();
            assertNotEquals(getValue("Название плана"), data, "План не удален" + getScreenshot("fail"));
        }
    }
}
