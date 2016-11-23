package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.*;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;

/**
 * Created by natalia on 13.02.15.
 *
 * Подобъект должен успешно добавляться из левого меню при корректном вводе всех полей - testAdd
 * Подобъект должен успешно сохранять изменения при корректном вводе всех полей - testEdit
 * Подобъект не должен добавляться при незаполненных обязательных полях - testAddEmpty
 * Подобъект не должен сохранять изменения при незаполненных обязательных полях - testEditEmpty
 * Подобъект должен успешно удаляться - testDelete
 */

public class Element extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Подобъекты");
    }

    @Test(priority=1, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        input("Наименование", data = "ТестСозданиеПодобъектаНаименование" + curDate());
        input("Описание","ТестСозданиеПодобъектаОписание");

        save();

        messageResultOfOperation("подобъект");

        goToList();
        selectRow(0);
        edit();
        assertEquals(getValue("Наименование"), data,"Подобъект не добавлен");
    }

    @Test(priority=2, groups = { "add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        input("Наименование", "");

        save();
        messageResultOfOperation("Подобъект");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=2,/*dependsOnMethods={"testAdd"}, dependsOnGroups = { "addObject","addBaCbyObject" },*/ groups = { "include" })
    public void testAddBaC() throws Exception
    {
        operation="включение в подобъект";
        selectRow(0);
        goTo("ЗиС");
        add();

        selectRow(0);
        edit();
        data=getValue("Название ЗиС");
        back();
        back();
        include(0);

        messageResultOfOperation("подобъект");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Подобъекты");
        selectRow(0);
        goTo("ЗиС");
        input("Поиск по списку", data);
        selectRow(0);
        edit();
        assertEquals(getValue("Название ЗиС"), data,"ЗиС не добавлен в подобъект");
    }

    @Test(priority=2,/*dependsOnMethods={"testAdd"}, dependsOnGroups = { "addObject","addTDbyObject" },*/ groups = { "include" })
    public void testAddTD() throws Exception
    {
        operation="включение в подобъект";
        selectRow(0);
        goTo("ТУ");
        add();

        selectRow(0);
        edit();
        data=getValue("Наименование тех. устройства");
        back();
        back();
        include(0);

        messageResultOfOperation("подобъект");

        goToList();
        selectRow(0);
        goTo("ТУ");
        input("Поиск по списку", data);
        selectRow(0);
        edit();
        assertEquals(getValue("Наименование тех. устройства"), data,"ТУ не добавлен в подобъект");
    }

    @Test(priority=2, /*dependsOnMethods="testAdd", */groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        input("Наименование", data = "ТестИзменениеПодобъектаНаименование" + curDate());
        input("Описание", "ТестСозданиеПодобъектаОписание");

        save();

        messageResultOfOperation("подобъект");

        goToList();
        selectRow(0);
        edit();
        assertEquals(getValue("Наименование"), data,"Подобъект не изменен\n" + getScreenshot("fail"));
    }

    @Test(priority=2, groups = { "add" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);
        edit();

        input("Наименование", "");

        save();
        messageResultOfOperation("Подобъект");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=3,/*dependsOnMethods={"testAddBaC"},*/ groups = { "exclude" })
    public void testDeleteBaC() throws Exception
    {
        operation="исключение из подобъекта";
        selectRow(0);
        goTo("ЗиС");

        selectRow(0);
        edit();
        data=getValue("Название ЗиС");
        back();
        back();
        exclude(0);

        messageResultOfOperation("подобъект");

        goToList();
        selectRow(0);
        goTo("ЗиС");
        if(!MOWebdriver.isListEmpty())
        {
            selectRow(0);
            edit();
            assertNotEquals(getValue("Название ЗиС"), data, "ЗиС не исключен из подобъекта\n" + getScreenshot("fail"));
        }
    }

    @Test(priority=3,/*dependsOnMethods={"testAddTD"}, */groups = { "exclude" })
    public void testDeleteTD() throws Exception
    {
        operation="исключение из подобъекта";
        selectRow(0);
        goTo("ТУ");

        selectRow(0);
        edit();
        data=getValue("Наименование тех. устройства");
        back();
        back();
        exclude(0);

        messageResultOfOperation("подобъект");

        goToList();
        selectRow(0);
        goTo("ТУ");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            edit();
            assertNotEquals(getValue("Наименование тех. устройства"), data, "ТУ не исключен из подобъекта");
        }
    }

    @Test(priority=300, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("Наименование");

        delete();

        messageResultOfOperation("подобъект");

        goToList();
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Наименование"), data, "Подобъект не удален");
        }
    }
}
