package myobject;

import Base.MOTestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Arrays;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import static Base.MOWebdriver.*;

/**
 * Created by natalia on 02.04.15.
 *
 * Филиал должен успешно добавляться при корректном заполнении всех полей
 * Филиал должен успешно сохранять изменения ри корректнмо заполнении всех полей
 * Филиал не должен добавляться при незаполненных обязательных полях
 * Филиал не должен сохранять изменения при незаполненных обязательных полях
 * TODO В аккаунт нового филиала можно зайти
 * TODO При добавлении филиала количество пользователей головной компании увеличивается на 1
 * TODO При изменении реквизитов (ИНН, ОГРН, ОКОГУ, ОКПО) головной компании должны поменяться реквизиты у филиала
 */
public class Filial extends MOTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Инциденты","Несчастные случаи");
    }

    @Test(priority=5, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        expected_result=true;
        goTo("Общее","Предприятие");
        goTo("Филиалы");
        add();

        legend="Данные учетной записи администратора филиала";
        input("E-mail","testF"+curDate()+"@testF.testF");
        input("Пароль","testF");
        input("Повтор пароля","testF");

        legend="Филиал";
        input("Полное наименование",data="ТестСозданиеФилиалаПолноеНаименование"+curDate());
        input("Сокращенное наименование","ТСФПН"+curDate());

        legend="";
        select("Территориальный орган Ростехнадзора",3);
        input("Почтовый индекс","456321");
        input("Местонахождение (юридический адрес) предприятия", "ТестСозданиеФилиалаСтранаГородУлица", "10", "20");
        input("КЛАДР","4569870321");
        input("Код ОКАТО","12345678910");
        input("КПП","123456789");
        input("Руководитель (ФИО)","ТестСозданиеФилиалаРуководитель");
        input("Должность руководителя", "ТестСозданиеФилиалаДолжностьРуководителя");
        input("Телефон","7-123-4567890");
        input("Факс", "7-123-4567890");

        save();

        messageResultOfOperation("филиал");

        goTo("Общее","Предприятие");
        goTo("Филиалы");
        selectRow(0);
        legend="Филиал";
        assertEquals(getValue("Полное наименование"),data,"Филиал не создан");
    }

    @Test(priority=8, groups = { "empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        goTo("Общее","Предприятие");
        goTo("Филиалы");
        add();

        legend="Данные учетной записи администратора филиала";
        input("E-mail","");
        input("Пароль","");
        input("Повтор пароля","");

        legend="Филиал";
        input("Полное наименование","");
        input("Сокращенное наименование","");

        legend="";
        input("Почтовый индекс","");
        input("Местонахождение (юридический адрес) предприятия", "", "", "");
        input("Код ОКАТО","");
        input("Телефон","--");

        save();

        messageResultOfOperation("филиал");

        String[] s=checkEmptyFields();
        assertTrue(s.length==0,"Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length==0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=200, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        expected_result=true;
        goTo("Общее","Предприятие");
        goTo("Филиалы");
        selectRow(0);

        legend="Филиал";
        input("Полное наименование",data="ТестИзменениеФилиалаПолноеНаименование"+curDate());
        input("Сокращенное наименование","ТИФПН"+curDate());

        legend="";
        select("Территориальный орган Ростехнадзора",7);
        input("Почтовый индекс","456321");
        input("Местонахождение (юридический адрес) предприятия", "ТестИзменениеФилиалаСтранаГородУлица", "10", "20");
        input("КЛАДР","4569870321");
        input("Код ОКАТО","12345678910");
        input("КПП","123456789");
        input("Руководитель (ФИО)","ТестИзменениеФилиалаРуководитель");
        input("Должность руководителя", "ТестИзменениеФилиалаДолжностьРуководителя");
        input("Телефон","7-123-4567890");
        input("Факс", "7-123-4567890");
        input("Электронная почта","testF"+curDate()+"@testF.testF");

        save();

        messageResultOfOperation("филиал");

        goTo("Общее","Предприятие");
        goTo("Филиалы");
        selectRow(0);
        legend="Филиал";
        assertEquals(getValue("Полное наименование"),data,"Филиал не изменен");
    }

    @Test(priority=200, groups = { "empty" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        goTo("Общее","Предприятие");
        goTo("Филиалы");
        selectRow(0);

        legend="Филиал";
        input("Полное наименование","");
        input("Сокращенное наименование","");

        legend="";
        input("Почтовый индекс","");
        input("Местонахождение (юридический адрес) предприятия", "", "", "");
        input("Код ОКАТО","");
        input("Телефон","--");

        save();

        messageResultOfOperation("филиал");

        String[] s=checkEmptyFields();
        assertTrue(s.length==0,"Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length==0 ? "" : getScreenshot("fail")));
    }
}
