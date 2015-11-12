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
public class Protocol extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Структур. подразделения", "Объекты", "Персонал"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Производственный контроль","Протоколы ВП");
    }

    @Test(priority = 10, groups = { "add"})
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="Данные протокола";
        input("Наименование протокола", "ТестСозданиеПротоколаНаименованиеПротокола" + curDate());
        date("Дата проведения проверки", "14.07.2015");
        input("№ протокола", data = "ТестСозданиеПротоколаНомерПротокола" + curDate());
        date("Дата протокола", "15.07.2015");
        select("ОПО", 0);
        select("Лицо, ответственное за проведение проверки", 1);
        switcher("Сведения подлежат подаче в РТН", "Да");

        legend="";
        date("Дата начала выполнения протокола", "15.07.2015");
        date("Выполнить до", "15.07.2016");
        select("Ответственный за устранение пунктов протокола", 2);

        legend="Проверка проведена в присутствии";
        button("Добавить");
        select("Сотрудник", 0);

        legend="Члены комиссии";
        select("Сотрудник",3);
        select("Тип","Член комиссии");

        legend="Структурное подразделение, подвергшееся проверке";
        select("Структурное подразделение", 0);

        save();
        messageResultOfOperation("Протокол ВП");

        goTo("Производственный контроль","Протоколы ВП");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("№ протокола"), data,"Протокол не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        legend="Данные протокола";
        input("№ протокола", "");
        input("Дата протокола", "");
        input("Дата проведения проверки", "");
        unselect("ОПО");
        unselect("Лицо, ответственное за проведение проверки");

        legend="";
        unselect("Ответственный за устранение пунктов протокола");

        legend="Проверка проведена в присутствии";
        button("Добавить");
        unselect("Сотрудник");

        legend="Члены комиссии";
        unselect("Сотрудник");
        unselect("Тип");

        legend="Структурное подразделение, подвергшееся проверке";
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Протоколы ВП");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        legend="Данные протокола";
        input("Наименование протокола", "ТестИзменениеПротоколаНаименованиеПротокола" + curDate());
        date("Дата проведения проверки", "14.08.2015");
        input("№ протокола", data = "ТестИзменениеПротоколаНомерПротокола" + curDate());
        date("Дата протокола", "15.08.2015");
        select("ОПО", 1);
        select("Лицо, ответственное за проведение проверки", 2);
        switcher("Сведения подлежат подаче в РТН", "Да");

        legend="";
        date("Дата начала выполнения протокола", "15.08.2015");
        date("Выполнить до", "15.08.2016");
        select("Ответственный за устранение пунктов протокола", 3);

        legend="Проверка проведена в присутствии";
        select("Сотрудник", 1);

        legend="Члены комиссии";
        select("Сотрудник",0);
        select("Тип","Член комиссии");

        legend="Структурное подразделение, подвергшееся проверке";
        select("Структурное подразделение", 1);

        save();
        messageResultOfOperation("Протокол ВП");

        goTo("Производственный контроль","Протоколы ВП");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("№ протокола"), data,"Протокол не изменен");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);
        edit();

        legend="Данные протокола";
        input("№ протокола", "");
        input("Дата протокола", "");
        unselect("ОПО");
        unselect("Лицо, ответственное за проведение проверки");
        input("Дата проведения проверки", "");

        legend="";
        unselect("Ответственный за устранение пунктов протокола");

        legend="Проверка проведена в присутствии";
        unselect("Сотрудник");

        legend="Члены комиссии";
        unselect("Сотрудник");
        unselect("Тип");

        legend="Структурное подразделение, подвергшееся проверке";
        unselect("Структурное подразделение");

        save();
        messageResultOfOperation("Протокол ВП");

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

        //messageResultOfOperation("Протокол ВП");

        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("№"), data, "Протокол не удален");
        }
    }
}
