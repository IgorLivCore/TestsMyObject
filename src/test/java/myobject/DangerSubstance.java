package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.*;
import static Base.MOWebdriver.*;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * Created by natalia on 13.02.15.
 *
 * ОВ должно успешно добавляться из объекта при корректном вводе всех полей - testAddByObject
 * ОВ должно успешно добавляться из ТУ при корректном вводе всех полей - testAddByObjectByTD
 * ОВ должно успешно сохранять изменения при корректном вводе всех полей - testEditByObject
 * ОВ должно успешно сохранять изменения при корректном вводе всех полей - testEditByTD
 * ОВ в радиусе 500м должно успешно добавляться при корректном вводе всех полей - testAddForeign500m
 * ОВ не должно добавляться при незаполненных обязательных полях - testAddEmpty
 * ОВ не должно сохранять изменения при незаполненных обязательных полях - testEditEmpty
 * ОВ должно успешно удаляться из объекта - testDeleteByObject
 * ОВ должно успешно удаляться из ТУ- testDeleteByTd
 * ОВ 500м должно успешно удаляться - testDeleteForeign500m
 *
 * TODO ОВ 500м не должно сохраняться при незаполненных обязательных полях
 * TODO ОВ 500м должно сохранять изменения при корректном вовде обязательных полей
 * TODO ОВ 500м не должно сохранять изменения при незаполненных обязательных полях
 */
public class DangerSubstance extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты","ТУ"}))
            fail("Не хватает данных для проведения теста!");
    }


    @Test(priority=10, groups = { "add", "addDSByObject"})
    public void testAddByObject() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        add();

        legend="";
        select("Наименование опасного вещества", "Аммиак");
        input("Характеристика опасного вещества", data = "ТестСозданиеОпасногоВеществаХарактеристика" + curDate());
        select("Техническое устройство", 0);
        input("Количество (тонны)", "300");
        input("Комментарий","ТестСозданиеОпасногоВеществаКомментарий");

        save();
        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        selectRow(0);
        assertEquals(getValue("Характеристика опасного вещества"), data,"ОВ не добавлено");
    }

    @Test(priority=10, groups = { "add", "addDSByTD" })
    public void testAddByTD() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        add();

        legend="";
        select("Наименование опасного вещества", "Хлор");
        input("Характеристика опасного вещества", data = "ТестСозданиеОпасногоВеществаХарактеристика" + curDate());
        input("Количество (тонны)", "200");
        input("Комментарий","ТестСозданиеОпасногоВеществаКомментарий");

        save();
        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        selectRow(0);
        assertEquals(getValue("Характеристика опасного вещества"), data,"ОВ не добавлено");
    }

    @Test(priority=10, groups = { "add" })
    public void testAddForeign500m() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества сторонних предприятий в радиусе 500м");
        add();

        legend="";
        select("Наименование опасного вещества", "Алкилы");
        input("Характеристика опасного вещества", data = "ТестСозданиеОпасногоВещества500мХарактеристика" + curDate());
        input("Количество (тонны)","400");
        input("Комментарий","ТестСозданиеОпасногоВещества500мКомментарий");

        save();
        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества сторонних предприятий в радиусе 500м");
        selectRow(0);
        assertEquals(getValue("Характеристика опасного вещества"), data,"ОВ не добавлено");
    }

    @Test(priority=20, groups = { "add","empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        add();

        legend="";
        unselect("Наименование опасного вещества");
        input("Количество (тонны)", "");

        save();
        messageResultOfOperation("ОВ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=210, groups = { "edit" })
    public void testEditByObject() throws Exception
    {
        operation="изменение";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        selectRow(0);

        legend="";
        select("Наименование опасного вещества", "Хлор");
        input("Характеристика опасного вещества", data = "ТестИзменениеОпасногоВеществаХарактеристика" + curDate());
        select("Техническое устройство", 0);
        input("Количество (тонны)", "200");

        save();
        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        selectRow(0);
        assertEquals(getValue("Характеристика опасного вещества"), data, "ОВ не изменено");
    }

    @Test(priority=210, groups = { "edit" })
    public void testEditByTD() throws Exception
    {
        operation="изменение";
        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        selectRow(0);

        legend="";
        select("Наименование опасного вещества", "Хлор");
        input("Характеристика опасного вещества", data = "ТестИзменениеОпасногоВеществаХарактеристика" + curDate());
        input("Количество (тонны)", "200");

        save();
        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        selectRow(0);
        assertEquals(getValue("Характеристика опасного вещества"), data,"ОВ не изменено");
    }

    @Test(priority=211, groups = { "empty","edit" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        selectRow(0);

        legend="";
        unselect("Наименование опасного вещества");
        input("Количество (тонны)","");

        save();
        messageResultOfOperation("ОВ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=310, groups = { "delete" })
    public void testDeleteByObject() throws Exception
    {
        operation="удаление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        selectRow(0);
        data = getValue("Характеристика опасного вещества");
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        selectRow(0);
        delete();

        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Характеристика опасного вещества"), data, "ОВ не удалено");
        }
    }

    @Test(priority=310, groups = { "delete" })
    public void testDeleteByTD() throws Exception
    {
        operation="удаление";
        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        selectRow(0);
        data = getValue("Характеристика опасного вещества");

        delete();

        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","ТУ");
        selectRow(0);
        goTo("Опасные вещества");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Характеристика опасного вещества"), data, "ОВ не удалено");
        }
    }

    @Test(priority=310, groups = { "delete" })
    public void testDeleteForeign500m() throws Exception
    {
        operation="удаление";
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества сторонних предприятий в радиусе 500м");
        selectRow(0);
        data = getValue("Характеристика опасного вещества");
        delete();

        messageResultOfOperation("опасные вещества");

        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Опасные вещества сторонних предприятий в радиусе 500м");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Характеристика опасного вещества"), data, "ОВ не удалено");
        }
    }
}
