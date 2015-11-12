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
public class ProtocolViolation extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Персонал", "Протоколы ВП" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Производственный контроль","Протоколы ВП");
        selectRow(0);
        goTo("Нарушения");
    }

    @Test(priority = 10, groups = { "add"})
    public void testAdd() throws Exception {
        operation="добавление";
        add();

        legend="Данные нарушения";
        input("Характер нарушения", "ТестСозданиеНарушенияПротоколаХарактерНарушения");
        date("Срок устранения нарушения", "21.12.2015");
        select("Ответственный за устранение нарушения", 0);
        input("Причины невыполнения в срок", data = "ТестСозданиеНарушенияПротоколаПричиныНевыполненияВСрок" + curDate());
        date("Перенос срока", "25.12.2015");
        input("Основание переноса срока", "ТестСозданиеНарушенияПротоколаОснованиеПереносаСрока");
        input("Предложения, внесенные службой производственного контроля руководству предприятий по обеспечению промышленой безопасности", "ТестСозданиеНарушенияПротоколаПредложения");
        switcher("Сведения подлежат подаче в РТН", "Да");

        legend="Нормативные документы, требования которых были нарушены";
        button("Добавить");
        select("Нормативный документ", 0);
        input("Номер статьи документа", "ТестСозданиеНарушенияПротоколаНомерСтатьи");
        input("Текст статьи документа", "ТестСозданиеНарушенияПротоколаТекстСтатьи");

        legend="Сотрудники, привлеченные к ответственности";
        button("Добавить");
        select("Сотрудник", 1);

        legend="Приостановленные работы";
        button("Добавить");
        input("Наименование приостановленной работы", "ТестСозданиеНарушенияПротоколаНаименованиеРаботы");

        save();
        messageResultOfOperation("Нарушение протокола ВП");

        goTo("Производственный контроль", "Протоколы ВП");
        selectRow(0);
        goTo("Нарушения");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("Причины невыполнения в срок"), data,"Нарушение протокола не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception {
        operation="добавление";
        expected_result=false;
        add();

        legend="Данные нарушения";
        input("Характер нарушения", "");
        input("Срок устранения нарушения", "");
        unselect("Ответственный за устранение нарушения");

        legend="Нормативные документы, требования которых были нарушены";
        button("Добавить");
        unselect("Нормативный документ");
        input("Номер статьи документа", "");

        legend="Сотрудники, привлеченные к ответственности";
        button("Добавить");
        unselect("Сотрудник");

        legend="Приостановленные работы";
        button("Добавить");
        input("Наименование приостановленной работы", "");

        save();
        messageResultOfOperation("Протоколы ВП");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception {
        operation="изменение";
        selectRow(0);
        edit();

        legend="Данные нарушения";
        input("Характер нарушения", "ТестИзменениеНарушенияПротоколаХарактерНарушения");
        date("Срок устранения нарушения", "22.11.2015");
        select("Ответственный за устранение нарушения", 1);
        input("Причины невыполнения в срок", data = "ТестИзменениеНарушенияПротоколаПричиныНевыполненияВСрок" + curDate());
        date("Перенос срока", "27.11.2015");
        input("Основание переноса срока", "ТестИзменениеНарушенияПротоколаОснованиеПереносаСрока");
        input("Предложения, внесенные службой производственного контроля руководству предприятий по обеспечению промышленой безопасности", "ТестИзменениеНарушенияПротоколаПредложения");
        switcher("Сведения подлежат подаче в РТН", "Да");

        legend="Нормативные документы, требования которых были нарушены";
        select("Нормативный документ", 1);
        input("Номер статьи документа", "ТестИзменениеНарушенияПротоколаНомерСтатьи");
        input("Текст статьи документа", "ТестИзменениеНарушенияПротоколаТекстСтатьи");

        legend="Сотрудники, привлеченные к ответственности";
        select("Сотрудник", 0);

        legend="Приостановленные работы";
        input("Наименование приостановленной работы", "ТестИзменениеНарушенияПротоколаНаименованиеРаботы");

        save();
        messageResultOfOperation("Нарушение протокола ВП");

        goTo("Производственный контроль", "Протоколы ВП");
        selectRow(0);
        goTo("Нарушения");

        selectRow(0);
        legend="";
        assertEquals(revisionValue("Причины невыполнения в срок"), data,"Нарушение протокола не изменено");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        operation="изменение";
        expected_result=false;
        selectRow(0);
        edit();

        legend="Данные нарушения";
        input("Характер нарушения", "");
        input("Срок устранения нарушения", "");
        unselect("Ответственный за устранение нарушения");

        legend="Нормативные документы, требования которых были нарушены";
        unselect("Нормативный документ");
        input("Номер статьи документа", "");

        legend="Сотрудники, привлеченные к ответственности";
        unselect("Сотрудник");

        legend="Приостановленные работы";
        input("Наименование приостановленной работы", "");

        save();
        messageResultOfOperation("Нарушение протокола РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("Причины невыполнения в срок");
        delete();

        //messageResultOfOperation("Нарушение протокола");

        goTo("Производственный контроль","Протоколы ВП");
        selectRow(0);
        goTo("Нарушения");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Причины невыполнения в срок"), data, "Нарушение протокола не удалено");
        }
    }
}
