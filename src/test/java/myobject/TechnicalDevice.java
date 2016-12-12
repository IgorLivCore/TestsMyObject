package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Arrays;
import static org.testng.Assert.*;
import static Base.MOWebdriver.*;

/**
 * Created by natalia on 07.05.15.
 *
 * ТУ должно успешно добавляться из левого меню при корректном вводе всех полей - testAdd
 * ТУ должно успешно добавляться из объекта при корректном вводе всех полей - testAddByObject
 * ТУ должно успешно сохранять изменения при корректном вводе всех полей - testEdit
 * ТУ не должно добавляться при незаполненных обязательных полях - testAddEmpty
 * ТУ не должно сохранять изменения при незаполненных обязательных полях - testEditEmpty
 * ТУ должно успешно удаляться - testDelete
 */
public class TechnicalDevice extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация","ТУ");
    }

    @Test(priority = 10, groups = { "add", "addTD" })
    public void testAdd() throws Exception{
        operation="добавление";
        add();

        legend="Данные ТУ";
        input("Наименование тех. устройства", data="ТестСозданиеТУНаименованиеТУ" + curDate());
        input("Марка тех. устройства",data="ТестСозданиеТУМаркаТУ" + curDate());
        select("Входит в состав объекта",0);
        //select("Входит в состав подобъекта",0);
        input("Заводской №", "ТестСозданиеТУЗаводской№");
        input("Регистрационный №", "ТестСозданиеТУРегистационный№");
        input("Серийный №","ТестСозданиеТУСерийный№");
        input("Гос. регистрационный знак","ТестСозданиеТУГосРегистрационныйЗнак");



        input("Нормативный срок эксплуатации (лет)","20");
        input("Год выпуска","2010");
        input("Год ввода в эксплуатацию","2011");

        switcher("Наличие предохранительного устройства", "Да");
        input("Тип предохранительного устройства", "ТестСозданиеТУТипПредохрантельногоУстройства");

        //input("Разрешенный срок эксплуатации (лет)", "ТестСозданиеТУРазрешенныйСрокЭксплуатации");
        input("Характеристика тех. устройства", "ТестСозданиеТУХарактеристикаТУ");

        legend="Технологическая схема";
        input("Технологический №","ТестСозданиеТУТехнологический№");//Технологический №+
        //input("Порядковый номер по технологической схеме","ТестСозданиеТУПорядковыйНомерПоТС");
        //switcher("Сведения подлежат подаче в РТН", "Нет");

        legend="Тип ТУ";
        select("Выберите тип ТУ из списка", 2);
        input("Вид ТУ","ТестСозданиеТУВидТУ");

        legend="Признаки опасности";
        check("2.1. Применяется для получения, использования, переработки, образовании, хранении, транспортировании, уничтожение опасных веществ");
        input("Объем, м","100");
        input("Давление, МПа","10");
        input("Dy, мм","5");
        check("2.2. Работает под давлением более 0,07 МПа или температуре нагрева воды более 115 С");
        input("Объем, м","50");
        input("Давление, МПа","100");
        check("2.3. Стационарно установленный грузоподъемный механизм, эскалатор, канатная дорога, фуникулер");
        input("Тип","ТестСозданиеТУТип");
        input("Подтип","ТестСозданиеТУПодтип");
        input("Грузоподъемность","1000");

        save();
        messageResultOfOperation("ТУ");

        goTo("Эксплуатация","ТУ");

        selectRow(0);
        legend="Данные технического устройства";
        assertEquals(revisionValue("Марка"), data,"ТУ не добавлено");
        //Thread.sleep(300);
    }

    @Test(priority = 10, groups = { "add", "addTDbyObject" })
    public void testAddByObject() throws Exception{
        operation="добавление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("ТУ");
        add();

        legend="Данные ТУ";
        input("Наименование тех. устройства", "ТестСозданиеТУНаименованиеТУ" + curDate());
        input("Марка тех. устройства", data = "ТестСозданиеТУМаркаТУ" + curDate());
        input("Заводской №", "ТестСозданиеТУЗаводской№");
        input("Регистрационный №", "ТестСозданиеТУРегистационный№");
        input("Серийный №", "ТестСозданиеТУСерийный№");
        input("Гос. регистрационный знак","ТестСозданиеТУГосРегистрационныйЗнак");
        input("Технологический № (номер по технологической схеме)","ТестСозданиеТУТехнологический№");
        input("Год выпуска","2010");
        input("Год ввода в эксплуатацию","2011");
        input("Нормативный срок эксплуатации (лет)","20");
        switcher("Наличие предохранительного устройства", "Да");
        input("Тип предохранительного устройства", "ТестСозданиеТУТипПредохрантельногоУстройства");
        input("Характеристика тех. устройства", "ТестСозданиеТУХарактеристикаТУ");

        legend="Тип ТУ";
        select("Выберите тип ТУ из списка", 2);
        input("Вид ТУ","ТестСозданиеТУВидТУ");

        legend="Признаки опасности";
        check("2.1. Применяется для получения, использования, переработки, образовании, хранении, транспортировании, уничтожение опасных веществ");
        input("Объем, м","100");
        input("Давление, МПа","10");
        input("Dy, мм", "5");
        check("2.2. Работает под давлением более 0,07 МПа или температуре нагрева воды более 115 С");
        input("Объем, м","50");
        input("Давление, МПа","100");
        check("2.3. Стационарно установленный грузоподъемный механизм, эскалатор, канатная дорога, фуникулер");
        input("Тип","ТестСозданиеТУТип");
        input("Подтип","ТестСозданиеТУПодтип");
        input("Грузоподъемность", "1000");

        save();
        messageResultOfOperation("ТУ");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("ТУ");

        selectRow(0);
        legend="Данные технического устройства";
        assertEquals(revisionValue("Марка"), data,"ТУ не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmpty() throws Exception{
        expected_result=false;
        add();

        legend="Данные ТУ";
        input("Наименование тех. устройства", "");
        unselect("Входит в состав объекта");
        input("Год ввода в эксплуатацию", "");
        input("Нормативный срок эксплуатации (лет)","");
        switcher("Наличие предохранительного устройства", "Да");
        input("Тип предохранительного устройства", "");

        save();
        messageResultOfOperation("ТУ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        selectRow(0);
        edit();

        legend="Данные ТУ";
        input("Наименование тех. устройства", "ТестИзменениеТУНаименованиеТУ" + curDate());
        input("Марка тех. устройства",data="ТестИзменениеТУМаркаТУ" + curDate());
        select("Входит в состав объекта",2);
        input("Заводской №", "ТестИзменениеТУЗаводской№");
        input("Регистрационный №", "ТестИзменениеТУРегистационный№");
        input("Серийный №","ТестИзменениеТУСерийный№");
        input("Гос. регистрационный знак","ТестИзменениеТУГосРегистрационныйЗнак");
        input("Технологический № (номер по технологической схеме)","ТестИзменениеТУТехнологический№");
        input("Год выпуска","2011");
        input("Год ввода в эксплуатацию","2012");
        input("Нормативный срок эксплуатации (лет)","25");
        switcher("Наличие предохранительного устройства", "Да");
        input("Тип предохранительного устройства", "ТестИзменениеТУТипПредохрантельногоУстройства");
        input("Характеристика тех. устройства", "ТестИзменениеТУХарактеристикаТУ");

        legend="Тип ТУ";
        select("Выберите тип ТУ из списка", 3);
        input("Вид ТУ","ТестИзменениеТУВидТУ");

        legend="Признаки опасности";
        check("2.1. Применяется для получения, использования, переработки, образовании, хранении, транспортировании, уничтожение опасных веществ");
        input("Объем, м","200");
        input("Давление, МПа","15");
        input("Dy, мм","10");
        check("2.2. Работает под давлением более 0,07 МПа или температуре нагрева воды более 115 С");
        input("Объем, м","75");
        input("Давление, МПа","85");
        check("2.3. Стационарно установленный грузоподъемный механизм, эскалатор, канатная дорога, фуникулер");
        input("Тип","ТестИзменениеТУТип");
        input("Подтип","ТестИзменениеТУПодтип");
        input("Грузоподъемность","750");

        save();
        messageResultOfOperation("ТУ");

        goTo("Эксплуатация","ТУ");

        selectRow(0);
        legend="Данные технического устройства";
        assertEquals(revisionValue("Марка"), data,"ТУ не изменено");
    }

    @Test(priority = 211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception{
        expected_result=false;
        selectRow(0);
        edit();

        legend="Данные ТУ";
        input("Наименование тех. устройства", "");
        unselect("Входит в состав объекта");
        input("Год ввода в эксплуатацию", "");
        input("Нормативный срок эксплуатации (лет)","");
        switcher("Наличие предохранительного устройства", "Да");
        input("Тип предохранительного устройства", "");

        save();
        messageResultOfOperation("ТУ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("Марка");
        delete();

        //messageResultOfOperation("ТУ");

        goTo("Эксплуатация","ТУ");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Марка"), data, "ТУ не удалено.\nExpected: " + data + "\nActual: " + revisionValue("Марка"));
        }
    }
}
