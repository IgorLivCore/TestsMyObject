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
 * Created by natalia on 10.04.15.
 */
public class PLA extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("ПЛА");
    }

    @Test(priority=10, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        input("Наименование аварии",data="ТестСозданиеПЛАНаменованиеАварии"+curDate());
        select("Уровень аварии","А");
        input("Место аварии", "ТестСозданиеПЛАМестоАварии");
        input("Опознавательные признаки","ТестСозданиеПЛАОпознавательныеПризнаки");
        input("Оптимальные способы противоаварийной защиты","ТестСозданиеПЛАОптимальныеСпособыЗащиты");
        input("Технические средства (системы) противоаварийной защиты, применяемые при подавлении и локализации аварии","ТестСозданиеПЛАТехническиеСредства");
        input("Порядок действий","ТестСозданиеПЛАПорядокДействий");
        select("Ответственный руководитель работ по локализации и ликвидации аварии", 0);

        input("Комментарий к оценке готовности","ТестСозданиеПЛАКомментарийКОценкеГотовности");

        save();
        messageResultOfOperation("ПЛА");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("ПЛА");
        selectRow(0);
        assertEquals(getValue("Наименование аварии"), data,"ПЛА не добавлен");
    }

    @Test(priority=20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        input("Наименование аварии", "");
        unselect("Уровень аварии");
        input("Место аварии", "");
        input("Опознавательные признаки", "");
        input("Оптимальные способы противоаварийной защиты", "");
        input("Технические средства (системы) противоаварийной защиты, применяемые при подавлении и локализации аварии", "");
        input("Порядок действий", "");
        select("Ответственный руководитель работ по локализации и ликвидации аварии", "");

        save();
        messageResultOfOperation("ПЛА");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=210, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);

        input("Наименование аварии",data="ТестИзменениеПЛАНаменованиеАварии"+curDate());
        select("Уровень аварии", "Б");
        input("Место аварии", "ТестИзменениеПЛАМестоАварии");
        input("Опознавательные признаки", "ТестИзменениеПЛАОпознавательныеПризнаки");
        input("Оптимальные способы противоаварийной защиты", "ТестИзменениеПЛАОптимальныеСпособыЗащиты");
        input("Технические средства (системы) противоаварийной защиты, применяемые при подавлении и локализации аварии", "ТестИзменениеПЛАТехническиеСредства");
        input("Порядок действий","ТестИзменениеПЛАПорядокДействий");
        select("Ответственный руководитель работ по локализации и ликвидации аварии", 0);

        input("Комментарий к оценке готовности","ТестИзменениеПЛАКомментарийКОценкеГотовности");

        save();
        messageResultOfOperation("ПЛА");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("ПЛА");
        selectRow(0);
        assertEquals(getValue("Наименование аварии"), data, "ПЛА не изменен");
    }

    @Test(priority=211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);

        input("Наименование аварии", "");
        unselect("Уровень аварии");
        input("Место аварии", "");
        input("Опознавательные признаки", "");
        input("Оптимальные способы противоаварийной защиты", "");
        input("Технические средства (системы) противоаварийной защиты, применяемые при подавлении и локализации аварии", "");
        input("Порядок действий", "");
        unselect("Ответственный руководитель работ по локализации и ликвидации аварии");

        save();
        messageResultOfOperation("ПЛА");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = getValue("Наименование аварии");
        delete();//удаляем

        messageResultOfOperation("ПЛА");

        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("ПЛА");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals( getValue("Наименование аварии"), data, "ПЛА не удалено");
        }
    }
}
