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
 * Created by natalia on 16.02.15.
 *
 * TODO ************* Работает (с оговорками) *************
 * TODO Сотрудник должен успешно добавляться при корректном вводе всех полей - testAdd (работает только с обязательными полями)
 * TODO Сотрудник должен успешно сохранять изменения при корректном вводе всех полей - testEdit
 * TODO Сотрудник не должен добавляться при незаполненных обязательных полях
 * TODO Сотрудник не должен сохранять изменения при незаполненных обязательных полях
 * Сотрудник должен делаться неактивным - testDeactivate
 * TODO Сотрудник должен успешно удаляться при отсутствии связей
 * TODO Сотрудник не должен удаляться при наличии связей (при существовании - заглушка)
 */
public class Employee extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Должности" }))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Общее","Персонал");
    }


    @Test(priority=6, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        legend="Общие сведения";
        input("Фамилия","ТестСозданиеРаботникаФамилия");
        input("Имя","ТестСозданиеРаботникаИмя");
        //input("Отчество","ТестСозданиеРаботникаФамилия");
        input("Образование",data="ТестСозданиеРаботникаОбразование" + curDate());
        //input("Дата рождения","18.09.1975");
        //input("№ телефона", "80123456789");
        select("Должность", 2);//
        //input("Дата последней аттестации (заполняется для подачи сведений в РТН)","21.03.2013");
        //switcher("Статус", "Активен");

        legend="Участие в производственном контроле ПБ";
        button("Добавить ответственность");
        select("Тип ответственности", 3);
        input("Зона ответственности", "ТестСозданиеРаботникаЗонаОтветственности");

        legend="Стаж работы";
        input("Наименование компании","ТестСозданиеРаботникаКомпания");///
        input("Должность","ТестСозданиеРаботникаДолжность");///
        date("Дата начала работы", "01.01.2010");//
        date("Дата окончания работы", "01.01.2011");//

        waitForPageLoad();
        //TimeUnit.SECONDS.sleep(2);//ждем секунду
        save();

        messageResultOfOperation("сотрудник");

        goTo("Общее", "Персонал");
        selectRow(0);
        legend="Данные сотрудника";
        assertEquals(revisionValue("Образование"), data, "Сотрудник не добавлен");
    }

    @Test(priority=6, groups = { "add", "empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        legend="Общие сведения";
        input("Фамилия", "");
        input("Имя", "");
        input("Образование", "");
        unselect("Должность");

        legend="Стаж работы";
        input("Наименование компании", "");
        input("Должность", "");
        input("Дата начала работы", "");
        input("Дата окончания работы", "");
        waitForPageLoad();

        save();
        messageResultOfOperation("Сотрудник");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=200, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        legend="Общие сведения";
        input("Фамилия", "ТестИзменениеРаботникаФамилия");
        input("Имя","ТестИзменениеРаботникаИмя");
        input("Отчество","ТестИзменениеРаботникаОтчество");
        input("Образование", data = "ТестИзменениеРаботникаОбразование" + curDate());
        input("Дата рождения","08.10.1983");
        input("№ телефона", "45698523");
        select("Должность", 1);

        legend="Участие в производственном контроле ПБ";
        select("Тип ответственности", 1);
        input("Зона ответственности", "ТестИзменениеРаботникаЗонаОтветственности");

        legend="Стаж работы";
        input("Наименование компании", "ТестИзменениеРаботникаКомпания");
        input("Должность","ТестИзменениеРаботникаДолжность");
        date("Дата начала работы", "11.11.2010");
        date("Дата окончания работы", "11.11.2011");
        save();

        messageResultOfOperation("сотрудник");

        goTo("Общее", "Персонал");
        selectRow(0);
        legend="Данные сотрудника";
        assertEquals(revisionValue("Образование"), data, "Сотрудник не изменен");
    }

    @Test(priority=6, groups = { "add", "empty" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);
        edit();

        legend="Общие сведения";
        input("Фамилия", "");
        input("Имя", "");
        input("Образование","");
        unselect("Должность");

        legend="Участие в производственном контроле ПБ";
        unselect("Тип ответственности");
        input("Зона ответственности", "");

        legend="Стаж работы";
        input("Наименование компании", "");
        input("Должность", "");
        input("Дата начала работы", "");
        input("Дата окончания работы", "");

        save();
        messageResultOfOperation("Сотрудник");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=300)
    public void testDeactivate() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        legend="Общие сведения";
        switcher("Статус","Неактивен");
        input("Причина деактивации сотрудника",data="ТестУдалениеСотрудникаПричина"+curDate());
        
        save();
        messageResultOfOperation("сотрудник");

        goTo("Общее","Персонал");
        selectRow(0);
        legend="Данные сотрудника";
        assertEquals(revisionValue("Статус"),"Неактивен  (" + data + ")", "Сотрудник не деактивирован");
    }

    @Test(priority=301)
    public void testActivate() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        legend="Общие сведения";
        switcher("Статус","Активен");

        save();
        messageResultOfOperation("сотрудник");

        goTo("Общее","Персонал");
        selectRow(0);
        legend="Данные сотрудника";
        assertEquals(revisionValue("Статус"),"Активен", "Сотрудник не активирован");
    }

    @Test(priority=330, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("Образование");

        delete();//удаляем
        //messageResultOfOperation("сотрудник");

        goTo("Общее", "Персонал");
        System.out.print(legend);
        if(!MOWebdriver.isListEmpty() && legend != "привязан") {
            selectRow(0);//заходим в первый
            assertNotEquals(revisionValue("Образование"), data, "Сотрудник не удален!");
        }/*обратная проверка, что элемент есть и не удален*/
        else {
            //сначала нажать закрыть, потом уже
            selectRow(0);
        }
    }
}
