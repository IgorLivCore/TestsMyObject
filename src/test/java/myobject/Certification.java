package myobject;

import static org.testng.Assert.assertEquals;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.*;
import static Base.MOWebdriver.*;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;

/**
 * Created by natalia on 04.02.15.
 *
 * TODO Аттестация должна успешно добавляться из левого меню при вводе всех данных корректно
 * TODO Аттестация должна успешно добавляться из сотрудника при вводе всех данных корректно
 * TODO Аттестация должна успешно изменяться при вводе всех данных корректно
 * TODO Аттестация не должна добавляться при незаполненных обязательных полях
 * TODO Аттестация не должна сохранять изменения при незаполненных обязательных полях
 */
public class Certification extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Персонал"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Персонал");
        goTo("Общее","Аттестации");
    }

    @Test(priority=2, groups = { "add" })
    public void testAddPlan() throws Exception
    {
        operation = "добавление";
        add();

        legend="";
        switcher("", "План");
        select("Сотрудник", 1);
        date("Дата плановая", "21.09.2015");
        select("Причина проверки", "Первичная");
        select("Области аттестации (можно указать несколько)", "А.1.");

        save();
        messageResultOfOperation("плановая аттестация");
    }

    @Test(priority=1, groups = { "add" })
    public void testAddFact() throws Exception
    {
        operation = "добавление";
        add();

        legend="";
        switcher("", "Факт");
        select("Сотрудник", 0);
        date("Дата протокола", "20.09.2015");
        input("Номер протокола", data="ТестСозданиеФактАттестацииНомерПротокола" + curDate());
        input("№ удостоверения", "469270");
        date("Срок истечения (действительно до)", "20.09.2021");
        select("Причина проверки", "Первичная");
        select("Области аттестации (можно указать несколько)", "Б.1.1.");

        save();
        messageResultOfOperation("факт аттестация");

        goTo("Общее", "Аттестации");
        goTo("Пройденные");
        selectRow(0);
        assertEquals(getValue("Номер протокола"), data, "Фактическая аттестация не добавлена");
    }

    @Test(priority=2,  dependsOnMethods = "testAddPlan", groups = { "edit" })
    public void testPlanToFact() throws Exception
    {
        selectRow(0);

        legend="";
        switcher("", "Факт");
        date("Дата протокола", "20.09.2015");
        input("Номер протокола", data = "ТестПланВФактАттестацииНомерПротокола" + curDate());
        input("№ удостоверения", "469270");
        date("Срок истечения (действительно до)", "20.09.2021");

        save();
        messageResultOfOperation("факт аттестация");

        goTo("Общее", "Аттестации");
        goTo("Пройденные");
        selectRow(0);
        assertEquals(getValue("Номер протокола"), data, "Фактическая аттестация не добавлена");
    }

    @Test(priority=2, groups = { "add" })
    public void testEditPlan() throws Exception
    {
        operation = "изменение";
        selectRow(0);

        legend="";
        switcher("", "План");
        select("Сотрудник", 1);
        date("Дата плановая", "22.09.2015");
        select("Причина проверки", "Внеочередная");
        select("Области аттестации (можно указать несколько)", "Б.1.2.");

        save();
        messageResultOfOperation("плановая аттестация");
    }

    @Test(priority=2,groups = { "add" })
    public void testEditFact() throws Exception
    {
        operation = "изменение";
        goTo("Пройденные");
        selectRow(0);

        legend="";
        switcher("", "Факт");
        select("Сотрудник", 0);
        date("Дата протокола", "21.09.2015");
        input("Номер протокола", data="ТестИзменениеФактАттестацииНомерПротокола" + curDate());
        input("№ удостоверения", "467568");
        date("Срок истечения (действительно до)", "21.09.2021");
        select("Причина проверки", "Периодическая");
        select("Области аттестации (можно указать несколько)", "Б.1.3.");

        save();
        messageResultOfOperation("факт аттестация");

        goTo("Общее", "Аттестации");
        goTo("Пройденные");
        selectRow(0);
        assertEquals(getValue("Номер протокола"), data, "Фактическая аттестация не Изменена");
    }

    @Test(priority=330, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        goTo("Пройденные");
        selectRow(0);
        data = getValue("Номер протокола");
        delete();

        //messageResultOfOperation("аттестация");

        goTo("Общее", "Аттестации");
        goTo("Пройденные");
        if(!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(getValue("Номер протокола"), data, "Аттестация не удалена!");
        }
    }
}
