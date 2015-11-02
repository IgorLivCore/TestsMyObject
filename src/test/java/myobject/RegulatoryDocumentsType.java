package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.data;
import static Base.MOWebdriver.getValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by natalia on 28.09.15.
 */
public class RegulatoryDocumentsType extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Справочники","Нормативные документы");
        goTo("Справочники","Типы документов");
    }

    @Test(priority = 10, groups = { "add" })
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="";
        input("Наименование типа документа", data = "ТестСозданиеТипаНормативногоДокумента" + curDate());

        save();
        messageResultOfOperation("Тип нормативного документа");

        goTo("Справочники","Нормативные документы");
        goTo("Справочники","Типы документов");
        selectRow(0);
        assertEquals(getValue("Наименование типа документа"), data,"Тип нормативного документа не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        operation="добавление";
        expected_result=false;
        add();

        legend="";
        input("Наименование типа документа", "");

        save();
        messageResultOfOperation("Тип нормативного документа");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        legend = "Переносы срока";
        selectRow(0);

        legend="";
        input("Наименование типа документа", data = "ТестИзменениеТипаНормативногоДокумента" + curDate());

        save();
        messageResultOfOperation("Тип нормативного документа");

        goTo("Справочники","Нормативные документы");
        goTo("Справочники","Типы документов");
        selectRow(0);
        assertEquals(getValue("Наименование типа документа"), data,"Тип нормативного документа не изменен");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);

        legend="";
        input("Наименование типа документа", "");

        save();
        messageResultOfOperation("Тип нормативного документа");

        String[] s = checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = getValue("Наименование типа документа");
        delete();

        messageResultOfOperation("Тип нормативного документа");

        goTo("Справочники","Нормативные документы");
        goTo("Справочники","Типы документов");
        if (!MOWebdriver.isListEmpty()){
            selectRow(0);
            assertNotEquals(getValue("Наименование типа документа"), data, "Тип нормативного документа не удален");
        }
    }
}
