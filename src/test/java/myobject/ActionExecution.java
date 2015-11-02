package myobject;

import Base.MOTestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static Base.MOWebdriver.*;
import static org.testng.Assert.*;

/**
 * Created by natalia on 28.09.15.
 */
public class ActionExecution extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Мероприятия","Планы"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Предприятие");
        goTo("Документы");
        selectRow(0);
        goTo("Мероприятия");
        selectRow(0);
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";

        button("Внести изменения");
        select("Включение в план",0);
        select("Ответственный",2);
        select("Cтатус",1);
        date("Начать","10.01.2016");
        date("Завершить","10.05.2016");
        date("Фактически завершено","15.04.2016");

        save();
        messageResultOfOperation("Выполнение мероприятия");
    }
}
