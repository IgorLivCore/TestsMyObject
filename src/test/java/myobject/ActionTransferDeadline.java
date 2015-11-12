package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.data;
import static org.testng.Assert.*;

/**
 * Created by natalia on 28.09.15.
 */
public class ActionTransferDeadline extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception
    {
        if (isListEmpty(new String[] {"Мероприятия"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception
    {
        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        selectRow(0);
    }

    @Test(priority = 10, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        button("Перенести срок выполнения");

        legend="";
        date("Новый срок выполения","10.06.2016");
        input("Причина переноса", data = "ТестСозданиеПереносаМерПричинаПереноса" + curDate());
        input("Основание переноса", "ТестСозданиеПереносаМерОснованиеПереноса");

        save();
        messageResultOfOperation("Перенос сроков мероприятия");

        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        selectRow(0);
        legend = "Переносы срока";
        assertEquals(revisionValue("Причина переноса"), data,"Перенос сроков меропритяия не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        button("Перенести срок выполнения");

        legend="";
        input("Новый срок выполения", "");
        input("Причина переноса", "");

        save();
        messageResultOfOperation("Перенос сроков меропритяия");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        legend = "Переносы срока";
        edit();

        legend="";
        date("Новый срок выполения", "10.07.2016");
        input("Причина переноса", data = "ТестИзменениеПереносаМерПричинаПереноса" + curDate());
        input("Основание переноса", "ТестИзменениеПереносаМерОснованиеПереноса");

        save();
        messageResultOfOperation("Перенос сроков мероприятия");

        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        selectRow(0);
        legend = "Переносы срока";
        assertEquals(revisionValue("Причина переноса"), data,"Перенос сроков меропритяия не изменен");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        edit();

        legend="";
        input("Новый срок выполения", "");
        input("Причина переноса", "");

        save();
        messageResultOfOperation("Перенос сроков меропритяия");

        String[] s = checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        legend = "Переносы срока";
        data = revisionValue("Причина переноса");
        edit();
        delete();

        messageResultOfOperation("Перенос срока мероприятия");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        if (!MOWebdriver.isListEmpty()){
            selectRow(0);
            assertNotEquals(revisionValue("Причина переноса"), data, "Перенос срока мероприятия не удален");
        }
    }
}
