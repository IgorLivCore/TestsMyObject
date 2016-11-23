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
public class Action extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Персонал","Документы"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
    }

    @Test(priority = 10, groups = { "add" })
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="";
        input("Описание", "ТестСозданиеМероприятияОписание");
        input("Код", data = "ТестСозданиеМероприятияКод"+curDate());
        input("Рекомендуемая дата завершения","20.04.2016");
        select("Ответственный за выполнение",0);

        save();
        messageResultOfOperation("Мероприятие");

        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");

        selectRow(0);
        legend = "Общая информация";
        assertEquals(revisionValue("Код"), data,"Мероприятие не добавлено" + getScreenshot("fail"));
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        legend="";
        input("Описание", "");

        save();
        messageResultOfOperation("Мероприятие");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        legend="";
        input("Описание", "ТестИзменениеМероприятияОписание"+curDate());
        input("Код", data = "ТестИзменениеМерКод"+curDate());
        input("Рекомендуемая дата завершения","04.02.2016");
        select("Ответственный за выполнение", 1);

        save();
        messageResultOfOperation("Мероприятие");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");

        selectRow(0);
        legend = "Общая информация";
        assertEquals(revisionValue("Код"), data,"Мероприятие не изменено" + getScreenshot("fail"));
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);
        edit();

        legend="";
        input("Описание", "");

        save();
        messageResultOfOperation("Мероприятие");

        String[] s = checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        legend = "Общая информация";
        data = revisionValue("Код");
        delete();

        messageResultOfOperation("Мероприятие");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        if (!MOWebdriver.isListEmpty()){
            selectRow(0);
            assertNotEquals(revisionValue("Код"), data, "Мероприятие не удалено" + getScreenshot("fail"));
        }
    }
}
