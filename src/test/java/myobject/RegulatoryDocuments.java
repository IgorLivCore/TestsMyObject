package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.data;
import static org.testng.Assert.*;

/**
 * Created by natalia on 28.09.15.
 */
public class RegulatoryDocuments extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Справочники","Нормативные документы");
    }

    @Test(priority = 10, groups = { "add" })
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="";
        select("Тип документа", 5);
        input("Наименование документа", data = "ТестСозданиеНормативногоДокументаНаименованиеДокумента" + curDate());
        input("Код документа", "ТестСозданиеНормативногоДокументаКод");
        select("Статус документа", "Актуальный");
        select("Источник", "Внутренний");

        save();
        messageResultOfOperation("Нормативный документ");

        goTo("Справочники","Нормативные документы");
        selectRow(0);
        assertEquals(getValue("Наименование документа"), data,"Нормативный документ не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        legend="";
        unselect("Тип документа");
        input("Наименование документа", "");

        save();
        messageResultOfOperation("Нормативный документ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        legend = "Переносы срока";
        selectRow(0);

        legend="";
        select("Тип документа", 5);
        input("Наименование документа", data = "ТестИзменениеНормативногоДокументаНаименованиеДокумента" + curDate());
        input("Код документа", "ТестИзменениеНормативногоДокументаКод");
        select("Статус документа", "Не актуальный");
        select("Источник", "Внешний");

        save();
        messageResultOfOperation("Нормативный документ");

        goTo("Справочники", "Нормативные документы");
        selectRow(0);
        assertEquals(getValue("Наименование документа"), data,"Нормативный документ не изменен");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);

        legend="";
        unselect("Тип документа");
        input("Наименование документа", "");

        save();
        messageResultOfOperation("Нормативный документ");

        String[] s = checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = getValue("Наименование документа");
        delete();

        messageResultOfOperation("Нормативный документ");

        goTo("Справочники", "Нормативные документы");
        if (!MOWebdriver.isListEmpty()){
            selectRow(0);
            assertNotEquals(getValue("Наименование документа"), data, "Нормативный документ не удален");
        }
    }
}
