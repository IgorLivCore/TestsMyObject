package myobject;

import Base.MOTestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static Base.MOWebdriver.*;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by natalia on 13.05.15.
 */
public class TDModernization extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"ТУ"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Модернизация");
    }

    @Test(priority=20, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        input("Год", "2008");
        input("Сведения о модернизации", data = "ТестСозданиеМодернизацииТУСведения" + curDate());

        save();
        messageResultOfOperation("модернизация ТУ");

        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Модернизация");
        selectRow(0);

        assertEquals(getValue("Сведения о модернизации"), data, "Модернизация ТУ не добавлена");
    }

    @Test(priority=20, groups = { "add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        input("Год", "");
        input("Сведения о модернизации", "");

        save();
        messageResultOfOperation("модернизация ТУ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=220, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);

        input("Год", "2009");
        input("Сведения о модернизации", data = "ТестИзменениеМодернизацииТУСведения" + curDate());

        save();
        messageResultOfOperation("модернизация ТУ");

        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Модернизация");
        selectRow(0);

        assertEquals(getValue("Сведения о модернизации"), data, "Модернизация ТУ не изменена");
    }

    @Test(priority=220, groups = { "edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);

        input("Год", "");
        input("Сведения о модернизации", "");

        save();
        messageResultOfOperation("модернизация ТУ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }
}
