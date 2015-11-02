package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Calendar;

import static org.testng.Assert.*;
import static Base.MOWebdriver.*;

/**
 * Created by natalia on 16.02.15.
 *
 */
public class EmployeeCount extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее", "Персонал");
        goTo("Общее", "Сведения в РТН");
    }


    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        edit();

        //button("Добавить");
        select("Вид надзора", 1);
        input("Руководители", data = Integer.toString(Calendar.getInstance().get(Calendar.HOUR)));
        input("Специалисты", data1 = Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)));
        input("Рабочие", data2 = Integer.toString(Calendar.getInstance().get(Calendar.SECOND)));

        save();
        messageResultOfOperation("Сведения в РТН");

        goTo("Общее", "Персонал");
        goTo("Общее", "Сведения в РТН");
        edit();
        assertEquals(getValue("Руководители"), data, "Сведения в РТН не изменены");
        assertEquals(getValue("Специалисты"), data1, "Сведения в РТН не изменены");
        assertEquals(getValue("Рабочие"), data2, "Сведения в РТН не изменены");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        expected_result=false;
        edit();

        unselect("Вид надзора");

        save();
        messageResultOfOperation("Сведения в РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        edit();
        data = getValue("Руководители");
        data1 = getValue("Специалисты");
        data2 = getValue("Рабочие");
        delete();

        messageResultOfOperation("Сведения в РТН");

        goTo("Общее", "Персонал");
        goTo("Общее", "Сведения в РТН");
        if (!MOWebdriver.isListEmpty()) {
            edit();
            assertNotEquals(getValue("Руководители"), data, "Сведения РТН не удалены");
            assertNotEquals(getValue("Специалисты"), data1, "Сведения РТН не удалены");
            assertNotEquals(getValue("Рабочие"), data2, "Сведения РТН не удалены");
        }
    }
}
