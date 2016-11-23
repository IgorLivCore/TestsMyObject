package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.*;

import static Base.MOWebdriver.*;
import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * Created by natalia on 07.05.15.
 *
 * ЗиС должно успешно добавляться из левого меню при корректном вводе всех полей - testAdd
 * ЗиС должно успешно добавляться из объекта при корректном вводе всех полей - testAddByObject
 * ЗиС должно успешно сохранять изменения при корректном вводе всех полей - testEdit
 * ЗиС не должно добавляться при незаполненных обязательных полях - testAddEmpty
 * ЗиС не должно сохранять изменения при незаполненных обязательных полях - testEditEmpty
 * ЗиС должно успешно удаляться - testDelete
 */
public class BuildAndConstruction extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception
    {
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @Test(priority = 10, groups = { "add", "addBaC" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация","ЗиС");
        add();

        legend="Общая информация";
        input("Название ЗиС", data = "ТестСозданиеЗиСНазваниеЗиС"+curDate());


        input("Код ЗиС",data = "ТестСозданиеЗиСКодЗиС"+curDate());
        input("Год ввода в эксплуатацию", "2011");
    /*    input("Описание ЗиС","ТестСозданиеЗиСОписание");

        legend = "Входит в состав объектов";
        button("Добавить");
        select("Объект",0);
*/
        save();
        messageResultOfOperation("ЗиС");

        goTo("Эксплуатация","ЗиС");

        selectRow(0);
        legend="Данные ЗиС";
        assertEquals(revisionValue("Код ЗиС"), data,"ЗиС не добавлено");
    }

    @Test(priority = 10, groups = { "add", "addBaC","addBaCbyObject" })
    public void testAddByObject() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("ЗиС");
        add();

        legend="Общая информация";
        input("Название ЗиС", "ТестСозданиеЗиСНазвание" + curDate());
        input("Код ЗиС", data = "ТестСозданиеЗиСКод" + curDate());
        input("Год ввода в эксплуатацию", "2000");
        input("Описание ЗиС", "ТестСозданиеЗиСОписание");

        save();
        messageResultOfOperation("ЗиС");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("ЗиС");

        selectRow(0);
        legend="Данные ЗиС";
        assertEquals(revisionValue("Код ЗиС"), data, "ЗиС не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        goTo("Эксплуатация","ЗиС");
        add();

        legend="Общая информация";
        input("Название ЗиС", "");
        input("Год ввода в эксплуатацию", "");

        save();
        messageResultOfOperation("ЗиС");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        goTo("Эксплуатация","ЗиС");
        selectRow(0);
        edit();

        legend="Общая информация";
        input("Название ЗиС", "ТестИзменениеЗиСНазваниеЗиС" + curDate());
        //select("Входит в состав объекта",0);
        input("Код ЗиС", data = "ТестИзменениеЗиСКодЗиС" + curDate());
        input("Год ввода в эксплуатацию", "2012");
        input("Описание ЗиС", "ТестИзменениеЗиСОписание");

        legend = "Входит в состав объектов";
        select("Объект", 0);

        save();
        messageResultOfOperation("Зис");

        goTo("Эксплуатация","ЗиС");

        selectRow(0);
        legend="Данные ЗиС";
        assertEquals(revisionValue("Код ЗиС"), data, "ЗиС не изменено");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        goTo("Эксплуатация","ЗиС");
        selectRow(0);
        edit();

        legend = "Общая информация";
        input("Название ЗиС", "");
        input("Год ввода в эксплуатацию", "");

        legend = "Входит в состав объектов";
        unselect("Объект");

        save();
        messageResultOfOperation("ЗиС");

        String[] s = checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        goTo("Эксплуатация","ЗиС");
        selectRow(0);
        legend="Данные ЗиС";
        data = revisionValue("Код ЗиС");
        delete();

        //messageResultOfOperation("ЗиС");

        goTo("Эксплуатация","ЗиС");
        if (!MOWebdriver.isListEmpty()){
            selectRow(0);
            assertNotEquals(revisionValue("Код ЗиС"), data, "ЗиС не удалено");
        }
    }
}
