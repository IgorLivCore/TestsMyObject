package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;

/**
 * Created by natalia on 22.09.15.
 */
public class PrescriptionViolation extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Персонал", "Предписания РТН" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Производственный контроль","Предписания РТН");
        selectRow(0);
        goTo("Нарушения");
    }

    @Test(priority = 10, groups = { "add"})
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="";
        input("Описание нарушения", data = "ТестСозданиеНарушенияПредписанияОписаниеНарушения" + curDate());
        date("Выполнить до", "23.12.2015");
        select("Ответственный за устранение нарушения", 0);
        input("Причины невыполнения в установленный срок", "ТестСозданиеНарушенияПредписанияПричиныНевыполнения");

        save();
        messageResultOfOperation("Нарушение предписания РТН");

        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Нарушения");

        selectRow(0);
        edit();
        assertEquals(getValue("Описание нарушения"), data, "Нарушение предписания не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        legend="";
        input("Описание нарушения", "");
        input("Выполнить до", "");
        unselect("Ответственный за устранение нарушения");

        save();
        messageResultOfOperation("Нарушение предписания РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        legend="";
        input("Описание нарушения", data = "ТестИзменениеНарушенияПредписанияОписаниеНарушения" + curDate());
        date("Выполнить до", "24.12.2015");
        select("Ответственный за устранение нарушения", 1);
        input("Причины невыполнения в установленный срок", "ТестИзменениеНарушенияПредписанияПричиныНевыполнения");

        save();
        messageResultOfOperation("Нарушение протокола ВП");

        goTo("Производственный контроль","Предписания РТН");
        selectRow(0);
        goTo("Нарушения");

        selectRow(0);
        edit();
        assertEquals(getValue("Описание нарушения"), data, "Нарушение предписания не изменено");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);
        edit();

        legend="";
        input("Описание нарушения", "");
        input("Выполнить до", "");
        unselect("Ответственный за устранение нарушения");

        save();
        messageResultOfOperation("Нарушение предписания РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        edit();
        data = getValue("Описание нарушения");
        back();
        delete();

        //messageResultOfOperation("Нарушение протокола");

        goTo("Производственный контроль","Предписания РТН");
        selectRow(0);
        goTo("Нарушения");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            edit();
            assertNotEquals(getValue("Описание нарушения"), data, "Нарушение протокола не удалено");
        }
    }
}
