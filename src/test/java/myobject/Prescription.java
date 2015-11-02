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
public class Prescription extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Структур. подразделения", "Объекты", "Персонал"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Производственный контроль","Предписания РТН");
    }

    @Test(priority = 10, groups = { "add"})
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="Данные предписания";
        input("Наименование предписания", "ТестСозданиеПредписанияНаименованиеПредписания" + curDate());
        input("№ предписания", data = "ТестСозданиеПредписанияНомерПредписания" + curDate());
        date("Дата предписания", "15.07.2015");
        select("ОПО", 0);
        input("Кем выдано", "ТестСозданиеПредписанияКемВыдано");

        legend="";
        date("Дата начала выполнения предписания", "15.07.2015");
        date("Выполнить до", "15.07.2016");
        select("Ответственный за устранение пунктов предписания", 1);

        legend="Проверка проведена в присутствии";
        select("Сотрудник", 0);

        legend="Структурные подразделения, подвергшиеся проверке";
        button("Добавить");
        select("Структурное подразделение", 0);

        save();
        messageResultOfOperation("Предписание РТН");

        goTo("Производственный контроль", "Предписания РТН");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("№"), data,"Предписание не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        legend="Данные предписания";
        input("№ предписания","");
        input("Дата предписания", "");
        input("Кем выдано", "");

        legend="";
        unselect("Ответственный за устранение пунктов предписания");

        legend="Проверка проведена в присутствии";
        unselect("Сотрудник");

        legend="Структурные подразделения, подвергшиеся проверке";
        button("Добавить");
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Предписание РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        legend="Данные предписания";
        input("Наименование предписания", "ТестИзменениеПредписанияНаименованиеПредписания" + curDate());
        input("№ предписания", data = "ТестИзменениеПредписанияНомерПредписания" + curDate());
        date("Дата предписания", "15.08.2015");
        select("ОПО", 1);
        input("Кем выдано", "ТестИзменениеПредписанияКемВыдано");

        legend="";
        date("Дата начала выполнения предписания", "15.08.2015");
        date("Выполнить до", "15.08.2016");
        select("Ответственный за устранение пунктов предписания", 0);

        legend="Проверка проведена в присутствии";
        select("Сотрудник", 1);

        legend="Структурные подразделения, подвергшиеся проверке";
        select("Структурное подразделение", 1);

        save();
        messageResultOfOperation("Предписание РТН");

        goTo("Производственный контроль", "Предписания РТН");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("№"), data,"Предписание не добавлено");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);
        edit();

        legend="Данные предписания";
        input("№ предписания","");
        input("Дата предписания", "");
        input("Кем выдано", "");

        legend="";
        unselect("Ответственный за устранение пунктов предписания");

        legend="Проверка проведена в присутствии";
        unselect("Сотрудник");

        legend="Структурные подразделения, подвергшиеся проверке";
        button("Добавить");
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Предписание РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("№");
        delete();

        //messageResultOfOperation("Предписание РТН");

        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("№"), data, "Предписание не удалено");
        }
    }
}
