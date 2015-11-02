package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;

/**
 * Created by natalia on 14.10.15.
 */
public class Team extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Предприятие");
        goTo("Общее","Структур. подразделения");
    }

    @Test(priority=1, groups = { "add","addObject" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        input("Наименование", data = "ТестСозданиеСПНаименование" + curDate());
        input("Описание","ТестСозданиеСПОписание");

        save();
        messageResultOfOperation("Структурное подразделение");

        goTo("Общее","Предприятие");
        goTo("Общее","Структур. подразделения");
        input("Поиск по списку", data);
        selectRow(0);
        assertEquals(revisionValue("Наименование").trim(), data,"СП не добавлено");
    }

    @Test(priority=2, groups = { "empty","add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        input("Наименование", "");

        save();
        messageResultOfOperation("Структурное подразделение");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=200, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        input("Поиск по списку", "Тест");
        selectRow(0);
        edit();

        input("Наименование", data = "ТестИзменениеСПНаименование" + curDate());
        input("Описание","ТестИзменениеСПОписание");

        save();
        messageResultOfOperation("Структурное подразделение");

        goTo("Общее","Предприятие");
        goTo("Общее","Структур. подразделения");
        input("Поиск по списку", data);
        selectRow(0);
        assertEquals(revisionValue("Наименование").trim(), data,"СП не изменено");
    }
    @Test(priority=201, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);
        edit();

        input("Наименование", "");

        save();
        messageResultOfOperation("Структурное подразделение");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=300, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        input("Поиск по списку", "ТестСозданиеСП");
        selectRow(0);
        data = revisionValue("Наименование");
        delete();

        //messageResultOfOperation("объект");

        goTo("Общее","Предприятие");
        goTo("Общее","Структур. подразделения");
        if(!MOWebdriver.isListEmpty()) {
            input("Поиск по списку", data);
            if(!MOWebdriver.isListEmpty())
                fail("СП не удалено!\n" + getScreenshot("fail"));
        }
    }
}
